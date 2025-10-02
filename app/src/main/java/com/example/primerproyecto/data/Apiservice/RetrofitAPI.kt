package com.example.primerproyecto.data.Apiservice

import com.example.primerproyecto.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitAPI {

    //  ENDPOINTS DE AUTENTICACIÓN
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("auth/roles")
    suspend fun getRoles(): Response<Role>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<User>

    //  ENDPOINTS SERVICIOS
    @GET("veterinaries")
    suspend fun getVeterinarias(): Response<List<Veterinarian>>

    @GET("adoptions")
    suspend fun getAdoptions(): Response<List<Adoption>>

    @GET("trainers")
    suspend fun getTrainers(): Response<List<Trainer>>

    @GET("products")
    suspend fun getProducts(): Response<List<Products>>

    @GET("orders")
    suspend fun getOrders(): Response<List<Orders>>

    @GET("orderitems")
    suspend fun getOrderItems(): Response<List<OrderItems>>

    @GET("orderitems/order/{orderId}")
    suspend fun getOrderItemsByOrder(@Path("orderId") orderId: Int): Response<List<OrderItems>>

    @POST("orders")
    suspend fun createOrder(@Body orderRequest: Orders): Response<Orders>

    @POST("orderitems")
    suspend fun createOrderItem(@Body orderItem: OrderItems): Response<OrderItems>
}