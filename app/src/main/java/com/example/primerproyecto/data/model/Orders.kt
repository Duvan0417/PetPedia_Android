package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class Orders(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("status")
    val status: String, // "pending", "completed", "cancelled"

    @SerializedName("total_amount")
    val totalAmount: Double,

    @SerializedName("order_date")
    val orderDate: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)