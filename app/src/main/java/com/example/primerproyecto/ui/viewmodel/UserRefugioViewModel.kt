package com.example.primerproyecto.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.data.model.Adoption
import com.example.primerproyecto.data.model.Pet
import com.example.primerproyecto.data.model.Shelter
import kotlinx.coroutines.launch

class UserRefugioViewModel(private val userId: Int) : ViewModel() {
    private val _shelter = mutableStateOf<Shelter?>(null)
    val shelter: State<Shelter?> = _shelter

    private val _adoptions = mutableStateListOf<Adoption>()
    val adoptions: List<Adoption> = _adoptions

    private val _pets = mutableStateListOf<Pet>()
    val pets: List<Pet> = _pets

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        loadShelterInfo()
    }

    private fun loadShelterInfo() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitService.apiService.getShelters()
                if (response.isSuccessful) {
                    val allShelters = response.body() ?: emptyList()

                    // ✅ Manejo mejorado: user_id ahora es nullable
                    _shelter.value = allShelters.find { it.user_id == userId }

                    if (_shelter.value != null) {
                        loadAdoptions()
                        loadPets()
                    } else {
                        _error.value = "No se encontró un refugio asociado a este usuario"
                    }
                } else {
                    _error.value = "Error al cargar información del refugio: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAdoptions() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitService.apiService.getAdoptions()
                if (response.isSuccessful) {
                    val allAdoptions = response.body() ?: emptyList()
                    val shelterId = _shelter.value?.id
                    _adoptions.clear()
                    if (shelterId != null) {
                        _adoptions.addAll(allAdoptions.filter { it.shelter_id == shelterId })
                    }
                } else {
                    _error.value = "Error al cargar adopciones: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar adopciones: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPets() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitService.apiService.getPets()
                if (response.isSuccessful) {
                    val allPets = response.body() ?: emptyList()
                    val shelterId = _shelter.value?.id
                    _pets.clear()
                    if (shelterId != null) {
                        _pets.addAll(allPets.filter { it.shelter_id == shelterId })
                    }
                } else {
                    _error.value = "Error al cargar mascotas: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar mascotas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAdoptionStatus(adoptionId: Int, newStatus: String) {
        viewModelScope.launch {
            try {
                val adoption = _adoptions.find { it.id == adoptionId }
                if (adoption != null) {
                    val updatedAdoption = adoption.copy(status = newStatus)
                    val response = RetrofitService.apiService.updateAdoption(
                        adoptionId,
                        updatedAdoption
                    )
                    if (response.isSuccessful) {
                        val index = _adoptions.indexOfFirst { it.id == adoptionId }
                        if (index != -1) {
                            _adoptions[index] = updatedAdoption
                        }
                    } else {
                        _error.value = "Error al actualizar adopción: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error al actualizar adopción: ${e.message}"
            }
        }
    }

    fun deletePet(petId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitService.apiService.deletePet(petId)
                if (response.isSuccessful) {
                    _pets.removeAll { it.id == petId }
                } else {
                    _error.value = "Error al eliminar mascota: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al eliminar mascota: ${e.message}"
            }
        }
    }

    // ✅ Función adicional para refrescar toda la información
    fun refresh() {
        loadShelterInfo()
    }

    fun clearError() {
        _error.value = null
    }
}