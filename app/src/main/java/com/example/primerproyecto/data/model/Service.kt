package com.example.primerproyecto.model

data class Service(
    val id: Int? = null,
    val name: String,
    val price: Double,
    val description: String,
    val duration: String,
    val trainer_id: Int?
)