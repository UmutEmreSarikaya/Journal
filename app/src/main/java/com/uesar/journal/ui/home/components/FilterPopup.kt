package com.uesar.journal.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.uesar.journal.domain.mood.moods
import com.uesar.journal.ui.theme.JournalTheme

@Composable
fun FilterPopup(
    onDismissRequest: () -> Unit, onItemClicked: (String) -> Unit, filterList: List<String>
) {
    Popup(onDismissRequest = onDismissRequest, offset = IntOffset(0, 42)) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp, vertical = 8.dp
                )
        ) {
            LazyColumn(
                modifier = Modifier.padding(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(moods) { index, mood ->
                    FilterPopupRow(mood = mood, onItemClicked = onItemClicked, filterList = filterList)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterPopupPreview() {
    JournalTheme {
        FilterPopup(onDismissRequest = {}, onItemClicked = {}, filterList = listOf("Peaceful", "Neutral"))
    }
}