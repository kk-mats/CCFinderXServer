package io.github.kk_mats.ccfinderxserver.domain.type.response

data class DetectionResponse(val id: String, val output: String, val additions: List<String>, val log: String)