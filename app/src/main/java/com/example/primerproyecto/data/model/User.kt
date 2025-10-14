
package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("role_id")
    val roleId: Int,

    @SerializedName("role")
    val role: Role?, // ✅ Usando tu Role existente

    @SerializedName("profile")
    val profile: Profile?,

    // ✅ Usando tus modelos existentes
    @SerializedName("veterinary")
    val veterinary: Veterinarian? = null,

    @SerializedName("trainer")
    val trainer: Trainer? = null,

    @SerializedName("shelter")
    val shelter: Shelter? = null,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)

