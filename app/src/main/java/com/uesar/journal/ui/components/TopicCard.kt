package com.uesar.journal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uesar.journal.R
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.JournalTheme
import com.uesar.journal.ui.theme.OnSurfaceVariant
import com.uesar.journal.ui.theme.smallPadding

@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
    topic: String
) {
    Row(
        modifier = modifier
            .height(44.dp).padding(horizontal = smallPadding)
            .background(InverseOnSurface, shape = RoundedCornerShape(999.dp)), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.padding(horizontal = smallPadding), text = "# $topic", color = OnSurfaceVariant)
        Icon(
            modifier = Modifier.padding(end = smallPadding).size(16.dp),
            painter = painterResource(R.drawable.close),
            contentDescription = stringResource(R.string.remove_topic),
            tint = OnSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicCardPreview() {
    JournalTheme {
        TopicCard(topic = "Topic 1")
    }
}