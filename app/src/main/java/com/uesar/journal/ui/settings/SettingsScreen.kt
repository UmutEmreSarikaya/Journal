package com.uesar.journal.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uesar.journal.R
import com.uesar.journal.ui.components.MoodSelectorRow
import com.uesar.journal.ui.theme.JournalTheme
import com.uesar.journal.ui.theme.Surface
import com.uesar.journal.ui.theme.standardPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = koinViewModel(), navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is SettingsAction.NavigateBack -> {
                    navigateBack()
                }

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(stringResource(R.string.settings))
        }, navigationIcon = {
            IconButton(onClick = { onAction(SettingsAction.NavigateBack) }) {
                Icon(
                    painter = painterResource(R.drawable.navigate_before), contentDescription = null
                )
            }
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(standardPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Surface),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            ) {
                Text(
                    modifier = Modifier.padding(top = standardPadding, start = standardPadding),
                    text = stringResource(R.string.my_mood)
                )
                Text(
                    modifier = Modifier.padding(start = standardPadding),
                    text = stringResource(R.string.select_default_mood)
                )
                MoodSelectorRow(selectedMood = state.selectedMood, onMoodClicked = {
                    onAction(
                        SettingsAction.OnMoodClicked(it)
                    )
                })
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    JournalTheme {
        SettingsScreen(
            state = SettingsState(),
            onAction = {}
        )
    }
}
