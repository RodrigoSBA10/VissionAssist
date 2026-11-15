package com.example.vissionassist.domain.model

data class ImageAnalysisResult(
    val description: String,
    val detectedObjects: List<String>,
    val warnings: List<String>
)
