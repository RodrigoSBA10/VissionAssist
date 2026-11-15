package com.example.vissionassist.domain.model

// Clase que representa una imagen capturada por la camara
data class CapturedImage(
    val id: String, // Identificador unico de la imagen
    val imageData: ByteArray, // Datos de la imagen en formato ByteArray
    val timestamp: Long // Momento en que se capturo la imagen
)