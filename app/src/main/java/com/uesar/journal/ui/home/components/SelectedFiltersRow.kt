package com.uesar.journal.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.uesar.journal.domain.mood.moods
import com.uesar.journal.ui.theme.JournalTheme
import com.uesar.journal.ui.theme.smallPadding
import com.uesar.journal.ui.utils.Utils.formatFilterList

@Composable
fun SelectedFilterRow(
    filterList: List<String>,
    onClearClicked: () -> Unit = {}
) {
    if (filterList.isEmpty()) {
        Text(stringResource(R.string.all_moods))
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy((-4).dp)) {
                filterList.forEach { moodName ->
                    val mood = moods.firstOrNull { it.name == moodName }
                    mood?.let {
                        Image(
                            painter = painterResource(it.icon),
                            contentDescription = it.name,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            val selectedMoods = formatFilterList(filterList)
            Text(modifier = Modifier.padding(start = smallPadding), text = selectedMoods)
            Icon(modifier = Modifier.padding(start = smallPadding).clickable{onClearClicked()}, painter = painterResource(R.drawable.close), contentDescription = stringResource(R.string.close))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectedFilterRowPreview() {
    JournalTheme {
        SelectedFilterRow(listOf("Stress", "Neutral", "Sad"))
    }
}