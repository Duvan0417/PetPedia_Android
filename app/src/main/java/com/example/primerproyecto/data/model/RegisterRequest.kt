package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("password_confirmation")
    val password_confirmation: String,

    @SerializedName("role_id")
    val role_id: Int,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("clinic_name")
    val clinic_name: String? = null,

    @SerializedName("veterinary_license")
    val veterinary_license: String? = null,

    @SerializedName("specialization")
    val specialization: String? = null,

    @SerializedName("specialty")
    val specialty: String? = null,

    @SerializedName("experience_years")
    val experience_years: Int? = null,

    @SerializedName("qualifications")
    val qualifications: String? = null,

    @SerializedName("hourly_rate")
    val hourly_rate: Double? = null,

    @SerializedName("shelter_name")
    val shelter_name: String? = null,

    @SerializedName("responsible_person")
    val responsible_person: String? = null,

    @SerializedName("capacity")
    val capacity: Int? = null
)