
package com.example.vissionassist.data.repository

import android.util.Log
import com.example.vissionassist.data.mapper.ObjectLabelTranslator
import com.example.vissionassist.data.mediapipe.EfficientDetImageAnalyzer
import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.model.ImageAnalysisResult
import com.example.vissionassist.domain.repository.ImageAnalysisRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageAnalysisRepositoryImpl(
    private val imageAnalyzer: EfficientDetImageAnalyzer
) : ImageAnalysisRepository {
    private val translator = ObjectLabelTranslator()
    override suspend fun analyzeImage(
        image: CapturedImage
    ): ImageAnalysisResult = withContext(Dispatchers.Default){

        // Analizar la fotografía con ML Kit
        val result = imageAnalyzer.analyzeImage(image)

        val detectedObjects = result.detections().mapNotNull {
            detection ->
            val label = detection.categories()
                .maxByOrNull { category -> category.score() }
                ?.categoryName()
                ?.takeIf { it.isNotBlank() }
            label?.let { translator.translate(it) }
        }
        val objectCount = result.detections().size
        // Generar una descripción según el resultado
        val description = when {
            objectCount == 0 -> {
                "No se identificaron objetos en la imagen."
            }

            detectedObjects.isEmpty() -> {
                "Se detectaron $objectCount posibles objetos en la imagen. " +
                        "pero no fue posible identificarlos"
            }

            else -> {
                "Se detectaron $objectCount posibles objetos en la imagen. " +
                        "Objetos reconocidos: " +
                        detectedObjects.joinToString(", ")
            }
        }

        Log.d(
            "VisionAssist",
            "Análisis completo para imagen ${image.id}. " +
                    "Objetos: $objectCount, Categorías: $detectedObjects"
        )

        ImageAnalysisResult(
            description = description,
            detectedObjects = detectedObjects,
            warnings = emptyList()
        )
    }
}
