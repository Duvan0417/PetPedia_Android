package com.example.primerproyecto.data.model

data class Veterinarian (
    val id: Int,
    val name: String,
    val image: String? = null,
    val email: String,
    val phone: String,
    val address: String,
    val schedules: String? = null,
    val user_id: Int? = null,
)
