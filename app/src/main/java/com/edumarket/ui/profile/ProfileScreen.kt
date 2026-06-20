package com.edumarket.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edumarket.data.preferences.UserPreferences
import com.edumarket.viewmodel.AuthViewModel
import com.edumarket.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel = viewModel(),
    onLogout: () -> Unit,
    onNavigateToOrders: () -> Unit
) {
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()
    val userEmail by authViewModel.userEmail.collectAsStateWithLifecycle()
    val userName by authViewModel.userName.collectAsStateWithLifecycle()

    val isEnglish = state.language == "en"

    val lang = com.edumarket.ui.theme.LocalAppLanguage.current

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("👤 ${com.edumarket.ui.theme.AppStrings.profileTitle(lang)}", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor    = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            Card(
                shape     = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier         = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 36.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text       = com.edumarket.ui.theme.AppStrings.signedIn(lang),
                        style      = MaterialTheme.typography.labelMedium,
                        color      = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            
            Card(
                shape     = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = com.edumarket.ui.theme.AppStrings.selectLanguage(lang),
                        style      = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text  = if (isEnglish) "🇬🇧 ${com.edumarket.ui.theme.AppStrings.english(lang)}" else "🇷🇴 ${com.edumarket.ui.theme.AppStrings.romanian(lang)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text  = "RO",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (!isEnglish) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(0.4f)
                            )
                            Switch(
                                checked         = isEnglish,
                                onCheckedChange = { checked ->
                                    homeViewModel.setLanguage(if (checked) "en" else "ro")
                                },
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            Text(
                                text  = "EN",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isEnglish) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(0.4f)
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text  = com.edumarket.ui.theme.AppStrings.savedInDataStore(lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }

            
            Card(
                shape     = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = com.edumarket.ui.theme.AppStrings.contactInfo(lang),
                        style      = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    ContactRow(label = com.edumarket.ui.theme.AppStrings.contactName(lang),  value = userName ?: "Unknown User")
                    HorizontalDivider(Modifier.padding(vertical = 6.dp), color = Color(0x22000000))
                    ContactRow(label = com.edumarket.ui.theme.AppStrings.email(lang), value = userEmail ?: "Unknown Email")
                }
            }

            
            Card(
                shape     = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = com.edumarket.ui.theme.AppStrings.appInfoTitle(lang),
                        style      = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    ContactRow(label = com.edumarket.ui.theme.AppStrings.appNameLabel(lang), value = "EduMarket")
                    HorizontalDivider(Modifier.padding(vertical = 6.dp), color = Color(0x22000000))
                    ContactRow(label = com.edumarket.ui.theme.AppStrings.appVersion(lang),  value = com.edumarket.BuildConfig.VERSION_NAME)
                    HorizontalDivider(Modifier.padding(vertical = 6.dp), color = Color(0x22000000))
                    ContactRow(label = com.edumarket.ui.theme.AppStrings.appPlatform(lang), value = "Android / Kotlin + Compose")
                }
            }

            
            Button(
                onClick  = onNavigateToOrders,
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(com.edumarket.ui.theme.AppStrings.myOrders(lang), fontWeight = FontWeight.Bold)
            }

            Button(
                onClick  = {
                    authViewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(com.edumarket.ui.theme.AppStrings.logout(lang), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ContactRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(
            text       = value,
            style      = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}
