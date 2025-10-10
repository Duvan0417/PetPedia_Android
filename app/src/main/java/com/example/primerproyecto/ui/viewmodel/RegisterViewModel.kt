package com.example.primerproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.data.model.RegisterRequest
import com.example.primerproyecto.data.model.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _registerSuccess = MutableStateFlow(false) // ✅ ESTADO DE ÉXITO
    val registerSuccess: StateFlow<Boolean> = _registerSuccess.asStateFlow()

    private val apiService = RetrofitService.apiService

    fun registerUser(registerRequest: RegisterRequest) {
        _isLoading.value = true
        _errorMessage.value = null
        _registerSuccess.value = false

        viewModelScope.launch {
            try {
                val response = apiService.register(registerRequest)

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.success == true) {
                        _registerSuccess.value = true
                        _errorMessage.value = null
                    } else {
                        val errorMsg = registerResponse?.message ?: "Error desconocido en el registro"
                        _errorMessage.value = errorMsg
                        _registerSuccess.value = false
                    }
                } else {
                    // Manejo de errores...
                    _registerSuccess.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
                _registerSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _isLoading.value = false
        _errorMessage.value = null
        _registerSuccess.value = false
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}