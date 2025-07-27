package com.uesar.journal.ui.newentry

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NewEntryViewModel : ViewModel() {
    private val _state = MutableStateFlow(NewEntryState())
    val state: StateFlow<NewEntryState> = _state

    fun onAction(action: NewEntryAction) {
        when (action) {
            is NewEntryAction.OnTitleChanged -> {
                _state.update { it.copy(title = action.title) }
            }

            is NewEntryAction.BottomSheetClosed -> {
                _state.update {
                    it.copy(isBottomSheetOpen = false)
                }
            }

            is NewEntryAction.OnChangeMoodClicked -> {
                _state.update { it.copy(isBottomSheetOpen = true) }
            }

            is NewEntryAction.OnMoodIconClicked -> {
                _state.update { it.copy(currentSelectedMoodInBottomSheet = action.selectedMood) }
            }

            is NewEntryAction.OnConfirmClicked -> {
                _state.update { it.copy(selectedMood = action.selectedMood) }
            }

            is NewEntryAction.OnCancelMoodBottomSheet -> {
                _state.update { it.copy(currentSelectedMoodInBottomSheet = state.value.selectedMood) }
            }

            else -> Unit
        }
    }
}