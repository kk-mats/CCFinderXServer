package io.github.kk_mats.ccfinderxserver.domain.service

import io.github.kk_mats.ccfinderxserver.domain.repository.DetectorInfoRepository
import io.github.kk_mats.ccfinderxserver.domain.type.*
import io.github.kk_mats.ccfinderxserver.domain.type.limitation.Limitation
import io.github.kk_mats.ccfinderxserver.domain.type.query.DetectionQuery
import io.github.kk_mats.ccfinderxserver.domain.type.query.RawDetectionQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidationService {

    @Autowired
    lateinit var detectorInfoRepository: DetectorInfoRepository

    fun validate(version: String?, query: RawDetectionQuery): Failable<DetectionQuery> {
        val inspectedParameters = HashMap<Label, String>()
        var r: Failable<*>

        val v = this.detectorInfoRepository.version().validate(
                when (version) {
                    null -> null; else -> Label(version)
                }
        )
        v.value ?: return v.delegate()

        r = this.rangeValidate(
                this.detectorInfoRepository.intRangeLimitations(),
                String::toIntOrNull,
                query.parameters,
                inspectedParameters
        )
        r.value ?: return r.delegate()

        for (limitations in listOf(
                this.detectorInfoRepository.variantLimitations(),
                this.detectorInfoRepository.regexLimitations()
        )) {
            r = this.stringValidate(limitations, query.parameters, inspectedParameters)
            r.value ?: return r.delegate()
        }

        if (query.parameters.isNotEmpty()) {
            return fail(FailureCode.unknownParameterEncountered(*query.parameters.keys.toTypedArray()))
        }

        return succeed(DetectionQuery(query.target, query.output, v.value, inspectedParameters))
    }

    private fun stringValidate(
            limitations: HashSet<out Limitation<String>>,
            rawParameters: HashMap<String, String>,
            inspectedParameters: HashMap<Label, String>
    ): Failable<String> {
        var r: Failable<String>
        for (limitation in limitations) {
            val value = rawParameters.remove(limitation.label.slug)
            r = limitation.validate(value)
            r.value ?: return r.delegate()

            inspectedParameters[limitation.label] = r.value.toString()
        }
        return succeed("");
    }

    private fun <T : Number> rangeValidate(
            limitations: HashSet<out Limitation<T>>,
            converter: (String) -> T?,
            rawParameters: HashMap<String, String>,
            inspectedParameters: HashMap<Label, String>
    ): Failable<String> {
        var r: Failable<T>
        for (limitation in limitations) {
            val value = rawParameters.remove(limitation.label.slug)
            if (value == null) {
                r = limitation.validate(null)
                r.failure ?: continue
            } else {
                r = when (val convertedValue = converter(value)) {
                    null -> fail(FailureCode.invalidRangeParameterType(limitation.label, value))
                    else -> limitation.validate(convertedValue)
                }
            }

            r.value ?: return r.delegate()
            inspectedParameters[limitation.label] = r.value.toString()
        }
        return succeed("")
    }
}