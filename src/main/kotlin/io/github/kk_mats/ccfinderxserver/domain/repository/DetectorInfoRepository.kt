package io.github.kk_mats.ccfinderxserver.domain.repository

import io.github.kk_mats.ccfinderxserver.domain.type.Label
import io.github.kk_mats.ccfinderxserver.domain.type.limitation.*

interface DetectorInfoRepository {
    fun isSupportedVersion(slug: String): Boolean
    fun version(): VariantLimitation<Label>

    fun intRangeLimitations(): HashSet<RangeLimitation<Int>>
    fun regexLimitations(): HashSet<RegexLimitation>
    fun variantLimitations(): HashSet<VariantLimitation<String>>
}