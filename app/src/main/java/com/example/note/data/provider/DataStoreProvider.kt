package com.example.note.data.provider

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory

import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.note.data.repository.impliment.DataStorePreferencesRepositoryImpl
import com.example.note.data.repository.repo.DataStorePreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val APP_PREFERENCES = "app_preferences"

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun bindAppPreferencesRepository(
        appPreferencesRepositoryImpl: DataStorePreferencesRepositoryImpl
    ): DataStorePreferencesRepository

    companion object {
        @Singleton
        @Provides
        fun provideAppPreferencesDataStore(
            @ApplicationContext appContext: Context
        ): DataStore<androidx.datastore.preferences.core.Preferences> {
            return PreferenceDataStoreFactory.create(
                produceFile = { appContext.preferencesDataStoreFile(APP_PREFERENCES) }
            )
        }

    }
}
