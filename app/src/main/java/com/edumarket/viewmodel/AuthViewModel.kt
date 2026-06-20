package com.edumarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edumarket.data.local.AppDatabase
import com.edumarket.data.local.entity.UserEntity
import com.edumarket.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class AuthViewModel : ViewModel() {

    private val userDao = AppDatabase.getInstance().userDao()
    private val userPreferences = UserPreferences()

    

    private val _authMessage = MutableStateFlow<String?>(null)
    val authMessage: StateFlow<String?> = _authMessage

    private val _authSuccess = MutableStateFlow(false)
    val authSuccess: StateFlow<Boolean> = _authSuccess

    

    val isLoggedIn: StateFlow<Boolean?> = userPreferences.userEmail
        .map { email -> email != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    

    fun login(email: String, password: String) {
        val trimmedEmail = email.trim()
        val trimmedPass  = password.trim()

        if (trimmedEmail.isEmpty() || trimmedPass.isEmpty()) {
            _authMessage.value = "Please fill in all fields."
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _authMessage.value = "Please enter a valid email address."
            return
        }

        viewModelScope.launch {
            val user = userDao.findByEmail(trimmedEmail)
            when {
                user == null -> _authMessage.value = "Account not found. Please register first."
                !verifyPassword(trimmedPass, user.passwordHash) ->
                    _authMessage.value = "Incorrect password. Please try again."
                else -> {
                    userPreferences.saveSession(trimmedEmail)
                    _authMessage.value = null
                    _authSuccess.value = true
                }
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.endsWith(" ")) {
            _authMessage.value = "Full Name cannot end with a space."
            return
        }
        
        val trimmedName  = name.trim()
        val trimmedEmail = email.trim()
        val trimmedPass  = password.trim()

        if (trimmedName.isEmpty() || trimmedEmail.isEmpty() || trimmedPass.isEmpty()) {
            _authMessage.value = "Please fill in all fields."
            return
        }
        
        val namePattern = "^[\\p{L} ]{3,}\$"
        if (!trimmedName.matches(namePattern.toRegex())) {
            _authMessage.value = "Full Name must be at least 3 characters and contain only letters and spaces."
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _authMessage.value = "Please enter a valid email address."
            return
        }
        
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.*_\\-]).{8,}$"
        if (!trimmedPass.matches(passwordPattern.toRegex())) {
            _authMessage.value = "Password must be at least 8 chars with 1 uppercase, 1 lowercase, 1 number, 1 special character."
            return
        }

        viewModelScope.launch {
            val exists = userDao.countByEmail(trimmedEmail) > 0
            if (exists) {
                _authMessage.value = "An account with this email already exists."
                return@launch
            }
            userDao.insert(
                UserEntity(
                    name         = trimmedName,
                    email        = trimmedEmail,
                    passwordHash = hashPassword(trimmedPass)
                )
            )
            userPreferences.saveSession(trimmedEmail)
            _authMessage.value = null
            _authSuccess.value = true
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearSession()
            _authSuccess.value = false
        }
    }

    fun clearMessage() {
        _authMessage.value = null
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch { userPreferences.setLanguage(lang) }
    }

    

    private fun hashPassword(password: String): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        val saltHex = salt.joinToString("") { "%02x".format(it) }
        val hashHex = hash.joinToString("") { "%02x".format(it) }
        return "$saltHex:$hashHex"
    }

    private fun verifyPassword(password: String, storedHash: String): Boolean {
        
        if (!storedHash.contains(":")) {
            val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            val oldHashHex = bytes.joinToString("") { "%02x".format(it) }
            return oldHashHex == storedHash
        }
        
        val parts = storedHash.split(":")
        if (parts.size != 2) return false
        val saltHex = parts[0]
        val hashHex = parts[1]
        
        return try {
            val salt = saltHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
            val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val testHash = factory.generateSecret(spec).encoded
            val testHashHex = testHash.joinToString("") { "%02x".format(it) }
            hashHex == testHashHex
        } catch (e: Exception) {
            false
        }
    }
}
