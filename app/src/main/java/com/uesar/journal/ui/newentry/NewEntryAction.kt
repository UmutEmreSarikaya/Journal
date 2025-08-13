package com.uesar.journal.ui.newentry

import com.uesar.journal.Mood

sealed interface NewEntryAction {
    data class OnTitleChanged(val title: String) : NewEntryAction
    data class OnTopicChanged(val topic: String) : NewEntryAction
    data class OnDescriptionChanged(val description: String) : NewEntryAction
    data class OnMoodIconClicked(val selectedMood: Mood) : NewEntryAction
    data class OnConfirmMoodClicked(val selectedMood: Mood) : NewEntryAction
    data class OnRemoveTopic(val topic: String) : NewEntryAction
    data class OnAddTopic(val topic: String) : NewEntryAction
    data class DeleteAudioFile(val audioPath: String): NewEntryAction
    data object NavigateBack : NewEntryAction
    data object CloseTopicDropDown : NewEntryAction
    data object OpenTopicDropDown : NewEntryAction
    data object BottomSheetClosed : NewEntryAction
    data object OnChangeMoodClicked : NewEntryAction
    data object OnCancelMoodBottomSheet : NewEntryAction
    data object OnAITranscribeButtonClicked : NewEntryAction
    data object StartPlaying : NewEntryAction
    data object StopPlaying : NewEntryAction
    data object ResumePlaying : NewEntryAction
    data object PausePlaying : NewEntryAction
    data object OpenNavigationDialog : NewEntryAction
    data object CloseNavigationDialog : NewEntryAction
    data object SaveEntry : NewEntryAction
}