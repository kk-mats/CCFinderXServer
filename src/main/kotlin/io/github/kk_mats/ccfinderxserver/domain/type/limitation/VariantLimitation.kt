package io.github.kk_mats.ccfinderxserver.domain.type.limitation

import io.github.kk_mats.ccfinderxserver.domain.type.*


class VariantLimitation<T : Any> private constructor(
        label: Label,
        private val default: T?,
        private val variants: HashSet<T>
) : Limitation<T>(label) {
    init {
        if (default != null) {
            require(variants.contains(default)) {
                FailureCode.parameterNotContained(this.label, this.default)
            }
        }
    }

    constructor(label: Label, default: T?, vararg variants: T)
            : this(label, default, hashSetOf(*variants))

    override fun validate(value: T?): Failable<T> {
        if (value == null) {
            return when {
                this.default != null -> succeed(this.default)
                else -> fail(FailureCode.parameterUndefinedWithoutDefault(this.label))
            }
        }

        return when (val r = this.variants.find(value::equals)) {
            null -> fail(FailureCode.parameterNotContained(this.label, value.toString()))
            else -> succeed(r)
        }
    }


    override fun toString(): String {
        return """
			|@variantParameter=${this.label.name}
			|   name: ${this.label.name}
			|   slug: ${this.label.slug}
			|   option: ${this.label.option}
			|   variant:
			|       ${this.variants.joinToString("", "- ", "\n")}
		""".trimMargin()
    }
}
