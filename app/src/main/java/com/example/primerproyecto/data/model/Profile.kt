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

    @SerializedName("clinic_name")
    val clinicName: String? = null,

    @SerializedName("veterinary_license")
    val veterinaryLicense: String? = null,

    @SerializedName("specialization")
    val specialization: String? = null,

    @SerializedName("schedules")
    val schedules: String? = null,

    @SerializedName("specialty")
    val specialty: String? = null,

    @SerializedName("experience_years")
    val experienceYears: Int? = null,

    @SerializedName("qualifications")
    val qualifications: String? = null,

    @SerializedName("hourly_rate")
    val hourlyRate: Double? = null,

    @SerializedName("shelter_name")
    val shelterName: String? = null,

    @SerializedName("responsible_person")
    val responsiblePerson: String? = null,

    @SerializedName("capacity")
    val capacity: Int? = null,

    @SerializedName("rating")
    val rating: String? = null,

    @SerializedName("review_count")
    val reviewCount: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)