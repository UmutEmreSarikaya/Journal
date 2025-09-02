package com.uesar.journal.domain.mood

import androidx.annotation.DrawableRes
import com.uesar.journal.R
import kotlinx.serialization.Serializable

@Serializable
data class Mood(
    @DrawableRes val icon: Int,
    @DrawableRes val emptyIcon: Int,
    val name: String
)

val moods = listOf(
    Mood(R.drawable.stress, R.drawable.stress_empty, "Stress"),
    Mood(R.drawable.sad, R.drawable.sad_empty, "Sad"),
    Mood(R.drawable.neutral, R.drawable.neutral_empty, "Neutral"),
    Mood(R.drawable.peaceful, R.drawable.peaceful_empty, "Peaceful"),
    Mood(R.drawable.excited, R.drawable.excited_empty, "Excited")
)
