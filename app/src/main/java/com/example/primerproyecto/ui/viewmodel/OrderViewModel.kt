package com.example.primerproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.data.model.Orders
import com.example.primerproyecto.data.model.OrderItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val _orders = MutableStateFlow<List<Orders>>(emptyList())
    val orders: StateFlow<List<Orders>> = _orders.asStateFlow()

    private val _orderItems = MutableStateFlow<List<OrderItems>>(emptyList())
    val orderItems: StateFlow<List<OrderItems>> = _orderItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadOrders()
        loadOrderItems()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = RetrofitService.apiService.getOrders()
                if (response.isSuccessful) {
                    _orders.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar pedidos: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
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
                // No marcamos error aquí para no interferir con la carga de órdenes
                println("Error al cargar items: ${e.message}")
            }
        }
    }

    fun getOrderItemsForOrder(orderId: Int): List<OrderItems> {
        return _orderItems.value.filter { it.order_id == orderId }
    }

    fun createOrder(orderRequest: Orders) { // ✅ Ya no necesita el path completo
        viewModelScope.launch {
            try {
                val response = RetrofitService.apiService.createOrder(orderRequest)
                if (response.isSuccessful) {
                    loadOrders() // Recargar órdenes
                } else {
                    _error.value = "Error al crear pedido: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al crear pedido: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}