package com.example.primerproyecto.data.model

data class OrderItems (

    val id: Int,
    val quantity: Int,
    val price: Double,
    val order_id: Int? = null,
    val product_id: Int? = null,
    val product: Products? = null, // Datos del producto
    val created_at: String? = null,
    val updated_at: String? = null
)


