package com.example.vissionassist.data.mediapipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.vissionassist.domain.model.CapturedImage
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult

class EfficientDetImageAnalyzer(
    context: Context
): AutoCloseable{
    private val detector: ObjectDetector
    init {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("model.tflite")
            .build()
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setBaseOptions(baseOptions)
            .setScoreThreshold(0.5f)
            .setMaxResults(10)
            .build()
        detector = ObjectDetector.createFromOptions(
            context.applicationContext,
            options
        )
    }

    fun analyzeImage(image: CapturedImage): ObjectDetectorResult {
        val bitmap = BitmapFactory.decodeByteArray(
            image.imageData,
            0,
            image.imageData.size
        )?: throw IllegalArgumentException(
            "No se pudo decodificar la imagen"
        )
        val argbBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)?:
        throw IllegalArgumentException(
            "No se pudo preparar la fotografia"
        )
        val mpImage = BitmapImageBuilder(argbBitmap).build()
        return detector.detect(mpImage)
    }

    override fun close(){
        detector.close()
    }
}