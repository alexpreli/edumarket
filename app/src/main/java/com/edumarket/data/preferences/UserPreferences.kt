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
        private val KEY_USER_NAME  = stringPreferencesKey("user_name")
    }

    private val context = EduMarketApp.appContext

    val language: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_LANGUAGE] ?: "en"
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_EMAIL]
    }

    val userName: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_NAME]
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = lang
        }
    }

    suspend fun saveSession(email: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_EMAIL] = email
            prefs[KEY_USER_NAME]  = name
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USER_EMAIL)
            prefs.remove(KEY_USER_NAME)
        }
    }
}
