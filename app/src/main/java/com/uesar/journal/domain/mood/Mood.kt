package com.uesar.journal.domain.mood

import androidx.annotation.DrawableRes
import com.uesar.journal.R

data class Mood(
    val name: String
    @param:DrawableRes val icon: Int,
    @param:DrawableRes val emptyIcon: Int,
)

val moods = listOf(
    Mood(R.drawable.stress, R.drawable.stress_empty, "Stress"),
    Mood(R.drawable.sad, R.drawable.sad_empty, "Sad"),
    Mood(R.drawable.neutral, R.drawable.neutral_empty, "Neutral"),
    Mood(R.drawable.peaceful, R.drawable.peaceful_empty, "Peaceful"),
    Mood(R.drawable.excited, R.drawable.excited_empty, "Excited")
)
