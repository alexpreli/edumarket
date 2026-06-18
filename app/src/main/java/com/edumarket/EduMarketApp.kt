package com.edumarket

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class EduMarketApp : Application() {

    companion object {
        
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}
