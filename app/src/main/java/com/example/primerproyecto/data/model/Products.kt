package com.example.primerproyecto.data.model

data class Products (

    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val image: String? = null,
    val category_id: Int? = null,
    val veterinary_id: Int? = null,
    val shoppingcar_id: Int? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)
