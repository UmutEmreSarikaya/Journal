package com.uesar.journal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uesar.journal.Mood
import com.uesar.journal.moods
import com.uesar.journal.ui.theme.JournalTheme
import com.uesar.journal.ui.theme.standardPadding

@Composable
fun MoodSelectorRow(
    modifier: Modifier = Modifier,
    selectedMood: Mood? = null,
    onMoodClicked: (Mood) -> Unit
) {
    Row(
        modifier = modifier
            .padding(standardPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        moods.forEach { mood ->
            val moodIcon = if (mood == selectedMood) mood.icon else mood.emptyIcon
            MoodIcon(
                moodIcon = moodIcon,
                name = mood.name,
                onMoodClicked = { onMoodClicked(mood) })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MoodSelectorRowPreview() {
    JournalTheme {
        MoodSelectorRow(onMoodClicked = {})
    }
}