package com.example.vissionassist.data.device.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TextToSpeechManager (
    context: Context
) : TextToSpeech.OnInitListener{
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    init {
        textToSpeech = TextToSpeech(
            context.applicationContext,
            this
        )
        Log.d("VissionAssistTTS", "Inicializando TextToSpeech")
    }

    override fun onInit(status: Int) {

        Log.d(
            "VisionAssistTTS",
            "Estado de inicialización: $status"
        )

        if (status == TextToSpeech.SUCCESS) {

            val languageResult = textToSpeech?.setLanguage(
                Locale.forLanguageTag("es-MX")
            )

            Log.d(
                "VisionAssistTTS",
                "Resultado del idioma es-MX: $languageResult"
            )

            isInitialized =
                languageResult != null &&
                        languageResult != TextToSpeech.LANG_MISSING_DATA &&
                        languageResult != TextToSpeech.LANG_NOT_SUPPORTED

            if (isInitialized) {
                Log.d(
                    "VisionAssistTTS",
                    "Motor de voz listo para reproducir"
                )
            } else {
                Log.e(
                    "VisionAssistTTS",
                    "El idioma español de México no está disponible"
                )
            }

        } else {

            isInitialized = false

            Log.e(
                "VisionAssistTTS",
                "Falló la inicialización del motor de voz"
            )
        }
    }


    fun speak(text: String) {

        if (text.isBlank()) {
            Log.w("VisionAssistTTS", "El texto está vacío")
            return
        }

        if (!isInitialized) {
            Log.e(
                "VisionAssistTTS",
                "No se puede reproducir: TextToSpeech no está inicializado"
            )
            return
        }

        val result = textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "VisionAssistSpeech"
        )

        Log.d(
            "VisionAssistTTS",
            "Resultado de speak(): $result"
        )
    }

    fun stop(){
        textToSpeech?.stop()
    }

    fun shutdown(){
        isInitialized = false
        textToSpeech?.shutdown()
        textToSpeech = null
    }

}