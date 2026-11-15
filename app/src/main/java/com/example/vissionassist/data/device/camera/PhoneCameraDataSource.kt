package com.example.vissionassist.data.device.camera

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.example.vissionassist.domain.model.CapturedImage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.util.UUID
import java.util.concurrent.Executor
import kotlin.coroutines.resumeWithException

// Fuente de datos de la camara
class PhoneCameraDataSource(
    // Inyeccion de la clase ImageCapture
    private val imageCapture: ImageCapture,
    // Inyeccion de la clase Executor
    private val outputDirectory: File,
    // Inyeccion de la clase Executor
    private val executor: Executor
){
    // Metodo de la interfaz CameraDataSource
    suspend fun captureImage(): CapturedImage =
        suspendCancellableCoroutine { continuation ->
            // Creacion del archivo de la imagen
            val imageId = UUID.randomUUID().toString()
            // Creacion del archivo de la imagen
            val photoFile = File(
                outputDirectory,
                "$imageId.jpg"
            )
            // Creacion de las opciones de la imagen
            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(photoFile)
                .build()
            // Captura de la imagen
            imageCapture.takePicture(
                outputOptions,
                executor,

                object : ImageCapture.OnImageSavedCallback {
                    // Metodo de la interfaz ImageCapture.OnImageSavedCallback
                    override fun onImageSaved(
                        outputFileResults: ImageCapture.OutputFileResults
                    ) {
                        try {
                            // Lectura del archivo de la imagen
                            val imageBytes = photoFile.readBytes()
                            // Creacion del objeto CapturedImage
                            val capturedImage = CapturedImage(
                                id = imageId,
                                imageData = imageBytes,
                                timestamp = System.currentTimeMillis()
                            )

                            if (continuation.isActive) {
                                continuation.resume(capturedImage) { _, _, _ -> }
                            }

                        } catch (exception: Exception) {
                            if (continuation.isActive) {
                                continuation.resumeWithException(exception)
                            }
                        } finally {
                            photoFile.delete()
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        photoFile.delete()

                        if (continuation.isActive) {
                            continuation.resumeWithException(exception)
                        }
                    }
                }
            )
        }
}