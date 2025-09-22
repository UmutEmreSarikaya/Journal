package com.uesar.journal.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uesar.journal.R
import com.uesar.journal.domain.mood.Mood
import com.uesar.journal.domain.mood.moods
import com.uesar.journal.ui.theme.InversePrimary
import com.uesar.journal.ui.theme.JournalTheme
import com.uesar.journal.ui.theme.smallPadding

@Composable
fun FilterPopupRow(
    modifier: Modifier = Modifier,
    mood: Mood,
    onItemClicked: (String) -> Unit,
    filterList: List<String>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (filterList.contains(mood.name)) {
                    modifier.clip(RoundedCornerShape(8.dp)).background(InversePrimary)
                } else {
                    modifier
                }
            )
            .padding(smallPadding)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onItemClicked(mood.name)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = mood.icon),
            contentDescription = mood.name,
            modifier = Modifier
                .size(32.dp)
                .padding(end = smallPadding)
        )
        Text(mood.name)
        Spacer(modifier = Modifier.weight(1f))
        if (filterList.contains(mood.name)) {
            Image(painter = painterResource(R.drawable.check), contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterPopupRowPreview() {
    JournalTheme {
        FilterPopupRow(
            mood = moods.first(),
            onItemClicked = {},
            filterList = listOf("Peaceful", "Neutral", "Stress")
        )
    }
}