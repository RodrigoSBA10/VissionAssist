package com.example.vissionassist.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.model.ImageAnalysisResult
import com.example.vissionassist.domain.usecase.AnalyzeImageUseCase
import com.example.vissionassist.domain.usecase.CapturedImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val capturedImageUseCase: CapturedImageUseCase,
    private val analyzeImageUseCase: AnalyzeImageUseCase
) : ViewModel(){
    private val _capturedImage = MutableStateFlow<CapturedImage?>(null)
    val capturedImage: StateFlow<CapturedImage?> = _capturedImage.asStateFlow()
    private val _analysisResult =
        MutableStateFlow<ImageAnalysisResult?>(null)

    val analysisResult: StateFlow<ImageAnalysisResult?> =
        _analysisResult.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)

    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()
    fun captureImage(){
        if (_isProcessing.value) return
        viewModelScope.launch {
            try {
                val image = capturedImageUseCase()
                _capturedImage.value = capturedImageUseCase()
                val result = analyzeImageUseCase(image)
                _analysisResult.value = result
            }catch (exception: Exception){
                _error.value = exception.message ?: "Error al capturar la imagen"
            }finally {
                _isProcessing.value = false
            }
        }
    }
}