package com.example.primerproyecto.data.model

data class Home(
    val welcomeMessage: String, // Parámetro para un mensaje de bienvenida
    val featuredProducts: List<Products>, // Parámetro para una lista de productos
)