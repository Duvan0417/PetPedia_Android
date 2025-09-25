package com.example.primerproyecto.data.model

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

data class Pet(
    val id: Int,
    val name: String,
    val age: String,
    val species: String, // Nuevo campo
    val breed: String,
    val size: String, // Cambiado de "size" a String para coincidir con "17.09"
    val sex: String, // Cambiado de "gender" a "sex"
    val description: String,
    val image: String? = null,
    val birth_date: String? = null, // Nuevo campo
    val shelter_id: Int? = null, // Nuevo campo
    val user_id: Int? = null, // Nuevo campo
    val veterinary_id: Int? = null, // Nuevo campo
    val created_at: String? = null,
    val updated_at: String? = null,
    // Mantener vaccines y health como opcionales por si acaso
    val vaccines: List<String>? = emptyList(),
    val health: String? = null
)
