package com.example.vissionassist.domain.repository

import com.example.vissionassist.domain.model.CapturedImage

interface TextRecognitionRepository {

    suspend fun recognizeText(image: CapturedImage): String
}