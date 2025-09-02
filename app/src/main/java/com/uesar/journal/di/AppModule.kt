package com.uesar.journal.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.uesar.journal.AudioPlayerImpl
import com.uesar.journal.AudioPlayer
import com.uesar.journal.AudioRecorder
import com.uesar.journal.AudioRecorderImpl
import com.uesar.journal.data.local.AppDatabase
import com.uesar.journal.data.local.JournalEntryDao
import com.uesar.journal.data.local.JournalRepositoryImpl
import com.uesar.journal.data.settings.DataStoreSettings
import com.uesar.journal.domain.JournalRepository
import com.uesar.journal.domain.settings.SettingsPreferences
import com.uesar.journal.ui.home.HomeViewModel
import com.uesar.journal.ui.newentry.NewEntryViewModel
import com.uesar.journal.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::NewEntryViewModel)
    viewModelOf(::SettingsViewModel)
    singleOf(::AudioRecorderImpl) bind AudioRecorder::class
    singleOf(::AudioPlayerImpl) bind AudioPlayer::class
    singleOf(::JournalRepositoryImpl) bind JournalRepository::class
    singleOf(::DataStoreSettings) bind SettingsPreferences::class
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "journal_entries"
        ).build()
    }

    single<JournalEntryDao> { get<AppDatabase>().getDao() }
}