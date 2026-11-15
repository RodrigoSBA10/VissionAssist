package com.example.vissionassist.domain.usecase

import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.repository.CameraRepository

// Caso de uso para capturar una imagen desde la camara
class CapturedImageUseCase(private val cameraRepository: CameraRepository) {
    // Operador invocable para ejecutar el caso de uso
    suspend operator fun invoke(): CapturedImage {
        // Llamada al metodo captureImage del repositorio de la camara
        return cameraRepository.captureImage()
    }
}
