package io.github.kk_mats.ccfinderxserver.domain.type.limitation

import io.github.kk_mats.ccfinderxserver.common.orUndefined
import io.github.kk_mats.ccfinderxserver.domain.type.*


class RangeLimitation<T : Comparable<T>>(
        label: Label, private val min: T?, private val max: T?, private val default: T?
) : Limitation<T>(label) {
    init {
        if (this.default != null) {
            if (this.min != null) {
                require(this.min <= this.default) {
                    FailureCode.parameterDefaultOutOfRange(this.label, this.default, this.min, this.max)
                }
            }
            if (this.max != null) {
                require(this.default <= this.max) {
                    FailureCode.parameterDefaultOutOfRange(this.label, this.default, this.min, this.max)
                }
            }
        } else if (this.min != null && this.max != null) {
            require(this.min < this.max) {
                FailureCode.invalidParameterRange(this.label, this.min, this.max)
            }
        }
    }

    override fun validate(value: T?): Failable<T> {
        if (value == null) {
            return when {
                this.default != null -> succeed(this.default)
                else -> fail(FailureCode.parameterUndefinedWithoutDefault(this.label))
            }
        }

        if (this.min != null && value < this.min) {
            return fail(FailureCode.parameterOutOfRange(this.label, value, this.min, this.max))
        }

        if (this.max != null && this.max < value) {
            return fail(FailureCode.parameterOutOfRange(this.label, value, this.min, this.max))
        }

        return succeed(value)
    }

    override fun toString(): String {
        return """
			|@rangeParameter=${this.label.name}
			|   name: ${this.label.name}
			|   slug: ${this.label.slug}
			|   option: ${this.label.option}
			|   range:
			|       min: ${orUndefined(this.min)}
			|       max: ${orUndefined(this.max)}
			|       default: ${orUndefined(this.default)}
		""".trimMargin()
    }

}
