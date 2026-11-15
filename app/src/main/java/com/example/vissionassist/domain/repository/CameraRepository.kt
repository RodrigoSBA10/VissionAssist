package com.example.vissionassist.domain.repository

import com.example.vissionassist.domain.model.CapturedImage

// Interfaz que define el repositorio de la camara
interface CameraRepository {
    // Metodo que captura una imagen y la devuelve como un ByteArray
    suspend fun captureImage(): CapturedImage

}