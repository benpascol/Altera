package com.example.coreyield.ml

import android.content.Context
import com.example.coreyield.data.model.Task
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.TimeUnit

class StressPredictor(context: Context) {
    private var interpreter: Interpreter? = null

    init {
        try {
            // Pastikan nama file ini SAMA PERSIS dengan yang ada di folder assets
            val model = loadModelFile(context, "stress_predictor_calculated2.tflite")
            interpreter = Interpreter(model)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Menghitung total skor stres dari daftar tugas.
     * Model ML menerima 3 Input: [Difficulty (1-5), Duration (Jam), DaysToDeadline (Hari)]
     */
    fun predictSmartStress(tasks: List<Task>): Float {
        if (interpreter == null) return 0f

        var totalStress = 0f
        val now = System.currentTimeMillis()

        for (task in tasks) {
            // 1. Hitung Sisa Hari (Deadline - Sekarang)
            val diffMillis = (task.deadlineTimestamp ?: 0L) - now
            // Konversi ke Hari (Float). Jika minus (telat), anggap 0 (sangat stres)
            // Jika deadline 0 (tidak diisi), anggap 7 hari (standar)
            var daysRemaining = if ((task.deadlineTimestamp ?: 0L) > 0) {
                 (diffMillis.toFloat() / (1000 * 60 * 60 * 24))
            } else {
                 7f
            }

            // Hindari nilai negatif ekstrem, minimal -1
            if (daysRemaining < 0) daysRemaining = 0f

            // 2. Siapkan Input AI [Difficulty, Duration, DaysRemaining]
            // Pastikan urutan ini SESUAI dengan saat Anda training di Colab!
            // Biasanya urutannya: [Difficulty, Duration, DaysRemaining]

            // Normalisasi (Gunakan Mean/Scale dari Colab untuk Stress Dataset)
            // Contoh dummy (Anda harus ganti angka ini sesuai Colab stress_dataset):
            val normDiff = (task.difficultyScore.toFloat() - 3.0f) / 1.5f
            val normDays = (daysRemaining - 5.0f) / 3.0f

            val input = floatArrayOf(normDiff, normDays)
            val output = Array(1) { FloatArray(1) }

            interpreter?.run(input, output)

            // 3. Akumulasi Skor
            val prediction = output[0][0]
            totalStress += prediction
        }

        return totalStress
    }

    fun close() {
        interpreter?.close()
    }
}