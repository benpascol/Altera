package com.example.coreyield.ml

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ExpensePredictor(context: Context) {
    private var interpreter: Interpreter? = null

    // --- PASTE ANGKA DARI COLAB DI SINI ---
    // Mean: [Hari, Pengeluaran, Saldo]
    private val meanValues = floatArrayOf(15.52625f, 1387022.0f, 2523903.4f)

    // Scale: [Hari, Pengeluaran, Saldo]
    private val scaleValues = floatArrayOf(8.555513f, 960809.4f, 1389111.6f)
    // --------------------------------------

    init {
        try {
            // Pastikan nama file .tflite sesuai dengan yang ada di folder assets
            // Jika di assets namanya 'expense_predictor_hybrid.tflite', GANTI string di bawah ini:
            val model = loadModelFile(context, "expense_predictor_calculated2.tflite")
            interpreter = Interpreter(model)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun predictEndOfMonth(day: Float, currentExpense: Float, currentBalance: Float): Float {
        if (interpreter == null) return 0f

        // 1. Normalisasi (Standard Scaler)
        // Rumus: (Value - Mean) / Scale
        val normDay = (day - meanValues[0]) / scaleValues[0]
        val normExpense = (currentExpense - meanValues[1]) / scaleValues[1]
        val normBalance = (currentBalance - meanValues[2]) / scaleValues[2]

        val input = floatArrayOf(normDay, normExpense, normBalance)
        val output = Array(1) { FloatArray(1) }

        // 2. Inference
        interpreter?.run(input, output)

        // 3. Denormalisasi (Kembalikan ke Rupiah)
        // Di Python: y_scaled = y_asli / 1.000.000
        // Jadi sekarang: y_asli = y_scaled * 1.000.000
        val rawPrediction = output[0][0]
        val rupiahPrediction = rawPrediction * 1000000f

        return rupiahPrediction
    }

    private fun loadModelFile(context: Context, fileName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(fileName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun close() {
        interpreter?.close()
    }
}