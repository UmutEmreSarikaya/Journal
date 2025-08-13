package com.uesar.journal.ui.home

sealed interface HomeAction {
    data object SettingsClicked : HomeAction
    data object BottomSheetOpened: HomeAction
    data object BottomSheetClosed: HomeAction
    data object StartRecording: HomeAction
    data object CancelRecording: HomeAction
    data object ResumeRecording: HomeAction
    data object PauseRecording: HomeAction
    data object SaveRecording: HomeAction
    data class StartPlaying(val audioPath: String) : HomeAction
    data object StopPlaying : HomeAction
    data object ResumePlaying : HomeAction
    data object PausePlaying : HomeAction
}