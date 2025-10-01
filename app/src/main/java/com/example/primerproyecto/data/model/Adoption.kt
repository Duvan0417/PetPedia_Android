package com.example.primerproyecto.data.model
import com.example.primerproyecto.data.model.Pet

data class Adoption (
    val id: Int,
    val status: String, // "pending", "approved", "rejected"
    val comment: String? = null,
    val pet_id: Int? = null,
    val shelter_id: Int? = null,
    val pet: Pet? = null, // Datos de la mascota
    val created_at: String? = null,
    val updated_at: String? = null
)



