package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class Trainer(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("specialty") val specialty: String? = null,
    @SerializedName("experience_years") val experience_years: Int? = null,
    @SerializedName("qualifications") val qualifications: String? = null,
    @SerializedName("hourly_rate") val hourly_rate: Double? = null,
    @SerializedName("rating") val rating: Double? = null,
    @SerializedName("review_count") val review_count: Int? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("user_id") val user_id: Int? = null,
    @SerializedName("created_at") val created_at: String? = null,
    @SerializedName("updated_at") val updated_at: String? = null
)