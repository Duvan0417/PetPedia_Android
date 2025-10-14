package com.example.primerproyecto.model

data class Request(
    val id: Int,
    val priority: String,
    val application_status: String,
    val adoption_id: Int?,
    val user_id: Int?,
    val trainer_id: Int? = null // ✅ Agregar este campo
)