package com.example.vissionassist.domain.repository

import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.model.ImageAnalysisResult

interface ImageAnalysisRepository {

    suspend fun analyzeImage(
        image: CapturedImage
    ): ImageAnalysisResult
}