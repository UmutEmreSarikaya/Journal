package com.uesar.journal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uesar.journal.R
import com.uesar.journal.ui.PlayerState
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.JournalTheme
import com.uesar.journal.ui.theme.standardPadding
import com.uesar.journal.ui.utils.formatSecondsToMinutes

@Composable
fun AudioPlayerUI(
    modifier: Modifier = Modifier,
    startPlaying: () -> Unit,
    resumePlaying: () -> Unit,
    pausePlaying: () -> Unit,
    currentTime: Int,
    totalTime: Int,
    playerState: PlayerState = PlayerState.Idle
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .background(InverseOnSurface, shape = RoundedCornerShape(999.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(44.dp)
                .shadow(elevation = 10.dp, shape = CircleShape)
                .background(Color.White, CircleShape)
                .clickable {
                    when (playerState) {
                        PlayerState.Playing -> pausePlaying()
                        PlayerState.Paused, PlayerState.Completed -> resumePlaying()
                        PlayerState.Idle -> startPlaying()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(
                    if (playerState == PlayerState.Playing) R.drawable.pause
                    else R.drawable.play_arrow_filled
                ),
                contentDescription = null,
                tint = Color.Black
            )
        }
        LinearProgressIndicator(
        progress = { currentTime.toFloat() / totalTime.toFloat() },
        modifier = Modifier.padding(horizontal = standardPadding).weight(1F),
        strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
        )
        Text(
            modifier = Modifier.padding(end = standardPadding),
            text = "${formatSecondsToMinutes(currentTime / 1000)}/${formatSecondsToMinutes(totalTime / 1000)}"
        )
    }
}

@Preview
@Composable
private fun AudioPlayerUIPreview() {
    JournalTheme {
        AudioPlayerUI(
            startPlaying = {},
            resumePlaying = {},
            pausePlaying = {},
            currentTime = 0,
            totalTime = 0
        )
    }
}