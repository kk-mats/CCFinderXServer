package io.github.kk_mats.ccfinderxserver.domain.type.limitation

import io.github.kk_mats.ccfinderxserver.domain.type.Failable
import io.github.kk_mats.ccfinderxserver.domain.type.Label

abstract class Limitation<T : Any>(val label: Label) {
    abstract fun validate(value: T?): Failable<T>
    abstract override fun toString(): String

    final override fun hashCode(): Int = this.label.slug.hashCode()
}
