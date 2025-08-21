package com.example.primerproyecto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MasOpcionesScreen(
    onNavigateToPerfil: () -> Unit,
    onNavigateToEntrenadores: () -> Unit,
    onNavigateToAdopciones: () -> Unit,
    onNavigateToGestionarServicios: () -> Unit // 👈 nuevo
) {
    val opciones = listOf(
        "Perfil" to Icons.Default.Person,
        "Entrenadores" to Icons.Default.FitnessCenter,
        "Adopciones" to Icons.Default.Pets,
        "Configuración" to Icons.Default.Settings,
        "Pedidos" to Icons.Default.Receipt,
        "Foro" to Icons.Default.Forum,
        "Solicitudes" to Icons.Default.MarkEmailUnread,
        "Gestionar Servicios" to Icons.Default.Build
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Encabezado
        Text(
            text = "Más Opciones",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6C28D0),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        opciones.forEach { (titulo, icono) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(2.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                onClick = {
                    when (titulo) {
                        "Perfil" -> onNavigateToPerfil()
                        "Entrenadores" -> onNavigateToEntrenadores()
                        "Adopciones" -> onNavigateToAdopciones()
                        "Gestionar Servicios" -> onNavigateToGestionarServicios() // 👈 nuevo
                    }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFFEDE7F6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icono,
                            contentDescription = titulo,
                            tint = Color(0xFF6C28D0),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.ArrowForwardIos,
                        contentDescription = "Ir a $titulo",
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
