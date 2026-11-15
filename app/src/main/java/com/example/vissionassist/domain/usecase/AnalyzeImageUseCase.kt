package com.example.vissionassist.domain.usecase

import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.model.ImageAnalysisResult
import com.example.vissionassist.domain.repository.ImageAnalysisRepository

class AnalyzeImageUseCase(
    private val imageAnalysisRepository: ImageAnalysisRepository
) {
    suspend operator fun invoke(
        image: CapturedImage
    ): ImageAnalysisResult {
        return imageAnalysisRepository.analyzeImage(image)
    }
}