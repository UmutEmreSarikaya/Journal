package com.uesar.journal.domain.mood

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.uesar.journal.R
import com.uesar.journal.ui.theme.Excited35
import com.uesar.journal.ui.theme.Neutral35
import com.uesar.journal.ui.theme.Peaceful35
import com.uesar.journal.ui.theme.Sad35
import com.uesar.journal.ui.theme.Stressed35

data class Mood(
    @param:DrawableRes val icon: Int,
    @param:DrawableRes val emptyIcon: Int,
    val name: String,
    val color: Color
)

val moods = listOf(
    Mood(R.drawable.stress, R.drawable.stress_empty, "Stress",  Stressed35),
    Mood(R.drawable.sad, R.drawable.sad_empty, "Sad", Sad35),
    Mood(R.drawable.neutral, R.drawable.neutral_empty, "Neutral", Neutral35),
    Mood(R.drawable.peaceful, R.drawable.peaceful_empty, "Peaceful", Peaceful35),
    Mood(R.drawable.excited, R.drawable.excited_empty, "Excited", Excited35)
)
