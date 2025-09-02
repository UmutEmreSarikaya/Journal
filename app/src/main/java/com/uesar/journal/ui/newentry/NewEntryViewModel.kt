package com.uesar.journal.ui.newentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uesar.journal.domain.player.AudioPlayer
import com.uesar.journal.domain.JournalEntry
import com.uesar.journal.domain.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class NewEntryViewModel(
    private val repository: JournalRepository,
    private val audioPlayer: AudioPlayer
) : ViewModel() {
    private val _state = MutableStateFlow(NewEntryState())
    val state: StateFlow<NewEntryState> = _state.asStateFlow()

    init {
        audioPlayer.playerState.onEach { playerState ->
            _state.update {
                it.copy(
                    journalEntryUIState = state.value.journalEntryUIState.copy(
                        playerState = playerState
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: NewEntryAction) {
        when (action) {
            is NewEntryAction.OnTitleChanged -> {
                _state.update { it.copy(journalEntryUIState = it.journalEntryUIState.copy(title = action.title)) }
            }

            NewEntryAction.BottomSheetClosed -> {
                _state.update {
                    it.copy(isBottomSheetOpen = false)
                }
            }

            NewEntryAction.OnChangeMoodClicked -> {
                _state.update { it.copy(isBottomSheetOpen = true) }
            }

            is NewEntryAction.OnMoodIconClicked -> {
                _state.update { it.copy(currentSelectedMoodInBottomSheet = action.selectedMood) }
            }

            is NewEntryAction.OnConfirmMoodClicked -> {
                _state.update { it.copy(journalEntryUIState = it.journalEntryUIState.copy(mood = action.selectedMood)) }
            }

            NewEntryAction.OnCancelMoodBottomSheet -> {
                _state.update { it.copy(currentSelectedMoodInBottomSheet = state.value.journalEntryUIState.mood) }
            }

            NewEntryAction.OnAITranscribeButtonClicked -> {

            }

            NewEntryAction.StartPlaying -> {
                audioPlayer.startPlayback(state.value.journalEntryUIState.audioPath)
                viewModelScope.launch {
                    audioPlayer.getCurrentPosition().collect { currentTime ->
                        _state.update {
                            it.copy(
                                journalEntryUIState = state.value.journalEntryUIState.copy(
                                    currentTime = currentTime
                                )
                            )
                        }
                    }
                }
            }

            NewEntryAction.StopPlaying -> {
                audioPlayer.stopPlayback()
            }

            NewEntryAction.ResumePlaying -> {
                audioPlayer.resumePlayback()
            }

            NewEntryAction.PausePlaying -> {
                audioPlayer.pausePlayback()
            }

            is NewEntryAction.OnTopicChanged -> {
                _state.update { it.copy(topicText = action.topic) }
            }

            NewEntryAction.CloseTopicDropDown -> {
                _state.update { it.copy(isTopicDropDownOpen = false) }
            }

            NewEntryAction.OpenTopicDropDown -> {
                _state.update { it.copy(isTopicDropDownOpen = true) }
            }


            is NewEntryAction.OnAddTopic -> {
                if (!state.value.journalEntryUIState.topics.contains(action.topic)) {
                    _state.update { it.copy(journalEntryUIState = it.journalEntryUIState.copy(topics = it.journalEntryUIState.topics + action.topic)) }
                }
            }

            is NewEntryAction.OnRemoveTopic -> {
                _state.update { it.copy(journalEntryUIState = it.journalEntryUIState.copy(topics = it.journalEntryUIState.topics - action.topic)) }
            }

            NewEntryAction.CloseNavigationDialog -> {
                _state.update { it.copy(isNavigationDialogOpen = false) }
            }

            NewEntryAction.OpenNavigationDialog -> {
                _state.update { it.copy(isNavigationDialogOpen = true) }
            }

            is NewEntryAction.OnDescriptionChanged -> {
                _state.update {
                    it.copy(
                        journalEntryUIState = it.journalEntryUIState.copy(
                            description = action.description
                        )
                    )
                }
            }

            NewEntryAction.SaveEntry -> {
                viewModelScope.launch {
                    repository.insertJournalEntry(
                        JournalEntry(
                            title = state.value.journalEntryUIState.title,
                            audioPath = state.value.journalEntryUIState.audioPath,
                            mood = state.value.journalEntryUIState.mood!!,
                            description = state.value.journalEntryUIState.description,
                            topics = state.value.journalEntryUIState.topics,
                            date = state.value.journalEntryUIState.date
                        )
                    )
                }
            }

            is NewEntryAction.DeleteAudioFile -> { action
                val file = File(action.audioPath)
                file.delete()
            }

            NewEntryAction.NavigateBack -> {
                //No implementation here
            }
        }
    }

    fun setAudioPath(audioPath: String) {
        _state.update { it.copy(journalEntryUIState = state.value.journalEntryUIState.copy(audioPath = audioPath)) }
        _state.update {
            it.copy(
                journalEntryUIState = state.value.journalEntryUIState.copy(
                    totalTime = audioPlayer.getDuration(File(audioPath))
                )
            )
        }
    }
}