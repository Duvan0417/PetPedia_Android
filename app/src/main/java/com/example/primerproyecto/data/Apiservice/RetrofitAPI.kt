package com.example.primerproyecto.data.Apiservice

import com.example.primerproyecto.data.model.DataModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface RetrofitAPI {
    @GET("veterinarias")
    suspend fun getVeterinarias(): Response<VeterinariaResponse>

    @GET("adopciones")
    suspend fun getAdopciones(): Response<AdopcionResponse>

 }