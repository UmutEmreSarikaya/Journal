package com.uesar.journal.ui.newentry

import com.uesar.journal.Mood

sealed interface NewEntryAction {
    data class OnTitleChanged(val title: String) : NewEntryAction
    data class OnMoodIconClicked(val selectedMood: Mood) : NewEntryAction
    data class OnConfirmClicked(val selectedMood: Mood) : NewEntryAction
    data object NavigateBack : NewEntryAction
    data object BottomSheetClosed : NewEntryAction
    data object OnChangeMoodClicked : NewEntryAction
    data object OnCancelMoodBottomSheet : NewEntryAction
    data object OnAITranscribeButtonClicked : NewEntryAction
}