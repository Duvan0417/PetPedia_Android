package com.example.primerproyecto.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.model.Request
import com.example.primerproyecto.model.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainerViewModel : ViewModel() {

    private val api = RetrofitService.apiService
    private val TAG = "TrainerViewModel"

    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    val requests: StateFlow<List<Request>> = _requests.asStateFlow()

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _trainerId = MutableStateFlow<Int?>(null)
    val trainerId: StateFlow<Int?> = _trainerId.asStateFlow()

    // ✅ Función para obtener el trainer_id desde el user_id
    fun loadTrainerId(userId: Int) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "🔍 Buscando trainerId para userId: $userId")
                val response = api.getTrainers()

                if (response.isSuccessful) {
                    val trainers = response.body() ?: emptyList()
                    Log.d(TAG, "📊 Total trainers encontrados: ${trainers.size}")

                    val trainer = trainers.find { it.user_id == userId }

                    if (trainer != null) {
                        _trainerId.value = trainer.id
                        Log.d(TAG, "✅ TrainerId encontrado: ${trainer.id}")

                        // Cargar datos automáticamente
                        trainer.id?.let {
                            loadRequests(it)
                            loadServices(it)
                        }
                    } else {
                        Log.w(TAG, "⚠️ No se encontró trainer para userId: $userId")
                        _message.value = "No se encontró perfil de entrenador"
                    }
                } else {
                    val errorMsg = "Error al obtener trainers (${response.code()})"
                    Log.e(TAG, errorMsg)
                    _message.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error de conexión: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _message.value = errorMsg
            }
        }
    }

    // --- SOLICITUDES ---
    fun loadRequests(trainerId: Int) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "📥 Cargando requests para trainerId: $trainerId")
                val response = api.getRequestsByTrainer(trainerId)

                if (response.isSuccessful) {
                    val requestsList = response.body() ?: emptyList()
                    _requests.value = requestsList
                    Log.d(TAG, "✅ Requests cargados: ${requestsList.size}")
                } else {
                    val errorMsg = "Error cargando solicitudes (${response.code()})"
                    Log.e(TAG, "$errorMsg - ${response.errorBody()?.string()}")
                    _message.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _message.value = errorMsg
            }
        }
    }

    fun acceptRequest(id: Int) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "✅ Aceptando solicitud: $id")
                val response = api.acceptRequest(id)

                if (response.isSuccessful) {
                    _message.value = "Solicitud aceptada exitosamente"
                    Log.d(TAG, "✅ Solicitud $id aceptada")
                    _trainerId.value?.let { loadRequests(it) }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error al aceptar (${response.code()}): $errorBody"
                    Log.e(TAG, errorMsg)
                    _message.value = "Error al aceptar solicitud"
                }
            } catch (e: Exception) {
                val errorMsg = "Error: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _message.value = errorMsg
            }
        }
    }

    fun rejectRequest(id: Int) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "❌ Rechazando solicitud: $id")
                val response = api.rejectRequest(id)

                if (response.isSuccessful) {
                    _message.value = "Solicitud rechazada"
                    Log.d(TAG, "❌ Solicitud $id rechazada")
                    _trainerId.value?.let { loadRequests(it) }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error al rechazar (${response.code()}): $errorBody"
                    Log.e(TAG, errorMsg)
                    _message.value = "Error al rechazar solicitud"
                }
            } catch (e: Exception) {
                val errorMsg = "Error: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _message.value = errorMsg
            }
        }
    }

    // --- SERVICIOS ---
    fun loadServices(trainerId: Int) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "📥 Cargando servicios para trainerId: $trainerId")
                val response = api.getServicesByTrainer(trainerId)

                if (response.isSuccessful) {
                    val servicesList = response.body() ?: emptyList()
                    _services.value = servicesList
                    Log.d(TAG, "✅ Servicios cargados: ${servicesList.size}")

                    if (servicesList.isEmpty()) {
                        _message.value = ""
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error cargando servicios (${response.code()}): $errorBody"
                    Log.e(TAG, errorMsg)
                    _message.value = "Error cargando servicios"
                }
            } catch (e: Exception) {
                val errorMsg = "Error de red: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _message.value = errorMsg
            }
        }
    }

    fun createService(service: Service) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "➕ Creando servicio: ${service.name}")
                Log.d(TAG, "📤 Datos del servicio: $service")

                val response = api.createService(service)

                if (response.isSuccessful) {
                    val createdService = response.body()
                    Log.d(TAG, "✅ Servicio creado: ${createdService?.id}")
                    _message.value = "Servicio creado exitosamente"

                    // Recargar servicios
                    _trainerId.value?.let { loadServices(it) }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error al crear servicio (${response.code()}): $errorBody"
                    Log.e(TAG, errorMsg)
                    _message.value = "Error al crear servicio: ${response.code()}"
                }
            } catch (e: Exception) {
                val errorMsg = "Error de conexión: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            try {
                service.id?.let { id ->
                    Log.d(TAG, "✏️ Actualizando servicio: $id")
                    val response = api.updateService(id, service)

                    if (response.isSuccessful) {
                        Log.d(TAG, "✅ Servicio actualizado: $id")
                        _message.value = "Servicio actualizado exitosamente"
                        _trainerId.value?.let { loadServices(it) }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMsg = "Error al actualizar (${response.code()}): $errorBody"
                        Log.e(TAG, errorMsg)
                        _message.value = "Error al actualizar servicio"
                    }
                }
            } catch (e: Exception) {
                val errorMsg = "Error: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _message.value = errorMsg
            }
        }
    }

    fun deleteService(serviceId: Int) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "🗑️ Eliminando servicio: $serviceId")
                val response = api.deleteService(serviceId)

                if (response.isSuccessful) {
                    Log.d(TAG, "✅ Servicio eliminado: $serviceId")
                    _message.value = "Servicio eliminado exitosamente"
                    _trainerId.value?.let { loadServices(it) }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error al eliminar (${response.code()}): $errorBody"
                    Log.e(TAG, errorMsg)
                    _message.value = "Error al eliminar servicio"
                }
            } catch (e: Exception) {
                val errorMsg = "Error: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _message.value = errorMsg
            }
        }
    }
}