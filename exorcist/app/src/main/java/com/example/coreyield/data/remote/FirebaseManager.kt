package com.example.coreyield.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Singleton untuk menginisialisasi dan menyediakan akses ke service Firebase
 */
object FirebaseManager {

    // Akses ke Authentication Service
    // Digunakan untuk Login/Register
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // Akses ke Firestore Database
    // Digunakan untuk menyimpan Task dan Transaction
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}