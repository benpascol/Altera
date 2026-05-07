package com.example.coreyield.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coreyield.data.repository.Repository // Pastikan Repository ada
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // State untuk Loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // State untuk Pesan Error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // State Status Login (Berhasil/Gagal)
    private val _loginState = MutableStateFlow(false)
    val loginState: StateFlow<Boolean> = _loginState

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Email and Password cannot be empty."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    // Simpan UID ke Repository global agar bisa dipakai di Home/Task
                    Repository.currentUserId = auth.currentUser?.uid
                    _loginState.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Login failed. Please check your credentials."
                }
            }
    }

    // Fungsi Register (Opsional, jika nanti dibutuhkan di RegisterScreen)
    fun register(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Email and Password cannot be empty."
            return
        }

        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    Repository.currentUserId = auth.currentUser?.uid
                    _loginState.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Registration failed."
                }
            }
    }
}