package com.example.primerproyecto.data.Apiservice

import com.example.primerproyecto.data.model.Adoption
import com.example.primerproyecto.data.model.OrderItems
import com.example.primerproyecto.data.model.Orders
import com.example.primerproyecto.data.model.Products
import com.example.primerproyecto.data.model.Trainer
import com.example.primerproyecto.data.model.Veterinarian
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitAPI {
    @GET("veterinaries") // ruta para las veterinarias
    suspend fun getVeterinarias(): Response<List<Veterinarian>>

    @GET("adoptions") // ruta para las adopciones
    suspend fun getAdoptions(): Response<List<Adoption>>

    @GET("trainers")
    suspend fun getTrainers(): Response<List<Trainer>>

    @GET("products") // ✅ NUEVO ENDPOINT
    suspend fun getProducts(): Response<List<Products>>

    @GET("orders")
    suspend fun getOrders(): Response<List<Orders>>

    @GET("orderitems") // ✅ Cambiado a "orderitems" (igual que tu tabla)
    suspend fun getOrderItems(): Response<List<OrderItems>>

    @GET("orderitems/order/{orderId}") // Endpoint para items por orden
    suspend fun getOrderItemsByOrder(@Path("orderId") orderId: Int): Response<List<OrderItems>>

    @POST("orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<Orders>

    @POST("orderitems")
    suspend fun createOrderItem(@Body orderItem: OrderItemRequest): Response<OrderItems>
}
data class OrderRequest(
    val status: String = "pending",
    val total_amount: Double,
    val user_id: Int,
    val order_date: String
)

data class OrderItemRequest(
    val quantity: Int,
    val price: Double,
    val order_id: Int,
    val product_id: Int
)

