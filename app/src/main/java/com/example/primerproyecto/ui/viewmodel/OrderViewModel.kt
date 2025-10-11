package com.example.primerproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.data.model.Orders
import com.example.primerproyecto.data.model.OrderItems
import com.example.primerproyecto.data.model.Products
import com.example.primerproyecto.data.model.OrderRequest
import com.example.primerproyecto.data.model.OrderItemRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderViewModel : ViewModel() {
    private val _orders = MutableStateFlow<List<Orders>>(emptyList())
    val orders: StateFlow<List<Orders>> = _orders.asStateFlow()

    private val _orderItems = MutableStateFlow<List<OrderItems>>(emptyList())
    val orderItems: StateFlow<List<OrderItems>> = _orderItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _orderCreated = MutableStateFlow(false)
    val orderCreated: StateFlow<Boolean> = _orderCreated.asStateFlow()

    init {
        loadOrders()
        loadOrderItems()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                println("DEBUG: Cargando pedidos del usuario autenticado")
                val response = RetrofitService.apiService.getOrders()

                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    println("DEBUG: Pedidos del usuario autenticado: ${orders.size}")
                    _orders.value = orders
                } else {
                    when (response.code()) {
                        401 -> _error.value = "No autorizado. Inicia sesión nuevamente."
                        else -> _error.value = "Error al cargar pedidos: ${response.code()}"
                    }
                    _orders.value = emptyList()
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
                _orders.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadOrdersByUser(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                println("🔄 DEBUG: Cargando pedidos para usuario ID: $userId")

                if (userId <= 0) {
                    _error.value = "Usuario no válido"
                    _orders.value = emptyList()
                    return@launch
                }

                // PRIMERO: Probar conexión básica
                println("🌐 DEBUG: Probando conexión con API...")
                val testResponse = RetrofitService.apiService.getOrders() // Endpoint que sabemos que funciona
                println("🌐 DEBUG: Test conexión - Código: ${testResponse.code()}")

                // LUEGO: Intentar el endpoint específico
                println("📡 DEBUG: Llamando a orders/user/$userId")
                val response = RetrofitService.apiService.getOrdersByUser(userId)
                println("📡 DEBUG: Respuesta código: ${response.code()}")
                println("📡 DEBUG: Respuesta mensaje: ${response.message()}")

                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    println("❌ DEBUG: Error body: $errorBody")
                }

                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    println("✅ DEBUG: Pedidos obtenidos para usuario $userId: ${orders.size}")
                    _orders.value = orders

                    loadOrderItemsForUserOrders(orders)
                } else {
                    when (response.code()) {
                        404 -> _error.value = "Usuario no encontrado"
                        401 -> _error.value = "No autorizado"
                        500 -> {
                            _error.value = "Error del servidor (500). Revisa logs de Laravel."
                            println("🚨 DEBUG: Error 500 - Problema en el backend")
                        }
                        else -> _error.value = "Error ${response.code()}: ${response.message()}"
                    }
                    _orders.value = emptyList()
                }
            } catch (e: Exception) {
                println("💥 DEBUG: Excepción: ${e.message}")
                _error.value = "Error de conexión: ${e.message}"
                _orders.value = emptyList()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función auxiliar para cargar items específicos de los pedidos del usuario
    private fun loadOrderItemsForUserOrders(userOrders: List<Orders>) {
        viewModelScope.launch {
            try {
                val response = RetrofitService.apiService.getOrderItems()
                if (response.isSuccessful) {
                    val allItems = response.body() ?: emptyList()
                    // Filtrar items que pertenecen a los pedidos del usuario
                    val userOrderIds = userOrders.map { it.id }
                    val userItems = allItems.filter { it.orderId in userOrderIds }
                    _orderItems.value = userItems
                    println("DEBUG: Items de pedidos del usuario: ${userItems.size}")
                }
            } catch (e: Exception) {
                println("Error al cargar items del usuario: ${e.message}")
            }
        }
    }

    fun loadOrderItems() {
        viewModelScope.launch {
            try {
                val response = RetrofitService.apiService.getOrderItems()
                if (response.isSuccessful) {
                    _orderItems.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                println("Error al cargar items: ${e.message}")
            }
        }
    }

    fun createOrder(userId: Int, cartItems: List<Pair<Products, Int>>) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _orderCreated.value = false

            try {
                // Calcular el total
                val totalAmount = cartItems.sumOf { it.first.price * it.second }

                // Formatear fecha actual
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                // Crear items para la request
                val orderItems = cartItems.map { (product, quantity) ->
                    OrderItemRequest(
                        productId = product.id,
                        quantity = quantity,
                        price = product.price
                    )
                }

                // Crear la order request
                val orderRequest = OrderRequest(
                    userId = userId,
                    totalAmount = totalAmount,
                    orderDate = currentDate,
                    status = "pending",
                    items = orderItems
                )

                println("DEBUG: Enviando order request: $orderRequest")

                val response = RetrofitService.apiService.createOrder(orderRequest)
                if (response.isSuccessful) {
                    _orderCreated.value = true
                    // Recargar pedidos del usuario específico
                    loadOrdersByUser(userId)
                    println("DEBUG: Orden creada exitosamente")
                } else {
                    val errorMsg = "Error al crear pedido: ${response.code()} - ${response.message()}"
                    _error.value = errorMsg
                    println("DEBUG: $errorMsg")
                }
            } catch (e: Exception) {
                val errorMsg = "Error al crear pedido: ${e.message}"
                _error.value = errorMsg
                println("DEBUG: $errorMsg")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getOrderItemsForOrder(orderId: Int): List<OrderItems> {
        return _orderItems.value.filter { it.orderId == orderId }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetOrderCreated() {
        _orderCreated.value = false
    }
}