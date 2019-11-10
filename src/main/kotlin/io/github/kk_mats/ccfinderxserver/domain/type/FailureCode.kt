package io.github.kk_mats.ccfinderxserver.domain.type

object FailureCode {
    fun parameterUndefinedWithoutDefault(label: Label) = Failure(
            "PARAMETER_UNDEFINED_WITHOUT_DEFAULT",
            "@${label}: undefined without default value"
    )

    fun <T> parameterOutOfRange(label: Label, value: T, min: T?, max: T?) = Failure(
            "PARAMETER_OUT_OF_RANGE",
            "@${label}: \"$value\" is out of range [${min ?: "undefined"}, ${max ?: "undefined"}]"
    )

    fun invalidRangeParameterType(label: Label, value: String) = Failure(
            "INVALID_RANGE_PARAMETER_TYPE",
            "@${label}: type of \"$value\" is not acceptable"
    )

    fun <T> invalidParameterRange(label: Label, min: T?, max: T?) = Failure(
            "PARAMETER_OUT_OF_RANGE",
            "@${label}: invalid range [${min ?: "undefined"}, ${max ?: "undefined"}]"
    )

    fun <T> parameterDefaultOutOfRange(label: Label, value: T, min: T?, max: T?) = Failure(
            "PARAMETER_DEFAULT_OUT_OF_RANGE",
            "@${label}: \"$value\" is out of range [${min ?: "undefined"}, ${max ?: "undefined"}]"
    )

    fun <T> parameterNotContained(label: Label, value: T) = Failure(
            "PARAMETER_NOT_CONTAINED",
            "@${label}: \"$value\" is not contained in variants"
    )

    fun parameterDefaultNotMatched(label: Label, regex: Regex, default: String) = Failure(
            "PARAMETER_DEFAULT_NOT_MATCHED",
            "@${label}: default value \"${default}\" is not matched by the regex pattern \"${regex.pattern}\""
    )

    fun parameterNotMatched(label: Label, regex: Regex, value: String) = Failure(
            "PARAMETER_DEFAULT_NOT_MATCHED",
            "@${label}: value \"${value}\" is not matched by the regex pattern \"${regex.pattern}\""
    )

    fun unknownParameterEncountered(vararg labels: String) = Failure(
            "UNKNOWN_PARAMETER_ENCOUNTERED",
            "@${labels.toList()}: unknown parameter(s) encountered"
    )

    fun detectionFailed(command: String, message: String) = Failure(
            "DETECTION_FAILED",
            "Detection failed at command \"${command}\"\n${message}"
    )
}