package io.github.kk_mats.ccfinderxserver.domain.type.limitation

import io.github.kk_mats.ccfinderxserver.common.orUndefined
import io.github.kk_mats.ccfinderxserver.domain.type.*

class RegexLimitation(
        label: Label, private val regex: Regex, private val default: String?
) : Limitation<String>(label) {

    constructor(label: Label, regex: Regex) : this(label, regex, null)

    init {
        if (this.default != null) {
            require(this.regex.matches(this.default)) {
                FailureCode.parameterDefaultNotMatched(label, this.regex, this.default)
            }
        }
    }

    override fun validate(value: String?): Failable<String> {
        if (value == null) {
            return when {
                this.default != null -> succeed(this.default)
                else -> fail(FailureCode.parameterUndefinedWithoutDefault(this.label))
            }
        }

        return when {
            this.regex.matches(value) -> succeed(value)
            else -> fail(FailureCode.parameterNotMatched(this.label, this.regex, value))
        }
    }

    override fun toString(): String {
        return """
			|@regexParameter=${this.label.name}
			|   name: ${this.label.name}
			|   slug: ${this.label.slug}
			|   option: ${this.label.option}
			|   regex:
			|       pattern: ${this.regex.pattern}
			|       default: ${orUndefined(this.default)}
		""".trimMargin()
    }

}