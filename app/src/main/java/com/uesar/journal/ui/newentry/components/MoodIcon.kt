package com.uesar.journal.ui.newentry.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uesar.journal.R

@Composable
fun MoodIcon(@DrawableRes moodIcon: Int, name: String, onMoodClicked: () -> Unit = {}) {
    Column(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onMoodClicked()
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(moodIcon),
            contentDescription = null
        )
        Text(name)
    }
}

@Preview(showBackground = true)
@Composable
private fun MoodIconPreview() {
    MoodIcon(R.drawable.sad, "Sad")
}