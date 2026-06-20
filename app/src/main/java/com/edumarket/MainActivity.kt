package com.edumarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.edumarket.data.preferences.UserPreferences
import com.edumarket.navigation.AppNavigation
import com.edumarket.ui.theme.EduMarketTheme
import com.edumarket.ui.theme.LocalAppLanguage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferences = UserPreferences()
        enableEdgeToEdge()
        setContent {
            val language by userPreferences.language.collectAsState(initial = "en")
            
            CompositionLocalProvider(LocalAppLanguage provides language) {
                EduMarketTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}