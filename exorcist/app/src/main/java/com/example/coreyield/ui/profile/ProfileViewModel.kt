package com.example.coreyield.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coreyield.data.model.UserProfile
import com.example.coreyield.data.remote.FirebaseManager
import com.example.coreyield.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        val uid = Repository.currentUserId ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = FirebaseManager.db.collection("users").document(uid).get().await()
                if (snapshot.exists()) {
                    _userProfile.value = snapshot.toObject(UserProfile::class.java)
                } else {
                    val newUser = UserProfile(uid = uid)
                    FirebaseManager.db.collection("users").document(uid).set(newUser).await()
                    _userProfile.value = newUser
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Nama parameter disesuaikan dengan panggilan di UI
    fun updateProfile(
        fullName: String,
        monthlyIncome: Long,
        savingsTarget: Int,
        activeStartHour: Int,
        activeEndHour: Int
    ) {
        val uid = Repository.currentUserId ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updates = mapOf(
                    "fullName" to fullName,
                    "monthlyIncome" to monthlyIncome,
                    "savingsTargetPercent" to savingsTarget,
                    "activeStartHour" to activeStartHour,
                    "activeEndHour" to activeEndHour
                )

                FirebaseManager.db.collection("users").document(uid).update(updates).await()
                loadUserProfile()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}