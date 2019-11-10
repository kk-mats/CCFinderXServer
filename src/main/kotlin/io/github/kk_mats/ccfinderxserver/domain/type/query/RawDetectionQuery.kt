package io.github.kk_mats.ccfinderxserver.domain.type.query

import io.github.kk_mats.ccfinderxserver.domain.type.Label

data class TargetPath(val relative: String, val absolute: String)

data class RawDetectionQuery(val target: TargetPath, val output: String, val parameters: HashMap<String, String>)

data class DetectionQuery(
        val target: TargetPath,
        val output: String,
        val version: Label,
        val parameters: HashMap<Label, String>
)