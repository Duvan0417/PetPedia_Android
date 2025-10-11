package com.example.primerproyecto.data.Apiservice

import com.example.primerproyecto.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitAPI {

    //  ENDPOINTS DE AUTENTICACIÓN
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    
    @GET("auth/roles")
    suspend fun getRoles(): Response<List<Role>>

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

    // ✅ NUEVO ENDPOINT - Agregar este método
    @GET("orders/user/{userId}")
    suspend fun getOrdersByUser(@Path("userId") userId: Int): Response<List<Orders>>

    // Nuevos endpoints para crear órdenes
    @POST("orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<Orders>

    // PÚBLICAS - no requieren token
    @GET("forums")
    suspend fun getAllPosts(): Response<ApiResponse<List<Forum>>>

    @GET("forums/{forumId}")
    suspend fun getPost(@Path("forumId") forumId: Int): Response<ApiResponse<Forum>>

    // PROTEGIDAS - requieren token
    @POST("forums")
    suspend fun createForumPost(
        @Header("Authorization") token: String,
        @Body postRequest: ForumPostRequest
    ): Response<ApiResponse<Forum>>

    @POST("forums/{forum}/comments")
    suspend fun addComment(
        @Header("Authorization") token: String,
        @Path("forum") forumId: Int,
        @Body commentRequest: ForumCommentRequest
    ): Response<ApiResponse<ForumComment>>

    @POST("forums/{forum}/like")
    suspend fun toggleLike(
        @Header("Authorization") token: String,
        @Path("forum") forumId: Int
    ): Response<ApiResponse<Unit>>
}
