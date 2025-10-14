package com.example.primerproyecto.ui.view.UserEntrenador

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.primerproyecto.ui.view.UserEntrenador.UserEntrenadorConfiguracionScreen
import com.example.primerproyecto.ui.view.UserEntrenador.UserEntrenadorSolicitudesScreen

@Composable
fun UserEntrenadorMainScreen(currentUserId: Int) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Solicitudes", "Servicios", "Configuración")

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.List, contentDescription = item)
                                1 -> Icon(Icons.Default.FitnessCenter, contentDescription = item)
                                2 -> Icon(Icons.Default.Settings, contentDescription = item)
                            }
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> UserEntrenadorSolicitudesScreen(userId = currentUserId, modifier = Modifier.padding(innerPadding))
            1 -> UserEntrenadorServiciosScreen(userId = currentUserId, modifier = Modifier.padding(innerPadding))
            2 -> UserEntrenadorConfiguracionScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
