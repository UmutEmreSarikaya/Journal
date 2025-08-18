package com.uesar.journal.ui.newentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uesar.journal.AudioPlayer
import com.uesar.journal.domain.JournalEntry
import com.uesar.journal.domain.JournalRepository
import com.uesar.journal.ui.utils.formatSecondsToMinutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class NewEntryViewModel(
    private val repository: JournalRepository,
    private val audioPlayer: AudioPlayer
) : ViewModel() {
    private val _state = MutableStateFlow(NewEntryState())
    val state: StateFlow<NewEntryState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            audioPlayer.playerState.collect { playerState ->
                _state.update { it.copy(journalEntryUIState = state.value.journalEntryUIState.copy(playerState = playerState)) }
            }
        }
    }

    fun onAction(action: NewEntryAction) {
        when (action) {
            is NewEntryAction.OnTitleChanged -> {
                _state.update { it.copy(title = action.title) }
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
                _state.update { it.copy(selectedMood = action.selectedMood) }
            }

            NewEntryAction.OnCancelMoodBottomSheet -> {
                _state.update { it.copy(currentSelectedMoodInBottomSheet = state.value.selectedMood) }
            }

            NewEntryAction.OnAITranscribeButtonClicked -> {

            }

            NewEntryAction.StartPlaying -> {
                audioPlayer.startPlayback(state.value.playback.audioPath)
                viewModelScope.launch {
                    audioPlayer.getCurrentPosition().collect { time ->
                        _state.update {
                            it.copy(
                                playback = state.value.playback.copy(
                                    currentTime = formatSecondsToTime(time)
                                    currentTime = formatSecondsToMinutes(currentTime)
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
                if (!state.value.topics.contains(action.topic)) {
                    _state.update { it.copy(topics = it.topics + action.topic) }
                }
            }

            is NewEntryAction.OnRemoveTopic -> {
                _state.update { it.copy(topics = it.topics - action.topic) }
            }

            NewEntryAction.CloseNavigationDialog -> {
                _state.update { it.copy(isNavigationDialogOpen = false) }
            }

            NewEntryAction.OpenNavigationDialog -> {
                _state.update { it.copy(isNavigationDialogOpen = true) }
            }

            is NewEntryAction.OnDescriptionChanged -> {
                _state.update { it.copy(description = action.description) }
            }

            NewEntryAction.SaveEntry -> {
                viewModelScope.launch {
                    repository.insertJournalEntry(
                        JournalEntry(
                            title = state.value.title,
                            audioPath = state.value.playback.audioPath,
                            mood = state.value.selectedMood!!,
                            description = state.value.description,
                            topics = state.value.topics,
                            date = Date()
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
        _state.update { it.copy(playback = state.value.playback.copy(audioPath = audioPath)) }
        _state.update {
            it.copy(
                playback = state.value.playback.copy(
                    totalTime = formatSecondsToTime(
                        audioPlayer.getDuration(File(audioPath))
                    totalTime = formatSecondsToMinutes(
                    )
                )
            )
        }
    }
}