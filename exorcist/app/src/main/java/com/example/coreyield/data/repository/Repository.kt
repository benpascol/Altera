package com.example.coreyield.data.repository

import com.example.coreyield.data.remote.FirebaseManager
import com.example.coreyield.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

object Repository {

    // --- VARIABEL UTAMA ---

    // Kita inisialisasi langsung dengan User ID dari Firebase (jika ada)
    // Menggunakan 'var' agar bisa di-reset manual jika perlu
    var currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

    // --- FUNGSI RESET DATA ---
    fun clearData() {
        currentUserId = null
        // Kita juga harus sign out dari Firebase agar sinkron
        FirebaseAuth.getInstance().signOut()
    }

    // --- 1. REGISTER FUNCTION ---
    fun registerUser(
        email: String,
        pass: String,
        userProfile: UserProfile,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        FirebaseManager.auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: ""

                    // Update variable lokal kita
                    currentUserId = userId

                    val finalProfile = userProfile.copy(uid = userId)
                    saveUserProfile(finalProfile) {
                        onSuccess()
                    }
                } else {
                    onFailure(task.exception?.message ?: "Registration Failed")
                }
            }
    }

    // --- 2. LOGIN FUNCTION ---
    fun loginUser(email: String, pass: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        FirebaseManager.auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update variable lokal kita saat berhasil login
                    currentUserId = task.result?.user?.uid
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Login Failed")
                }
            }
    }

    // --- 3. SAVE PROFILE FUNCTION ---
    private fun saveUserProfile(profile: UserProfile, onComplete: () -> Unit) {
        FirebaseManager.db.collection("users")
            .document(profile.uid)
            .set(profile)
            .addOnCompleteListener {
                onComplete()
            }
    }

    // --- 4. GET CURRENT USER (FIREBASE OBJECT) ---
    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    // --- 5. LOGOUT FUNCTION ---
    fun logout() {
        clearData() // Panggil fungsi clearData yang sudah kita buat di atas
    }

    // Alias fungsi logout (jika ada yang memanggil signOut)
    fun signOut() {
        clearData()
    }

    // --- NEW FEATURE: Check Account Status ---
    suspend fun checkUserAccountStatus(): Boolean {
        // Cek langsung ke Firebase Auth biar akurat
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return false
        return try {
            val document = FirebaseManager.db.collection("users").document(uid).get().await()
            if (document.exists()) {
                currentUserId = uid // Sinkronisasi ulang jika valid
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isUserValid(): Boolean {
        return checkUserAccountStatus()
    }

} // <--- PERHATIKAN: Kurung Tutup Akhir ada di SINI (Paling Bawah)