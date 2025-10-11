// LoginViewModel.kt - ACTUALIZADO
package com.example.primerproyecto.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.data.model.LoginRequest
import com.example.primerproyecto.data.model.LoginResponse
import com.example.primerproyecto.data.model.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(private val context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse: StateFlow<LoginResponse?> = _loginResponse.asStateFlow()

    private val apiService = RetrofitService.apiService

    fun loginUser(loginRequest: LoginRequest) {
        _isLoading.value = true
        _errorMessage.value = null
        _loginResponse.value = null

        viewModelScope.launch {
            try {
                val response = apiService.login(loginRequest)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.success == true) {
                        // ✅ Obtener el rol del usuario SOLO desde user.role.name
                        val userRole = loginResponse.user?.role?.name ?: "client"

                        println("DEBUG: User role detected - $userRole") // Log para debug

                        _loginResponse.value = loginResponse
                        _errorMessage.value = null

                        // ✅ GUARDAR EL TOKEN EN AMBOS LUGARES
                        loginResponse.token?.let { token ->
                            // 1. Guardar en RetrofitService para solicitudes automáticas
                            RetrofitService.setAuthToken(token)

                            // 2. Guardar en SessionManager para ForumViewModel
                            sessionManager.saveAuthToken(token)

                            // 3. Guardar datos del usuario
                            loginResponse.user?.let { user ->
                                sessionManager.saveUserData(
                                    user.id ?: -1,
                                    user.name ?: "",
                                    user.email ?: ""
                                )
                            }
                        }

                        println("DEBUG: Token guardado en SessionManager: ${sessionManager.getAuthToken()}")

                    } else {
                        val errorMsg = loginResponse?.message ?: "Credenciales inválidas"
                        _errorMessage.value = errorMsg
                        _loginResponse.value = null
                    }
                } else {
                    val errorMessage = try {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody?.contains("message") == true) {
                            val errorJson = JSONObject(errorBody)
                            errorJson.getString("message") ?: "Error en el servidor (${response.code()})"
                        } else {
                            errorBody ?: "Error en el servidor (${response.code()})"
                        }
                    } catch (e: Exception) {
                        "Error de conexión: ${e.message}"
                    }
                    _errorMessage.value = errorMessage
                    _loginResponse.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
                _loginResponse.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _isLoading.value = false
        _errorMessage.value = null
        _loginResponse.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}