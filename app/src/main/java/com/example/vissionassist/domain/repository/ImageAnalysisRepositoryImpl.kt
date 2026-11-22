
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



        val detectedObjects = result.detections().mapNotNull { detection ->

            val category = detection.categories()
                .maxByOrNull { it.score() }
                ?: return@mapNotNull null

            val label = category.categoryName()
                .takeIf { it.isNotBlank() }
                ?: return@mapNotNull null

            val translatedLabel = translator.translate(label)

            val confidence = category.score()

            Log.d(
                "VisionAssist",
                "Objeto: $translatedLabel | Confianza: %.2f%%"
                    .format(confidence * 100)
            )

            when {
                confidence >= 0.75f ->
                    translatedLabel

                confidence >= 0.4f ->
                    ": $translatedLabel"

                else ->
                    null
            }
        }


        val objectCount = result.detections().size
        // Generar una descripción según el resultado
        val description = when {
            objectCount == 0 -> {
                "No se identificaron objetos en la imagen."
            }

            detectedObjects.isEmpty() -> {
                "Se detectaron $objectCount objetos en la imagen. " +
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
