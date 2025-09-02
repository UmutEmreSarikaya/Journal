package com.uesar.journal.ui.newentry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.uesar.journal.domain.mood.Mood
import com.uesar.journal.R
import com.uesar.journal.domain.mood.moods
import com.uesar.journal.ui.components.MoodSelectorRow
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.OnPrimary
import com.uesar.journal.ui.theme.Primary
import com.uesar.journal.ui.theme.PrimaryContainer
import com.uesar.journal.ui.theme.largePadding
import com.uesar.journal.ui.theme.smallPadding
import com.uesar.journal.ui.theme.standardPadding

@Composable
fun MoodSelectorBottomSheet(
    selectedMood: Mood?,
    onCancelButtonClicked: () -> Unit,
    onConfirmButtonClicked: (Mood) -> Unit,
    onMoodIconClicked: (Mood) -> Unit
) {
    Column(
        modifier = Modifier.padding(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.how_are_you_doing))
        MoodSelectorRow(modifier = Modifier.padding(top = largePadding), selectedMood = selectedMood, onMoodClicked = onMoodIconClicked)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = standardPadding),
            horizontalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            Button(
                modifier = Modifier.weight(1.5F), colors = ButtonDefaults.buttonColors(
                    containerColor = InverseOnSurface, contentColor = Primary
                ), onClick = { onCancelButtonClicked() }) { Text(stringResource(R.string.cancel)) }
            Button(
                modifier = Modifier.weight(3F), colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryContainer, contentColor = OnPrimary
                ), onClick = { onConfirmButtonClicked(selectedMood ?: moods.first()) }, enabled = selectedMood != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.padding(end = smallPadding),
                        painter = painterResource(R.drawable.check),
                        contentDescription = null
                    )
                    Text(stringResource(R.string.confirm))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MoodSelectorBottomSheetPreview() {
    MoodSelectorBottomSheet(moods.first(), {}, {}, {})
}