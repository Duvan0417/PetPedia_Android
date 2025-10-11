package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("total_amount")
    val totalAmount: Double,

    @SerializedName("order_date")
    val orderDate: String,

    @SerializedName("status")
    val status: String = "pending",

    @SerializedName("items")
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Double
)