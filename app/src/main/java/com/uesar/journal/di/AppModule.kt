package com.uesar.journal.di

import androidx.room.Room
import com.uesar.journal.AudioPlayerImpl
import com.uesar.journal.AudioPlayer
import com.uesar.journal.AudioRecorder
import com.uesar.journal.AudioRecorderImpl
import com.uesar.journal.data.local.AppDatabase
import com.uesar.journal.data.local.JournalEntryDao
import com.uesar.journal.data.local.JournalRepositoryImpl
import com.uesar.journal.domain.JournalRepository
import com.uesar.journal.ui.home.HomeViewModel
import com.uesar.journal.ui.newentry.NewEntryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::NewEntryViewModel)
    singleOf(::AudioRecorderImpl).bind<AudioRecorder>()
    singleOf(::AudioPlayerImpl).bind<AudioPlayer>()
    singleOf(::JournalRepositoryImpl).bind<JournalRepository>()
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "journal_entries"
        ).build()
    }

    single<JournalEntryDao> { get<AppDatabase>().getDao() }
}