package com.example.primerproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.Apiservice.RetrofitAPI
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.model.Request
import com.example.primerproyecto.model.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainerViewModel : ViewModel() {

    private val api = RetrofitService.apiService

    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    val requests: StateFlow<List<Request>> = _requests.asStateFlow()

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    // --- SOLICITUDES ---
    fun loadRequests() {
        viewModelScope.launch {
            try {
                val response = api.getRequests()
                if (response.isSuccessful) {
                    _requests.value = response.body() ?: emptyList()
                } else {
                    _message.value = "Error cargando solicitudes (${response.code()})"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun createRequest(request: Request) {
        viewModelScope.launch {
            try {
                val response = api.createRequest(request)
                if (response.isSuccessful) {
                    _message.value = "Solicitud creada exitosamente"
                    loadRequests()
                } else {
                    _message.value = "Error al crear solicitud (${response.code()})"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun updateRequest(id: Int, request: Request) {
        viewModelScope.launch {
            try {
                val response = api.updateRequest(id, request)
                if (response.isSuccessful) {
                    _message.value = "Solicitud actualizada"
                    loadRequests()
                } else {
                    _message.value = "Error al actualizar solicitud (${response.code()})"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteRequest(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.deleteRequest(id)
                if (response.isSuccessful) {
                    _message.value = "Solicitud eliminada"
                    loadRequests()
                } else {
                    _message.value = "Error al eliminar solicitud (${response.code()})"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    // --- SERVICIOS ---
    fun loadServices() {
        viewModelScope.launch {
            try {
                val response = api.getServices()
                if (response.isSuccessful) {
                    _services.value = response.body() ?: emptyList()
                } else {
                    _message.value = "Error cargando servicios (${response.code()})"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun createService(service: Service) {
        viewModelScope.launch {
            try {
                val response = api.createService(service)
                if (response.isSuccessful) {
                    _message.value = "Servicio creado exitosamente"
                    loadServices()
                } else {
                    _message.value = "Error al crear servicio (${response.code()})"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }
    fun acceptRequest(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.acceptRequest(id)
                if (response.isSuccessful) {
                    _message.value = "Solicitud aceptada correctamente"
                    loadRequests()
                } else {
                    _message.value = "Error al aceptar solicitud (${response.code()})"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun rejectRequest(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.rejectRequest(id)
                if (response.isSuccessful) {
                    _message.value = "Solicitud rechazada correctamente"
                    loadRequests()
                } else {
                    _message.value = "Error al rechazar solicitud (${response.code()})"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }
}
