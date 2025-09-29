package com.example.primerproyecto.data.model

data class Trainer (
    val id: Int,
    val name: String,
    val specialty: String,
    val experience: Int,
    val qualifications: String,
    val phone: String,
    val email: String,
    val biography: String,
    val rating: Double? = 4.5,
    val image: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)
