package com.example.coreyield.ml

import android.content.Context
import android.content.res.AssetFileDescriptor
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class PersonaClassifier(context: Context) {
    private var interpreter: Interpreter? = null

    init {
        try {
            val modelBuffer = loadModelFile(context, "persona_clustering.tflite")
            interpreter = Interpreter(modelBuffer)
        } catch (e: Exception) { e.printStackTrace() }
    }

    // Output: Cluster Index (0, 1, 2, or 3)
    fun predictPersona(prodScore: Float, savingScore: Float): Int {
        if (interpreter == null) return 0

        // Input: [ProdScore, SavingScore] (Scale 0.0 - 1.0)
        val inputs = Array(1) { floatArrayOf(prodScore, savingScore) }
        // Output: Probabilities for 4 Clusters
        val outputs = Array(1) { FloatArray(4) }

        interpreter?.run(inputs, outputs)

        // Find index with highest value (ArgMax)
        val probabilities = outputs[0]
        var maxIndex = 0
        var maxVal = probabilities[0]
        for (i in 1 until probabilities.size) {
            if (probabilities[i] > maxVal) {
                maxVal = probabilities[i]
                maxIndex = i
            }
        }
        return maxIndex
    }

    private fun loadModelFile(context: Context, fileName: String): ByteBuffer {
        val fd = context.assets.openFd(fileName)
        val stream = FileInputStream(fd.fileDescriptor)
        val channel = stream.channel
        return channel.map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength)
    }

    fun close() { interpreter?.close() }
}