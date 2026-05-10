package com.edumarket.data.preferences

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.edumarket.EduMarketApp
import com.edumarket.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences {

    companion object {
        private val KEY_LANGUAGE   = stringPreferencesKey("language")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    }

    private val context = EduMarketApp.appContext

    val language: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_LANGUAGE] ?: "en"
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_EMAIL]
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = lang
        }
    }

    suspend fun saveSession(email: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USER_EMAIL)
        }
    }
}
