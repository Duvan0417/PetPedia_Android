package com.example.primerproyecto.data.model

data class Veterinaria(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val telefono: String,
    val horario: String,
    val servicios: String,
    val imagen: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null
)

data class VeterinariaResponse(
    val success: Boolean,
    val message: String,
    val data: List<Veterinaria>
)