package com.uesar.journal

import kotlinx.serialization.Serializable

@Serializable
data class Recording(
    val totalTime: Long,
    val currentTime: Long,
    val audio: String
)
