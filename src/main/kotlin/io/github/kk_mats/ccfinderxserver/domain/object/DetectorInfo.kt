package io.github.kk_mats.ccfinderxserver.domain.`object`

import io.github.kk_mats.ccfinderxserver.domain.type.Label
import io.github.kk_mats.ccfinderxserver.domain.type.limitation.*

object DetectorInfo {
    val name = Label("CCFinderX", "ccfx")
    val versions = VariantLimitation(
            Label("version"),
            Label("c:/ccfx/10.2.7.4/bin/ccfx.exe", "10.2.7.4", "10.2.7.4", Int.MAX_VALUE),
            Label("c:/ccfx/10.2.7.4/bin/ccfx.exe", "10.2.7.4", "10.2.7.4", Int.MAX_VALUE)
    )

    val intRangeLimitations = hashSetOf(
            RangeLimitation(Label("-b", "b"), 1, null, 50),
            RangeLimitation(Label("-t", "t"), 1, null, 12)
    )

    val regexLimitations = hashSetOf(
            RegexLimitation(Label("-o", "output"), Regex("""[\w-_\.\\//]+"""), "a.ccfxd")
    )

    val variantLimitations = hashSetOf(
            VariantLimitation(Label("d", "language", "lang", Int.MAX_VALUE - 1), null, "cobol", "cpp", "csharp", "java", "visualbasic", "plaintext"),
            VariantLimitation(Label("-w"), "f+w+", "f-", "w-", "f+", "f+", "f-w+", "f-w-", "f+w-", "f+w+")
    )
}