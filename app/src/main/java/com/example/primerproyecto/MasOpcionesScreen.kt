package com.example.primerproyecto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MasOpcionesScreen(
    onNavigateToPerfil: () -> Unit,
    onNavigateToEntrenadores: () -> Unit,
    onNavigateToAdopciones: () -> Unit
) {
    val opciones = listOf(
        "Perfil" to Icons.Default.Person,
        "Entrenadores" to Icons.Default.FitnessCenter,
        "Adopciones" to Icons.Default.Pets, // 👈 nueva opción
        "Configuración" to Icons.Default.Settings,
        "Pedidos" to Icons.Default.Receipt,
        "Foro" to Icons.Default.Forum,
        "Solicitudes" to Icons.Default.MarkEmailUnread,
        "Gestionar Servicios" to Icons.Default.Build
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        opciones.forEach { (titulo, icono) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                onClick = {
                    when (titulo) {
                        "Perfil" -> onNavigateToPerfil()
                        "Entrenadores" -> onNavigateToEntrenadores()
                        "Adopciones" -> onNavigateToAdopciones()
                    }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(icono, contentDescription = titulo, tint = Color(0xFF6C28D0))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(titulo, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
