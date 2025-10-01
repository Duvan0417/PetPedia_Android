package com.example.primerproyecto.data.model

data class User (
    val id: Int? = null,
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
    val telefono: String? = null,
    val direccion: String? = null,
    val horario: String? = null,
    val role: String? = null
)

//data class AuthResponse(
    //val usuario: User,
  //  val token: String
//)
