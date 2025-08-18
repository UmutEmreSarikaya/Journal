package com.uesar.journal.ui.newentry

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uesar.journal.R
import com.uesar.journal.ui.components.AudioPlayerUI
import com.uesar.journal.ui.newentry.components.MoodSelectorBottomSheet
import com.uesar.journal.ui.theme.InverseOnSurface
import com.uesar.journal.ui.theme.OnPrimary
import com.uesar.journal.ui.theme.OnSurfaceVariant
import com.uesar.journal.ui.theme.OutlineVariant
import com.uesar.journal.ui.theme.Primary
import com.uesar.journal.ui.theme.PrimaryContainer
import com.uesar.journal.ui.theme.smallPadding
import com.uesar.journal.ui.theme.standardPadding
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewEntryScreenRoot(
    viewModel: NewEntryViewModel = koinViewModel(), audioPath: String, navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.setAudioPath(audioPath)
    }

    NewEntryScreen(
        state = state, onAction = { action ->
            when (action) {
                is NewEntryAction.NavigateBack -> {
                    navigateBack()
                }

                else -> Unit
            }
            viewModel.onAction(action)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewEntryScreen(
    state: NewEntryState, onAction: (NewEntryAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text("New Entry")
        }, navigationIcon = {
            IconButton(onClick = { onAction(NewEntryAction.OpenNavigationDialog) }) {
                Icon(
                    painter = painterResource(R.drawable.navigate_before), contentDescription = null
                )
            }
        })
    }) { innerPadding ->
        BackHandler {
            onAction(NewEntryAction.OpenNavigationDialog)
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(standardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onAction(NewEntryAction.OnChangeMoodClicked) },
                    painter = painterResource(state.selectedMood?.icon ?: R.drawable.change_mood),
                    contentDescription = null
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title,
                    onValueChange = { onAction(NewEntryAction.OnTitleChanged(it)) },
                    placeholder = { Text("Add Title...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = OutlineVariant,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            Row {
                AudioPlayerUI(
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = standardPadding),
                    startPlaying = { onAction(NewEntryAction.StartPlaying) },
                    resumePlaying = { onAction(NewEntryAction.ResumePlaying) },
                    pausePlaying = { onAction(NewEntryAction.PausePlaying) },
                    isPlaying = state.playback.isPlaying,
                    currentTime = state.playback.currentTime,
                    totalTime = state.playback.totalTime
                    playerState = state.journalEntryUIState.playerState
                )
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(elevation = 10.dp, shape = CircleShape)
                        .background(Color.White, CircleShape)
                        .clickable { onAction(NewEntryAction.OnAITranscribeButtonClicked) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ai),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }

            FlowRow(
                modifier = Modifier.padding(top = standardPadding),
                itemVerticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.size(16.dp),
                    text = "#",
                    color = OutlineVariant,
                    fontSize = 16.sp
                )
                state.topics.forEach {
                    InputChip(
                        modifier = Modifier.padding(end = smallPadding),
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = InverseOnSurface,
                            labelColor = OnSurfaceVariant,
                            selectedContainerColor = InverseOnSurface
                        ),
                        shape = RoundedCornerShape(999.dp),
                        onClick = { onAction(NewEntryAction.OnRemoveTopic(it)) },
                        label = {
                            Text(
                                text = it
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
                        },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        onAction(
                                            NewEntryAction.OnRemoveTopic(
                                                it
                                            )
                                        )
                                    },
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.remove_topic),
                                tint = OnSurfaceVariant.copy(alpha = 0.3f),
                            )
                        },
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = state.topicText.isNotEmpty(), onExpandedChange = {}) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor(
                                MenuAnchorType.PrimaryEditable, true
                            )
                            .weight(1F),
                        singleLine = true,
                        value = state.topicText,
                        onValueChange = {
                            onAction(NewEntryAction.OnTopicChanged(it))
                            if (state.topicText.isNotEmpty()) {
                                onAction(NewEntryAction.OpenTopicDropDown)
                            } else {
                                onAction(NewEntryAction.CloseTopicDropDown)
                            }
                        },
                        placeholder = { Text(stringResource(R.string.add_topic)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedPlaceholderColor = OutlineVariant,
                            focusedPlaceholderColor = OutlineVariant,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = state.isTopicDropDownOpen,
                        onDismissRequest = { onAction(NewEntryAction.CloseTopicDropDown) }) {
                        state.topics.forEach {
                            DropdownMenuItem(leadingIcon = {
                                Text(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(start = 4.dp),
                                    text = "#",
                                    color = OutlineVariant,
                                    fontSize = 16.sp
                                )
                            }, text = { Text(it) }, onClick = { /*TODO*/ })
                        }
                        DropdownMenuItem(
                            leadingIcon = {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(R.drawable.add),
                                contentDescription = null
                            )
                        },
                            text = { Text(stringResource(R.string.create, state.topicText)) },
                            onClick = { onAction(NewEntryAction.OnAddTopic(state.topicText)) })
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.mode_edit_outline),
                    tint = OutlineVariant,
                    contentDescription = null
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1F),
                    value = state.description,
                    onValueChange = { onAction(NewEntryAction.OnDescriptionChanged(it)) },
                    placeholder = { Text(stringResource(R.string.add_description)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = OutlineVariant,
                        focusedPlaceholderColor = OutlineVariant,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
            Spacer(Modifier.weight(1F))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = standardPadding),
                horizontalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Button(
                    modifier = Modifier.weight(1.5F), colors = ButtonDefaults.buttonColors(
                        containerColor = InverseOnSurface, contentColor = Primary
                    ), onClick = { onAction(NewEntryAction.OpenNavigationDialog) }) {
                    Text(
                        stringResource(R.string.cancel)
                    )
                }
                Button(
                    modifier = Modifier.weight(3F), colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryContainer, contentColor = OnPrimary
                    ), onClick = {
                        onAction(NewEntryAction.SaveEntry)
                        onAction(NewEntryAction.NavigateBack)
                    }, enabled = state.selectedMood != null && state.title.isNotEmpty()
                ) {
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

            if (state.isBottomSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState, onDismissRequest = {
                        onAction(NewEntryAction.BottomSheetClosed)
                        onAction(NewEntryAction.OnCancelMoodBottomSheet)
                    }) {
                    MoodSelectorBottomSheet(
                        selectedMood = state.currentSelectedMoodInBottomSheet,
                        onCancelButtonClicked = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onAction(NewEntryAction.BottomSheetClosed)
                                onAction(NewEntryAction.OnCancelMoodBottomSheet)
                            }
                        },
                        onConfirmButtonClicked = { mood ->
                            coroutineScope.launch {
                                sheetState.hide()
                                onAction(NewEntryAction.BottomSheetClosed)
                                onAction(
                                    NewEntryAction.OnConfirmMoodClicked(mood)
                                )
                            }
                        },
                        onMoodIconClicked = { mood -> onAction(NewEntryAction.OnMoodIconClicked(mood)) })
                }
            }
        }
    }

    if (state.isNavigationDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(NewEntryAction.CloseNavigationDialog) },
            title = { Text("Are you sure?") },
            text = { Text("Do you want to go back? Unsaved changes may be lost.") },
            confirmButton = {
                Button(onClick = {
                    onAction(NewEntryAction.CloseNavigationDialog)
                    onAction(NewEntryAction.StopPlaying)
                    onAction(NewEntryAction.NavigateBack)
                    onAction(NewEntryAction.DeleteAudioFile(state.playback.audioPath))
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = {
                    onAction(NewEntryAction.CloseNavigationDialog)
                }) {
                    Text("No")
                }
            })
    }
}

@Preview
@Composable
private fun NewEntryScreenPreview() {
    NewEntryScreen(state = NewEntryState(), onAction = {})
}
