package com.uesar.journal

import com.uesar.journal.ui.home.HomeViewModel
import com.uesar.journal.ui.newentry.NewEntryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::NewEntryViewModel)
}