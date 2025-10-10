
package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("role") // ✅ NUEVO: Campo para el rol
    val role: String? = "client" // Valor por defecto
)
