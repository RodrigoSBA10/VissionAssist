package com.example.vissionassist.data.mlkit

import android.graphics.BitmapFactory
import android.util.Log
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import com.example.vissionassist.domain.model.CapturedImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayInputStream
import kotlin.coroutines.resumeWithException

class MLKitTextAnalyzer : AutoCloseable{

    private val recognizer = TextRecognition.getClient(
        TextRecognizerOptions.DEFAULT_OPTIONS
    )

    suspend fun analyzeImage(image: CapturedImage): String {
        val bitmap = correctImageOrientation(image.imageData)
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        return suspendCancellableCoroutine { continuation ->
            recognizer.process(inputImage)
                .addOnSuccessListener{ result ->
                    val recognizedText = result.text
                    Log.d("VisionAssist", "Texto reconocido: $recognizedText")
                    if (continuation.isActive){
                        continuation.resume(recognizedText, null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("VissionAssit", "Error al reconocer texto",
                        exception)
                    if (continuation.isActive){
                        continuation.resumeWithException(exception)
                    }
                }
        }
    }

    private fun correctImageOrientation(imageData: ByteArray): android.graphics.Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(
            imageData,
            0,
            imageData.size
        )?: throw IllegalArgumentException(
            "No se pudo decodificar la imagen"
        )
        val exif = ExifInterface(
            ByteArrayInputStream(imageData)
        )
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        val matrix = Matrix()
        when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.postRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.postRotate(270f)
                matrix.postScale(-1f, 1f)
            }
        }
        return if (matrix.isIdentity){
            bitmap
        }else{
            val correctedBitmap = android.graphics.Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
            if (correctedBitmap != bitmap){
                bitmap.recycle()
            }
            correctedBitmap
        }
    }

    override fun close() {
        recognizer.close()
    }
}