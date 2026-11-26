# VisionAssist

**Aplicación móvil de asistencia para personas con discapacidad visual mediante inteligencia artificial y visión artificial.**

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![Status](https://img.shields.io/badge/Estado-En%20desarrollo-orange)

## Descripción

VisionAssist es un proyecto académico orientado al desarrollo de una herramienta tecnológica de asistencia para personas con discapacidad visual.

La aplicación utiliza la cámara del dispositivo móvil para adquirir información visual, procesarla mediante modelos de inteligencia artificial y comunicar los resultados al usuario mediante síntesis de voz.

El proyecto busca favorecer la autonomía de los usuarios y facilitar el acceso a información visual y materiales educativos.

Actualmente, el sistema se encuentra en una etapa de desarrollo de prototipo funcional para Android.

## Objetivo

Desarrollar una aplicación móvil que permita interpretar información visual del entorno mediante técnicas de visión artificial y reconocimiento óptico de caracteres, proporcionando retroalimentación auditiva accesible.

## Funcionalidades implementadas

- Captura de imágenes mediante la cámara del dispositivo.
- Visualización de la cámara en tiempo real.
- Detección de objetos mediante inteligencia artificial.
- Traducción de categorías de objetos al español.
- Generación de descripciones básicas.
- Reconocimiento óptico de caracteres (OCR).
- Reproducción de resultados mediante síntesis de voz.
- Corrección de orientación de imágenes.
- Procesamiento local de imágenes.
- Gestión de estados y errores.
- Gestión del ciclo de vida de la cámara.

## Tecnologías utilizadas

| Tecnología | Propósito |
|---|---|
| Kotlin | Lenguaje de programación principal. |
| Android SDK | Desarrollo nativo para Android. |
| Jetpack Compose | Desarrollo declarativo de interfaces. |
| Material Design 3 | Diseño de componentes gráficos. |
| CameraX | Captura y visualización de imágenes. |
| MediaPipe Tasks Vision | Ejecución del modelo de detección de objetos. |
| EfficientDet-Lite0 | Modelo de inteligencia artificial para detección de objetos. |
| Google ML Kit | Reconocimiento óptico de caracteres. |
| TextToSpeech | Conversión de texto a voz. |
| Kotlin Coroutines | Ejecución de operaciones asíncronas. |
| StateFlow | Gestión reactiva de estados. |
| ViewModel | Administración del estado de la interfaz. |
| ExifInterface | Lectura de metadatos y orientación de imágenes. |
| Gradle | Automatización de compilación y gestión de dependencias. |

## Arquitectura

El proyecto utiliza **Clean Architecture**, complementada con el patrón **MVVM (Model-View-ViewModel)** en la capa de presentación.

Esta arquitectura permite separar responsabilidades, reducir el acoplamiento entre componentes y facilitar el mantenimiento, las pruebas y la incorporación de nuevas funcionalidades.

### Domain

Contiene la lógica del dominio y las abstracciones principales del sistema.

- Entidades.
- Interfaces de repositorios.
- Casos de uso.

### Data

Contiene las implementaciones concretas y los mecanismos de acceso a recursos externos.

- Implementaciones de repositorios.
- Fuentes de datos.
- Captura de imágenes.
- Detección de objetos.
- Reconocimiento de texto.
- Procesamiento de imágenes.

### Presentation

Contiene los componentes responsables de la interacción con el usuario.

- Pantallas desarrolladas con Jetpack Compose.
- ViewModels.
- Estados de interfaz.
- Gestión de eventos.
- Reproducción de resultados.

## Funcionamiento

El flujo general de procesamiento comprende las siguientes etapas:

1. El usuario selecciona una funcionalidad.
2. CameraX captura una imagen.
3. El ViewModel ejecuta el caso de uso correspondiente.
4. El repositorio delega el procesamiento al analizador.
5. MediaPipe o Google ML Kit procesa la imagen.
6. El resultado se devuelve al ViewModel.
7. La interfaz actualiza su estado.
8. TextToSpeech reproduce el resultado.

### Detección de objetos

La detección de objetos se realiza mediante MediaPipe Tasks Vision y el modelo EfficientDet-Lite0.

El modelo se ejecuta localmente y permite identificar objetos pertenecientes a las categorías del conjunto de datos COCO.

| Parámetro | Configuración |
|---|---|
| Modelo | EfficientDet-Lite0 |
| Formato | TensorFlow Lite (.tflite) |
| Categorías | 80 |
| Umbral de confianza | 0.4 |
| Máximo de detecciones | 10 |
| Procesamiento | Local |

### Reconocimiento de texto

El reconocimiento óptico de caracteres utiliza Google ML Kit Text Recognition v2 para identificar información textual presente en imágenes de documentos, libros y materiales educativos.

Antes del reconocimiento, se verifica la orientación de la imagen mediante ExifInterface para aplicar las transformaciones necesarias.

### Síntesis de voz

Los resultados obtenidos se reproducen mediante la API TextToSpeech de Android, configurada para utilizar el idioma español.

## Requisitos de instalación

- Android Studio.
- Android SDK.
- JDK compatible con la configuración de Gradle del proyecto.
- Dispositivo Android con cámara.
- Android 7.0 (API 24) o superior.
- Permisos de acceso a la cámara.

## Instalación y ejecución

1. Clonar este repositorio.
2. Abrir el proyecto mediante Android Studio.
3. Sincronizar las dependencias de Gradle.
4. Verificar la existencia del archivo `model.tflite` dentro de `app/src/main/assets/`.
5. Conectar un dispositivo Android o configurar un emulador compatible.
6. Compilar y ejecutar la aplicación.
7. Conceder los permisos de acceso a la cámara.

Se recomienda utilizar un dispositivo físico para evaluar las funcionalidades de reconocimiento visual.

## Roadmap

### Funcionalidades actuales

- [x] Arquitectura base del software.
- [x] Captura de imágenes.
- [x] Detección de objetos.
- [x] Traducción de etiquetas.
- [x] Reconocimiento óptico de caracteres.
- [x] Síntesis de voz.
- [x] Corrección de orientación.
- [x] Gestión reactiva de estados.

### Funcionalidades futuras

- [ ] Almacenamiento local mediante Room y SQLite.
- [ ] Administración de documentos reconocidos.
- [ ] Inyección de dependencias mediante Hilt.
- [ ] Reconocimiento de comandos de voz.
- [ ] Mejoras de accesibilidad y compatibilidad con TalkBack.
- [ ] Procesamiento continuo de imágenes.
- [ ] Interpretación contextual mediante modelos multimodales.
- [ ] Integración con lentes inteligentes.
- [ ] Exportación de documentos para impresión en Braille.
- [ ] Pruebas automatizadas.
- [ ] Evaluación de precisión y rendimiento.
- [ ] Evaluación de usabilidad.

## Limitaciones actuales

- La detección está limitada a las categorías reconocidas por el modelo.
- El reconocimiento depende de la calidad de las imágenes.
- No se realiza una interpretación contextual completa del entorno.
- No se dispone de estimación precisa de distancias.
- La captura de imágenes se realiza bajo demanda.
- La integración con lentes inteligentes se encuentra pendiente.
- El prototipo todavía requiere evaluaciones de accesibilidad con usuarios.

## Privacidad

El procesamiento de imágenes se realiza localmente en el dispositivo, sin requerir el envío de fotografías a servidores externos de inteligencia artificial.

Las imágenes capturadas se almacenan temporalmente durante el procesamiento y los archivos temporales se eliminan posteriormente.

## Documentación de las tecnologías

- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Kotlin](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/compose)
- [CameraX](https://developer.android.com/media/camera/camerax)
- [MediaPipe Tasks Vision](https://ai.google.dev/edge/mediapipe/solutions/vision/object_detector)
- [EfficientDet](https://arxiv.org/abs/1911.09070)
- [Google ML Kit](https://developers.google.com/ml-kit/vision/text-recognition/v2)
- [TextToSpeech](https://developer.android.com/reference/android/speech/tts/TextToSpeech)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html)
- [Room](https://developer.android.com/training/data-storage/room)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

## Estado del proyecto

**Prototipo funcional en desarrollo.**

VisionAssist es un proyecto académico y experimental. No pretende sustituir los dispositivos de asistencia, mecanismos de orientación o herramientas de movilidad utilizados por personas con discapacidad visual.

---

**VisionAssist — Tecnología al servicio de la accesibilidad.**
