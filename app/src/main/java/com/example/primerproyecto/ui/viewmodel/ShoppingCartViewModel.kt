package com.example.primerproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.model.Products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingCartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<Map<Products, Int>>(emptyMap())
    val cartItems: StateFlow<Map<Products, Int>> = _cartItems.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    fun addToCart(product: Products) {
        val currentItems = _cartItems.value.toMutableMap()
        val currentQuantity = currentItems[product] ?: 0
        currentItems[product] = currentQuantity + 1
        _cartItems.value = currentItems
        updateTotalAmount()
    }

    fun removeFromCart(product: Products) {
        val currentItems = _cartItems.value.toMutableMap()
        currentItems.remove(product)
        _cartItems.value = currentItems
        updateTotalAmount()
    }

    fun updateQuantity(product: Products, quantity: Int) {
        val currentItems = _cartItems.value.toMutableMap()
        if (quantity <= 0) {
            currentItems.remove(product)
        } else {
            currentItems[product] = quantity
        }
        _cartItems.value = currentItems
        updateTotalAmount()
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
        _totalAmount.value = 0.0
    }

    fun getCartItemsList(): List<Pair<Products, Int>> {
        return _cartItems.value.toList()
    }

    private fun updateTotalAmount() {
        val total = _cartItems.value.entries.sumOf { (product, quantity) ->
            product.price * quantity
        }
        _totalAmount.value = total
    }

    fun getItemCount(): Int {
        return _cartItems.value.values.sum()
    }
}