package com.example.vissionassist.presentation.camera

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vissionassist.data.device.camera.CameraManager
import com.example.vissionassist.data.device.camera.PhoneCameraDataSource
import com.example.vissionassist.data.mediapipe.EfficientDetImageAnalyzer
import com.example.vissionassist.data.mlkit.MLKitImageAnalyzer
import com.example.vissionassist.data.repository.CameraRepositoryImpl
import com.example.vissionassist.data.repository.ImageAnalysisRepositoryImpl
import com.example.vissionassist.domain.usecase.AnalyzeImageUseCase
import com.example.vissionassist.domain.usecase.CapturedImageUseCase
import com.example.vissionassist.data.device.speech.TextToSpeechManager
import com.example.vissionassist.data.mlkit.MLKitTextAnalyzer
import com.example.vissionassist.data.repository.TextRecognitionRepositoryImpl
import com.example.vissionassist.domain.usecase.RecogTextUseCase

// Vista de la camara
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val textSpeechManager = remember(context) {
        TextToSpeechManager(context)
    }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    val cameraManager = remember(context) {
        CameraManager(context)
    }

    val repositoryHolder: CameraRepositoryHolder = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>
            ) : T {
                return CameraRepositoryHolder(
                    CameraRepositoryImpl(
                        phoneCameraDataSource = PhoneCameraDataSource(
                            imageCapture = cameraManager.imageCapture,
                            outputDirectory = context.filesDir,
                            executor = ContextCompat.getMainExecutor(context)
                        )
                    )
                ) as T
            }
        }
    )

    val cameraRepository = repositoryHolder.repository

    LaunchedEffect(cameraManager, cameraRepository) {
        cameraRepository.updateImageCapture(
            cameraManager.imageCapture
        )
    }

    val capturedImageUseCase = remember(cameraRepository) {
        CapturedImageUseCase(cameraRepository)
    }
    val efficientDetImageAnalyzer = remember(context) {
        EfficientDetImageAnalyzer(context)
    }

    val textAnalyzerHolder : TextAnalyzerHolder = viewModel()

    val mlKitTextAnalyzer = textAnalyzerHolder.analyzer

    val textRecognitionRepository = remember(mlKitTextAnalyzer) {
        TextRecognitionRepositoryImpl(
            mlKitTextAnalyzer
        )
    }

    val recognizeTextUseCase = remember(textRecognitionRepository) {
        RecogTextUseCase(textRecognitionRepository)
    }

    val imageAnalysisRepository = remember(efficientDetImageAnalyzer/*mlKitImageAnalyzer*/) {
        ImageAnalysisRepositoryImpl(
            //mlKitImageAnalyzer = mlKitImageAnalyzer
            efficientDetImageAnalyzer
        )
    }

    val analyzeImageUseCase = remember(imageAnalysisRepository) {
        AnalyzeImageUseCase(imageAnalysisRepository)
    }

    val cameraViewModel: CameraViewModel = viewModel(
        factory = CameraViewModelFactory(capturedImageUseCase,
            analyzeImageUseCase,
            recognizeTextUseCase)
    )

    val capturedImage by cameraViewModel.capturedImage.collectAsState()
    val captureError by cameraViewModel.error.collectAsState()
    val analysisResult by cameraViewModel.analysisResult.collectAsState()
    val isProcessing by cameraViewModel.isProcessing.collectAsState()
    val isCameraReady by cameraManager.isCameraReady.collectAsState()
    val recognizedText by cameraViewModel.recognizedText.collectAsState()
    LaunchedEffect(analysisResult) {
        analysisResult?.let { result ->
            Log.d("Launch", "Descripcion: ${result.description}")
            Log.d("Launch", "Objetos detectados: ${result.detectedObjects}")
            Log.d("Launch", "Advertencias: ${result.warnings}")
            textSpeechManager.speak(
                result.description
            )
        }
    }
    LaunchedEffect(recognizedText) {
        recognizedText?.let { text ->
            Log.d("VisionAssist", "Reproduciento Texto reconocido: $text")
            textSpeechManager.speak(text)
        }
    }
    LaunchedEffect(capturedImage) {
        capturedImage?.let { image ->
            Log.d("Launch", "Imagen capturada correctamente. " +
                    "ID: ${image.id}, " + "Tamaño: ${image.imageData.size}")
        }
    }

    LaunchedEffect(captureError) {
        captureError?.let { error ->
            Log.e("Launch", "Error al capturar imagen: $error")
        }
    }

    val previewView = remember(context) {
        PreviewView(context)
    }
    DisposableEffect(textSpeechManager) {
        onDispose {
            textSpeechManager.shutdown()
        }
    }
    DisposableEffect(
        hasCameraPermission,
        lifecycleOwner,
        cameraManager,
        previewView
    ) {
        if (hasCameraPermission) {
            cameraManager.startCamera(
                lifecycleOwner = lifecycleOwner,
                previewView = previewView
            )
        }

        onDispose {
            // CameraX administra la vinculación con el LifecycleOwner.
        }
    }

    if (hasCameraPermission) {


        Box(
            modifier = modifier.fillMaxSize()
        ) {

            // Vista previa de la cámara
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )

            // Panel de resultados del análisis
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {

                Text(
                    text = "Análisis del entorno",
                    color = Color(0xFF8BC34A),
                    style = MaterialTheme.typography.titleMedium
                )

                // Indicador de procesamiento
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 12.dp),
                        color = Color(0xFF8BC34A)
                    )
                }

                // Resultado de Google ML Kit
                analysisResult?.let { result ->
                    Text(
                        text = result.description,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                recognizedText?.let { text ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Texto roeconocido:",
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = text)
                    }
                }
                // Mensaje de error
                captureError?.let { error ->
                    Text(
                        text = error,
                        color = Color(0xFFFF8A80),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }


            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Botón para reconocer objetos
                Button(
                    onClick = {
                        if (isCameraReady && !isProcessing) {
                            cameraViewModel.captureImage()
                        }
                    },
                    enabled = isCameraReady && !isProcessing
                ) {
                    Text(
                        text = when {
                            !isCameraReady -> "Iniciando cámara..."
                            isProcessing -> "Analizando..."
                            else -> "Reconocer objetos"
                        }
                    )
                }

                // Botón para reconocer texto mediante OCR
                Button(
                    onClick = {
                        if (isCameraReady && !isProcessing) {
                            cameraViewModel.recognizeText()
                        }
                    },
                    enabled = isCameraReady && !isProcessing
                ) {
                    Text(
                        text = when {
                            !isCameraReady -> "Iniciando cámara..."
                            isProcessing -> "Analizando..."
                            else -> "Leer texto"
                        }
                    )
                }
            }
        }
    } else {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "VisionAssist necesita acceso a la cámara"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    permissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            ) {
                Text("Permitir cámara")
            }
        }
    }
}
class CameraRepositoryHolder(
    val repository: CameraRepositoryImpl
) : androidx.lifecycle.ViewModel()

class TextAnalyzerHolder : ViewModel(){
    val analyzer = MLKitTextAnalyzer()
    override fun onCleared() {
        analyzer.close()
        super.onCleared()
    }
}