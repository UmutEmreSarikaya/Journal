package com.uesar.journal.domain

sealed class PlayerState {
    object Idle : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
    object Completed : PlayerState()
}