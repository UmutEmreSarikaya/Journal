package com.uesar.journal.ui.home

data class HomeState(
    val entryCount: Int = 0,
    val isBottomSheetOpen: Boolean = false,
    val isRecording: Boolean = false,
    val recordingTime: String = "00:00:00"
) 