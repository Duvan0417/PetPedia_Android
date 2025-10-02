package com.example.primerproyecto.data.Apiservice

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    // Variable para almacenar el token (puedes obtenerlo de SharedPreferences después)
    private var authToken: String? = null

    // Función para actualizar el token
    fun setAuthToken(token: String?) {
        authToken = token
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            // Si tenemos token, agregarlo al header
            val requestBuilder = originalRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")

            authToken?.let { token ->
                requestBuilder.header("Authorization", "Bearer $token")
            }

            val newRequest = requestBuilder.build()
            chain.proceed(newRequest)
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // ¡Importante agregar el cliente!
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: RetrofitAPI by lazy {
        retrofit.create(RetrofitAPI::class.java)
    }
}