package com.example.vissionassist.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vissionassist.domain.usecase.AnalyzeImageUseCase
import com.example.vissionassist.domain.usecase.CapturedImageUseCase
import com.example.vissionassist.domain.usecase.RecogTextUseCase

class CameraViewModelFactory(
    private val capturedImageUseCase: CapturedImageUseCase,
    private val analyzeImageUseCase: AnalyzeImageUseCase,
    private val recognizeTextUseCase: RecogTextUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CameraViewModel(capturedImageUseCase, analyzeImageUseCase,
                recognizeTextUseCase = recognizeTextUseCase) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}