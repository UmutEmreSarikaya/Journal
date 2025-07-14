package com.uesar.journal.ui

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route
    @Serializable
    data object NewEntry : Route
}