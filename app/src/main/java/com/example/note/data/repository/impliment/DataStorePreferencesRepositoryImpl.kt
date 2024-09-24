package com.example.note.data.repository.impliment

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.note.data.model.TaiKhoan
import com.example.note.data.repository.impliment.DataStorePreferencesRepositoryImpl.PreferencesKeys.LAST_LOGIN
import com.example.note.data.repository.repo.DataStorePreferencesRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStorePreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStorePreferencesRepository {

    val TAG = "DataStorePreferencesRepositoryImpl"

    private object PreferencesKeys {
        val FIRST_TIME_LAUNCH = booleanPreferencesKey("FIRST_TIME_LAUNCH")
        val LAST_LOGIN = stringPreferencesKey("LAST_LOGIN")
    }

    override fun isFirstTimeLaunch(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.i(
                    DataStorePreferencesRepositoryImpl::class.simpleName,
                    "Error reading preferences.",
                    exception
                )
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.FIRST_TIME_LAUNCH] ?: true
        }

    override suspend fun updateFirstTimeLaunch(isFirstTimeLaunch: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_TIME_LAUNCH] = isFirstTimeLaunch
        }
    }

    override fun getLastLogin(): Flow<TaiKhoan?> = dataStore.data
        .map { preferences ->
            val json = preferences[LAST_LOGIN]
            try {
                json?.let { Gson().fromJson(it, TaiKhoan::class.java) }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun setLastLogin(taiKhoan: TaiKhoan) {
        val json = Gson().toJson(taiKhoan)
        dataStore.edit { preferences ->
            preferences[LAST_LOGIN] = json
        }
    }
}
