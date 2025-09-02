package com.example.primerproyecto.data.Apiservice

import com.example.primerproyecto.data.model.DataModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface RetrofitAPI {
   @PUT("api/users/2")
    fun updateData(@Body dataModel: DataModel?):
           Call<DataModel?>

 }