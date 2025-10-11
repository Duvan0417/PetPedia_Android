package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class OrderItems(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Double,

    @SerializedName("order_id")
    val orderId: Int? = null,

    @SerializedName("product_id")
    val productId: Int? = null,

    @SerializedName("product")
    val product: Products? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)