package com.uesar.journal.ui

sealed class PlayerState {
    object Idle : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
    object Completed : PlayerState()
}