package com.example.vissionassist.presentation.camera

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.model.ImageAnalysisResult
import com.example.vissionassist.domain.usecase.AnalyzeImageUseCase
import com.example.vissionassist.domain.usecase.CapturedImageUseCase
import com.example.vissionassist.domain.usecase.RecogTextUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val capturedImageUseCase: CapturedImageUseCase,
    private val analyzeImageUseCase: AnalyzeImageUseCase,
    private val recognizeTextUseCase: RecogTextUseCase
) : ViewModel(){
    private val _capturedImage = MutableStateFlow<CapturedImage?>(null)
    val capturedImage: StateFlow<CapturedImage?> = _capturedImage.asStateFlow()
    private val _analysisResult =
        MutableStateFlow<ImageAnalysisResult?>(null)

    val analysisResult: StateFlow<ImageAnalysisResult?> =
        _analysisResult.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _recognizedText = MutableStateFlow<String?>(null)
    val recognizedText: StateFlow<String?> = _recognizedText.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)

    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    fun captureImage() {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _error.value = null
            _recognizedText.value = null
            _analysisResult.value = null

            try {
                val image = capturedImageUseCase()

                _capturedImage.value = image

                val result = analyzeImageUseCase(image)

                _analysisResult.value = result

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                Log.e(
                    "VisionAssist",
                    "Error al capturar o analizar imagen",
                    exception
                )

                _error.value =
                    exception.message ?: "Error al capturar la imagen"

            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun recognizeText(){
        if (_isProcessing.value) return
        viewModelScope.launch {
            _analysisResult.value = null
            _isProcessing.value = true
            _error.value = null
            _recognizedText.value = null
            try {
                val image = capturedImageUseCase()
                _capturedImage.value = image
                val text = recognizeTextUseCase(image)
                _recognizedText.value = if (text.isBlank()) {
                    "No se identifico texto en la imagen"
                }else{
                    text
                }
            }catch (exception: Exception){
                if (exception is CancellationException){
                    throw exception
                }
                Log.e("VisionAssist", "Error al reconocer texto", exception)
                _error.value = "No fue posible reconocer el texto"
            }finally {
                _isProcessing.value = false
            }
        }
    }
}