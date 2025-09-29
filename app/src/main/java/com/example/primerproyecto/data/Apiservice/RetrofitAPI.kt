package com.example.primerproyecto.data.Apiservice

import com.example.primerproyecto.data.model.Adoption
import com.example.primerproyecto.data.model.Trainer
import com.example.primerproyecto.data.model.Veterinarian
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RetrofitAPI {
    @GET("veterinaries") // ruta para las veterinarias
    suspend fun getVeterinarias(): Response<List<Veterinarian>>

    @GET("adoptions") // ruta para las adopciones
    suspend fun getAdoptions(): Response<List<Adoption>>

    @GET("trainers")
    suspend fun getTrainers(): Response<List<Trainer>>


 }