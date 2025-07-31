package com.uesar.journal.ui.newentry

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewEntryViewModel : ViewModel() {
    private val _state = MutableStateFlow(NewEntryState())
    val state: StateFlow<NewEntryState> = _state.asStateFlow()


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

            NewEntryAction.OnPlayButtonClicked -> {

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

            }

            is NewEntryAction.NavigateBack -> {
                //No implementation here
            }
        }
    }
}