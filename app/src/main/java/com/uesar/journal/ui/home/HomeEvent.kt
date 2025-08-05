package com.uesar.journal.ui.home

sealed interface HomeEvent {
    data class AudioRecorded(val audioPath: String) : HomeEvent
}