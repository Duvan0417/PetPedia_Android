package com.example.primerproyecto.ui.viewmodel

import com.example.primerproyecto.data.model.RegisterResponse

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val response: RegisterResponse) : RegisterState()
    data class Error(val message: String) : RegisterState()
}