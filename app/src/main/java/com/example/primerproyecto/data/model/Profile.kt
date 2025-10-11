
package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("biography")
    val biography: String? = null,

    @SerializedName("photo")
    val photo: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)
