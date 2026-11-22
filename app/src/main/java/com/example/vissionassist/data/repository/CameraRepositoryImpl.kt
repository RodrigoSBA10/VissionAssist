package com.example.vissionassist.data.repository

import androidx.camera.core.ImageCapture
import com.example.vissionassist.domain.model.CapturedImage
import com.example.vissionassist.domain.repository.CameraRepository
import com.example.vissionassist.data.device.camera.PhoneCameraDataSource

// Implementacion del repositorio de la camara
class CameraRepositoryImpl (
    // Inyeccion de la fuente de datos de la camara
    private val phoneCameraDataSource: PhoneCameraDataSource ) : CameraRepository {

    // Implementacion del metodo de la interfaz
    override suspend fun captureImage(): CapturedImage {
        // Llamada al metodo de la fuente de datos de la camara
        return phoneCameraDataSource.captureImage()
    }

    fun updateImageCapture(newImageCapture: ImageCapture) {
        phoneCameraDataSource.updateImageCapture(newImageCapture)
    }
}