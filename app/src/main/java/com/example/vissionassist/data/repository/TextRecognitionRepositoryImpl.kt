package com.example.vissionassist.data.repository

import android.util.Log
import com.example.vissionassist.data.mlkit.MLKitTextAnalyzer
import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.repository.TextRecognitionRepository

class TextRecognitionRepositoryImpl(
    private val textAnalyzer: MLKitTextAnalyzer
) : TextRecognitionRepository{

    override suspend fun recognizeText(
        image: CapturedImage
    ): String{
        val recognizedText = textAnalyzer.analyzeImage(image)
        Log.d("VissionAssist", "Imagen : ${image.id}")
        Log.d("VissionAssist", "Caracteres reconocido: ${recognizedText.length}")
        return recognizedText
    }
}