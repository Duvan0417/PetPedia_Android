package com.example.primerproyecto.data.Apiservice

import com.example.primerproyecto.data.model.*
import com.example.primerproyecto.model.Request
import com.example.primerproyecto.model.Service
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.*

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
    // 🔹 SOLICITUDES
    @GET("requestts")
    suspend fun getRequests(): Response<List<Request>>

    @POST("requestts")
    suspend fun createRequest(@Body request: Request): Response<Request>

    @GET("requestts/{id}")
    suspend fun getRequestById(@Path("id") id: Int): Response<Request>

    @PUT("requestts/{id}")
    suspend fun updateRequest(@Path("id") id: Int, @Body request: Request): Response<Request>

    @DELETE("requestts/{id}")
    suspend fun deleteRequest(@Path("id") id: Int): Response<Unit>

    // 🔹 SERVICIOS
    @GET("services")
    suspend fun getServices(): Response<List<Service>>

    @POST("services")
    suspend fun createService(@Body service: Service): Response<Service>

    @GET("services/{id}")
    suspend fun getServiceById(@Path("id") id: Int): Response<Service>

    @PUT("services/{id}")
    suspend fun updateService(@Path("id") id: Int, @Body service: Service): Response<Service>

    @DELETE("services/{id}")
    suspend fun deleteService(@Path("id") id: Int): Response<Unit>

    @POST("requestts/{id}/accept")
    suspend fun acceptRequest(@Path("id") id: Int): Response<Unit>

    @POST("requestts/{id}/reject")
    suspend fun rejectRequest(@Path("id") id: Int): Response<Unit>

}
