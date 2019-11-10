package io.github.kk_mats.ccfinderxserver.domain.type

fun toSlug(str: String): String {
    return str.replace(Regex("^-+"), "")
}

class Label(
        val option: String,
        val name: String = option,
        val slug: String = toSlug(option),
        val priority: Int = 0
) : Comparable<Label> {
    constructor(option: String, priority: Int) : this(option, option, toSlug(option), priority)
    constructor(option: String, name: String = toSlug(option), priority: Int) : this(
            option,
            name,
            toSlug(option),
            priority
    )

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            this.javaClass != other?.javaClass -> false
            else -> {
                other as Label
                return this.slug == other.slug
            }
        }
    }

    override fun hashCode(): Int = this.slug.hashCode()

    override fun compareTo(other: Label): Int {
        return this.slug.compareTo(other.slug)
    }

    override fun toString(): String {
        return this.slug
    }
}