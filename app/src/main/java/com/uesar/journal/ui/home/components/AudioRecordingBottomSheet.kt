package com.uesar.journal.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uesar.journal.R
import com.uesar.journal.ui.theme.ButtonGradient
import com.uesar.journal.ui.theme.ErrorContainer
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.OnErrorContainer
import com.uesar.journal.ui.theme.Primary

@Composable
fun AudioRecordingBottomSheet(
    timePassed: String,
    isRecording: Boolean,
    onCancelButtonClicked: () -> Unit,
    onResumeButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isRecording) "Recording your memories..." else "Recording Paused")
        Text(timePassed)
        Row(
            horizontalArrangement = Arrangement.spacedBy(71.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 51.dp, bottom = 25.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ErrorContainer)
                    .clickable { onCancelButtonClicked() }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.close),
                    contentDescription = null,
                    tint = OnErrorContainer
                )
            }
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(ButtonGradient)
                    .clickable(
                        onClick = { if (isRecording) onSaveButtonClicked() else onResumeButtonClicked() }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = if (isRecording) painterResource(R.drawable.check) else painterResource(
                        R.drawable.mic
                    ),
                    contentDescription = if (isRecording) "Finish Recording" else "Start Recording",
                    tint = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(InverseOnSurface)
                    .clickable { if (isRecording) onPauseButtonClicked() else onSaveButtonClicked() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (isRecording) painterResource(R.drawable.pause) else painterResource(
                        R.drawable.check
                    ), contentDescription = null, tint = Primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AudioRecordingBottomSheetPreview() {
    AudioRecordingBottomSheet("00:00:00", true, {}, {}, {}, {})
}