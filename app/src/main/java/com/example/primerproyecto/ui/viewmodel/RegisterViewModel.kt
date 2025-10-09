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

    // Estados simples sin sealed class
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _registerResponse = MutableStateFlow<RegisterResponse?>(null)
    val registerResponse: StateFlow<RegisterResponse?> = _registerResponse.asStateFlow()

    private val apiService = RetrofitService.apiService

    fun registerUser(registerRequest: RegisterRequest) {
        _isLoading.value = true
        _errorMessage.value = null
        _registerResponse.value = null

        viewModelScope.launch {
            try {
                val response = apiService.register(registerRequest)

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.success == true) {
                        _registerResponse.value = registerResponse
                        _errorMessage.value = null
                    } else {
                        val errorMsg = registerResponse?.message ?: "Error desconocido en el registro"
                        _errorMessage.value = errorMsg
                        _registerResponse.value = null
                    }
                } else {
                    val errorMessage = try {
                        val errorBody = response.errorBody()?.string()
                        // Intentar parsear el JSON de error
                        if (errorBody?.contains("errors") == true) {
                            // Extraer mensajes de validación del backend
                            val errorJson = JSONObject(errorBody)
                            val errors = errorJson.optJSONObject("errors")
                            if (errors != null) {
                                val firstError = errors.keys().asSequence().firstOrNull()
                                if (firstError != null) {
                                    val errorArray = errors.getJSONArray(firstError)
                                    if (errorArray.length() > 0) {
                                        "${firstError.replace("_", " ").capitalize()}: ${errorArray.getString(0)}"
                                    } else {
                                        "Error de validación en $firstError"
                                    }
                                } else {
                                    errorJson.getString("message") ?: "Error de validación"
                                }
                            } else {
                                errorJson.getString("message") ?: "Error en el servidor (${response.code()})"
                            }
                        } else {
                            errorBody ?: "Error en el servidor (${response.code()})"
                        }
                    } catch (e: Exception) {
                        "Error de conexión: ${e.message}"
                    }
                    _errorMessage.value = errorMessage
                    _registerResponse.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
                _registerResponse.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _isLoading.value = false
        _errorMessage.value = null
        _registerResponse.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearRegisterResponse() {
        _registerResponse.value = null
    }
}