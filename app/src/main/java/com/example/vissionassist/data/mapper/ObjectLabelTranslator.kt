
package com.example.vissionassist.data.mapper

class ObjectLabelTranslator {

    private val translations = mapOf(
        "person" to "persona",
        "bicycle" to "bicicleta",
        "car" to "automóvil",
        "motorcycle" to "motocicleta",
        "bus" to "autobús",
        "truck" to "camión",
        "traffic light" to "semáforo",
        "stop sign" to "señal de alto",
        "bench" to "banca",
        "bird" to "pájaro",
        "cat" to "gato",
        "dog" to "perro",
        "backpack" to "mochila",
        "umbrella" to "paraguas",
        "handbag" to "bolso",
        "suitcase" to "maleta",
        "bottle" to "botella",
        "cup" to "taza",
        "fork" to "tenedor",
        "knife" to "cuchillo",
        "spoon" to "cuchara",
        "bowl" to "tazón",
        "banana" to "plátano",
        "apple" to "manzana",
        "sandwich" to "sándwich",
        "orange" to "naranja",
        "broccoli" to "brócoli",
        "carrot" to "zanahoria",
        "pizza" to "pizza",
        "cake" to "pastel",
        "chair" to "silla",
        "couch" to "sofá",
        "potted plant" to "planta en maceta",
        "bed" to "cama",
        "dining table" to "mesa de comedor",
        "toilet" to "inodoro",
        "tv" to "televisión",
        "laptop" to "computadora portátil",
        "mouse" to "ratón",
        "remote" to "control remoto",
        "keyboard" to "teclado",
        "cell phone" to "teléfono celular",
        "microwave" to "microondas",
        "oven" to "horno",
        "refrigerator" to "refrigerador",
        "book" to "libro",
        "clock" to "reloj",
        "vase" to "florero",
        "scissors" to "tijeras",
        "teddy bear" to "oso de peluche",
        "toothbrush" to "cepillo de dientes"
    )

    fun translate(label: String): String {
        return translations[label.lowercase()] ?: label
    }
}
