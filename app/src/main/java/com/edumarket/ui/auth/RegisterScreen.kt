package com.edumarket.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edumarket.ui.theme.EduBlue
import com.edumarket.ui.theme.EduBlueDark
import com.edumarket.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var name     by rememberSaveable { mutableStateOf("") }
    var email    by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val authMessage by authViewModel.authMessage.collectAsStateWithLifecycle()
    val authSuccess by authViewModel.authSuccess.collectAsStateWithLifecycle()

    LaunchedEffect(authSuccess) {
        if (authSuccess) {
            authViewModel.clearMessage()
            onRegisterSuccess()
        }
    }

    val lang = com.edumarket.ui.theme.LocalAppLanguage.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(EduBlueDark, EduBlue)))
    ) {
        
        IconButton(
            onClick  = onNavigateBack,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint               = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text       = "📚 ${com.edumarket.ui.theme.AppStrings.appName(lang)}",
                fontSize   = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text  = com.edumarket.ui.theme.AppStrings.createAccount(lang),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(32.dp))

            Card(
                shape   = RoundedCornerShape(20.dp),
                colors  = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier            = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text       = com.edumarket.ui.theme.AppStrings.registerTitle(lang),
                        style      = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value         = name,
                        onValueChange = { name = it },
                        label         = { Text(com.edumarket.ui.theme.AppStrings.fullName(lang)) },
                        singleLine    = true,
                        modifier      = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value           = email,
                        onValueChange   = { email = it },
                        label           = { Text(com.edumarket.ui.theme.AppStrings.email(lang)) },
                        singleLine      = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier        = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value                = password,
                        onValueChange        = { password = it },
                        label                = { Text(com.edumarket.ui.theme.AppStrings.password(lang) + " (min. 8, upper, lower, num, special)") },
                        singleLine           = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier             = Modifier.fillMaxWidth()
                    )

                    if (authMessage != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text  = authMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick  = { authViewModel.register(name, email, password) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(com.edumarket.ui.theme.AppStrings.registerBtn(lang), fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(8.dp))

                    TextButton(onClick = {
                        authViewModel.clearMessage()
                        onNavigateBack()
                    }) {
                        Text(com.edumarket.ui.theme.AppStrings.backToLogin(lang))
                    }
                }
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, end = 24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            val isEnglish = lang == "en"
            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "RO",
                    color = if (!isEnglish) Color.White else Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )
                androidx.compose.material3.Switch(
                    checked = isEnglish,
                    onCheckedChange = { authViewModel.setLanguage(if (it) "en" else "ro") },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "EN",
                    color = if (isEnglish) Color.White else Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
