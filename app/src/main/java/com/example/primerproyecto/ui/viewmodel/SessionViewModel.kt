package com.example.primerproyecto.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.data.model.SessionManager
import com.example.primerproyecto.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SessionViewModel(private val context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val apiService = RetrofitService.apiService

    // Cargar información del usuario actual
    fun loadCurrentUser() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getCurrentUser()
                if (response.isSuccessful) {
                    _currentUser.value = response.body()
                }
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Establecer usuario después del login
    fun setUser(user: User) {
        _currentUser.value = user
    }

    // ✅ Cerrar sesión - LIMPIA TODO
    fun logout() {
        _currentUser.value = null
        RetrofitService.setAuthToken(null)
        sessionManager.clearSession() // ✅ Limpia las SharedPreferences
        println("DEBUG: SessionViewModel - Sesión limpiada")
    }

    // Obtener rol actual
    fun getCurrentRole(): String {
        return when (currentUser.value?.roleId) {
            1 -> "Cliente"
            2 -> "Veterinaria"
            3 -> "Entrenador"
            4 -> "Refugio"
            else -> "Cliente"
        }
    }

    // Verificar si hay sesión guardada
    fun hasActiveSession(): Boolean {
        return sessionManager.getAuthToken() != null
    }
}