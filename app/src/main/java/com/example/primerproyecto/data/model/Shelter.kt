package com.example.primerproyecto.data.model

data class Shelter(
    val id: Int,
    val shelter_name: String,           // ✅ Coincide con el JSON del backend
    val responsible_person: String,     // ✅ Coincide con el JSON del backend
    val capacity: Int,                  // ✅ Campo presente en el JSON
    val rating: Double = 0.0,          // ✅ Campo presente en el JSON
    val review_count: Int = 0,         // ✅ Campo presente en el JSON
    val image: String? = null,         // ✅ Campo presente en el JSON
    val user_id: Int,                  // ✅ AGREGAR ESTA PROPIEDAD FALTANTE
    val created_at: String? = null,
    val updated_at: String? = null
)