package com.example.coreyield.util

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MLManager(private val context: Context) {

    private var interpreterStress: Interpreter? = null
    private var interpreterFinance: Interpreter? = null

    init {
        // 1. LOAD MODEL STRESS
        try {
            if (isFileExists("stress_predictor_calculated2.tflite")) {
                interpreterStress = Interpreter(loadModelFile("stress_predictor_calculated2.tflite"))
                Log.d("MLManager", "✅ Stress Model Loaded")
            } else {
                Log.e("MLManager", "❌ stress_predictor_calculated2.tflite not found")
            }
        } catch (e: Exception) {
            Log.e("MLManager", "Error loading Stress Model", e)
        }

        // 2. LOAD MODEL FINANCE
        try {
            if (isFileExists("expense_predictor_calculated2.tflite")) {
                interpreterFinance = Interpreter(loadModelFile("expense_predictor_calculated2.tflite"))
                Log.d("MLManager", "✅ Finance Model Loaded")
            } else {
                Log.w("MLManager", "⚠️ expense_predictor_calculated2.tflite not found. Using Rule-Based logic.")
            }
        } catch (e: Exception) {
            Log.e("MLManager", "Error loading Finance Model", e)
        }
    }

    private fun loadModelFile(modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun isFileExists(fileName: String): Boolean {
        return try {
            context.assets.list("")?.contains(fileName) == true
        } catch (e: Exception) {
            false
        }
    }

    // =========================================================================
    // 🧠 1. STRESS PREDICTOR LOGIC
    // =========================================================================
    fun predictStressScore(difficulty: Int, daysUntilDeadline: Float, taskCount: Int): Float {
        if (interpreterStress == null) return 0f

        val inputs = arrayOf(floatArrayOf(difficulty.toFloat(), daysUntilDeadline, taskCount.toFloat()))
        val output = Array(1) { FloatArray(1) }

        try {
            interpreterStress?.run(inputs, output)
            return output[0][0]
        } catch (e: Exception) {
            Log.e("MLManager", "Error predicting stress", e)
            return 50f
        }
    }

    // =========================================================================
    // 💰 2. FINANCE LOGIC (FIXED CRASH SHAPE [1,1])
    // =========================================================================

    /**
     * Menentukan Persona Keuangan.
     * FIX: Mengubah output buffer dari FloatArray(4) menjadi FloatArray(1)
     * karena model ternyata hanya mengembalikan 1 angka (Index/Score).
     */
    fun determinePersona(income: Float, expense: Float, savingsRate: Float): String {
        // A. COBA PAKAI AI
        if (interpreterFinance != null) {
            try {
                // Input: [Income, Expense, SavingsRate]
                val inputs = arrayOf(floatArrayOf(income, expense, savingsRate))

                // FIX CRASH DISINI: Ubah output shape ke [1][1] sesuai error log
                val output = Array(1) { FloatArray(1) }

                interpreterFinance?.run(inputs, output)

                // Kita asumsikan outputnya adalah Index Kelas (0.0, 1.0, 2.0, 3.0)
                val predictedIndex = output[0][0].toInt()

                return when (predictedIndex) {
                    0 -> "The Wealth Builder 💎"
                    1 -> "Smart Saver 🧠"
                    2 -> "Balanced Spender ⚖️"
                    3 -> "High Roller 💸"
                    else -> getManualPersona(savingsRate) // Fallback jika index aneh
                }
            } catch (e: Exception) {
                // Jika masih error, jangan crash, langsung ke manual
                Log.e("MLManager", "AI Persona Error, switching to manual", e)
                return getManualPersona(savingsRate)
            }
        }

        // B. FALLBACK MANUAL (Jika model null atau error)
        return getManualPersona(savingsRate)
    }

    // Logic Manual dipisah biar rapi dan bisa dipanggil saat catch error
    private fun getManualPersona(savingsRate: Float): String {
        return when {
            savingsRate >= 0.5f -> "The Wealth Builder 💎"
            savingsRate >= 0.2f -> "Smart Saver 🧠"
            savingsRate > 0f -> "Balanced Spender ⚖️"
            else -> "High Roller 💸"
        }
    }

    /**
     * Memprediksi Pengeluaran (Tetap Manual agar realistis)
     */
    fun predictExpense(income: Float, expense: Float, balance: Float): Float {
        // Tetap gunakan logika manual karena AI prediksinya terlalu tinggi (5jt++)
        if (expense <= 0) return 100000f
        return expense * 1.1f
    }
}