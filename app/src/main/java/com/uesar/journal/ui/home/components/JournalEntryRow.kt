package com.uesar.journal.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uesar.journal.domain.mood.moods
import com.uesar.journal.domain.PlayerState
import com.uesar.journal.ui.components.AudioPlayerUI
import com.uesar.journal.ui.model.JournalEntryUIState
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.JournalTheme
import com.uesar.journal.ui.theme.OnSurfaceVariant
import com.uesar.journal.ui.theme.OutlineVariant
import com.uesar.journal.ui.theme.Surface
import com.uesar.journal.ui.theme.smallPadding
import com.uesar.journal.ui.theme.standardPadding
import java.util.Date

@Composable
fun JournalEntryRow(
    modifier: Modifier = Modifier,
    journalEntry: JournalEntryUIState,
    startPlaying: () -> Unit = {},
    resumePlaying: () -> Unit = {},
    pausePlaying: () -> Unit = {},
    currentTime: Int = 0,
    totalTime: Int = 0,
    playerState: PlayerState = PlayerState.Idle
) {
    Row(modifier = modifier) {
        journalEntry.mood?.icon?.let { iconRes ->
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = journalEntry.mood.name
            )
        }
        Card(
            modifier = Modifier.padding(start = standardPadding),
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        ) {
            Column(modifier = Modifier.padding(standardPadding)) {
                Row { Text(text = journalEntry.title, style = MaterialTheme.typography.titleSmall) }
                AudioPlayerUI(
                    modifier = Modifier.padding(top = smallPadding),
                    startPlaying = {startPlaying()},
                    resumePlaying = {resumePlaying()},
                    pausePlaying = {pausePlaying()},
                    currentTime = currentTime,
                    totalTime = totalTime,
                    playerState = playerState
                )
                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    journalEntry.topics.sortedByDescending { it.length }.forEach { topic ->
                        InputChip(
                            modifier = Modifier.padding(end = smallPadding),
                            colors = InputChipDefaults.inputChipColors(
                                containerColor = InverseOnSurface,
                                labelColor = OnSurfaceVariant,
                                selectedContainerColor = InverseOnSurface
                            ),
                            shape = RoundedCornerShape(999.dp),
                            onClick = { },
                            label = {
                                Text(
                                    text = topic
                                )
                            },
                            selected = true,
                            leadingIcon = {
                                Text(
                                    modifier = Modifier.size(16.dp),
                                    text = "#",
                                    color = OutlineVariant,
                                    fontSize = 16.sp
                                )
                            })
                    }
                }
                Text(modifier = Modifier.padding(top = smallPadding), text = journalEntry.description,  style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JournalEntryRowPreview() {
    JournalTheme {
        JournalEntryRow(
            modifier = Modifier.fillMaxWidth(),
            journalEntry = JournalEntryUIState(
                title = "My Entry",
                topics = listOf("topic 1", "topic 2"),
                description = "This is a description",
                audioPath = "",
                mood = moods.first(),
                date = Date()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JournalEntryRowMultipleTopicsPreview() {
    JournalTheme {
        JournalEntryRow(
            modifier = Modifier.fillMaxWidth(),
            journalEntry = JournalEntryUIState(
                title = "My Entry",
                topics = listOf("topic 1", "topic 2", "topic 3 topic 3", "topic 4", "topic 5", "topic 6"),
                description = "This is a description",
                audioPath = "",
                mood = moods.first(),
                date = Date()
            )
        )
    }
}