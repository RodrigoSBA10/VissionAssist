package com.example.vissionassist.domain.usecase

import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.repository.TextRecognitionRepository

class RecogTextUseCase(
    private val repository: TextRecognitionRepository
){
    suspend operator fun invoke(
        image: CapturedImage
    ): String{
        return repository.recognizeText(image)
    }
}