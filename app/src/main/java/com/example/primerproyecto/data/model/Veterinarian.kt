package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class Veterinarian(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("clinic_name") val clinic_name: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("specialization") val specialization: String? = null,
    @SerializedName("veterinary_license") val veterinary_license: String? = null,
    @SerializedName("schedules") val schedules: String? = null,
    @SerializedName("user_id") val user_id: Int? = null
) {
    // Función helper para obtener el nombre de la clínica
    fun getClinicName(): String {
        return clinic_name ?: name ?: "Sin nombre"
    }
}