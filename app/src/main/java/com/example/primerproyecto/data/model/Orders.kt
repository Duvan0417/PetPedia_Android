package com.example.primerproyecto.data.model

data class Orders(
    val id: Int,
    val status: String, // "pending", "completed", "cancelled"
    val total_amount: Double,
    val order_date: String,
    val user_id: Int,
    val created_at: String? = null,
    val updated_at: String? = null

        )