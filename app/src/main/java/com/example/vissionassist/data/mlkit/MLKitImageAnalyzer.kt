package com.example.vissionassist.data.mlkit

import android.graphics.BitmapFactory
import android.util.Log
import com.example.vissionassist.domain.model.CapturedImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class MLKitImageAnalyzer{
    suspend fun analyzeImage(image: CapturedImage): MLKitDetectionResult =
        suspendCancellableCoroutine{ continuation ->
            // Convertir los bytes de la imagen en un objeto Bitmap
            val bitmap = BitmapFactory.decodeByteArray(
                image.imageData,
                0,
                image.imageData.size
            )
            // Crear un objeto InputImage a partir del Bitmap
            val inputImage = InputImage.fromBitmap(bitmap,0)
            // Crear un objeto ObjectDetectorOptions con el modo de detección
            val options = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build()
            val detector = ObjectDetection.getClient(options)
            // Procesar la imagen con el detector de objetos
            detector.process(inputImage)
                .addOnSuccessListener { detectedObjects ->
                val labels = detectedObjects.flatMap { detectedObjects ->
                    detectedObjects.labels.map { label ->
                        label.text
                    }
                }
                    val result = MLKitDetectionResult(
                        objectCount = detectedObjects.size,
                        labels = labels
                    )
                    if (continuation.isActive) {
                        continuation.resume(result, null)
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resumeWithException(exception)
                    }
                }
                .addOnCompleteListener {
                    detector.close()
                }

    }
}