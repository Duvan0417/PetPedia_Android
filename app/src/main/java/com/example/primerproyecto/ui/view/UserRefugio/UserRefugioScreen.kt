
package com.example.primerproyecto.ui.view.UserRefugio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserRefugioScreen() { // ✅ Cambié el nombre para que coincida con el archivo
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                "Panel de Refugio",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C28D0)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Estadísticas rápidas - SIN WEIGHT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier.width(100.dp)) {
                    StatCard("Mascotas", "15", Icons.Default.Pets, Color(0xFF6C28D0))
                }
                Box(modifier = Modifier.width(100.dp)) {
                    StatCard("Adopciones", "3", Icons.Default.Favorite, Color(0xFF4CAF50))
                }
                Box(modifier = Modifier.width(100.dp)) {
                    StatCard("Capacidad", "12/20", Icons.Default.Home, Color(0xFF2196F3))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mascotas recientes
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Mascotas Recientes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF6C28D0)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    MascotaItem("Luna", "Perro", "Labrador", "2 años")
                    MascotaItem("Simba", "Gato", "Siamés", "1 año")
                    MascotaItem("Rocky", "Perro", "Bulldog", "3 años")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Solicitudes de adopción
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Solicitudes Pendientes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF6C28D0)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SolicitudItem("Juan Pérez", "Luna", "En revisión")
                    SolicitudItem("María García", "Rocky", "Pendiente")
                    SolicitudItem("Carlos López", "Simba", "Entrevista programada")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Acciones rápidas - SIN WEIGHT
            Text(
                "Acciones Rápidas",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF6C28D0),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier.width(100.dp)) {
                    ActionButton("Nueva Mascota", Icons.Default.Add)
                }
                Box(modifier = Modifier.width(100.dp)) {
                    ActionButton("Adopciones", Icons.Default.Favorite)
                }
                Box(modifier = Modifier.width(100.dp)) {
                    ActionButton("Inventario", Icons.Default.Inventory)
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(), // ✅ SIN WEIGHT
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = color)
            Text(title, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun MascotaItem(nombre: String, especie: String, raza: String, edad: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Pets,
                contentDescription = "Mascota",
                tint = Color(0xFF6C28D0),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.fillMaxWidth(0.7f)) { // ✅ SIN WEIGHT
                Text(nombre, fontWeight = FontWeight.Bold)
                Text("$especie • $raza • $edad", fontSize = 14.sp, color = Color.Gray)
            }
            Text("Disponible", fontWeight = FontWeight.Medium, color = Color(0xFF4CAF50))
        }
    }
}

@Composable
fun SolicitudItem(solicitante: String, mascota: String, estado: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) { // ✅ SIN WEIGHT
                Text(solicitante, fontWeight = FontWeight.Bold)
                Text("Para: $mascota", fontSize = 14.sp, color = Color.Gray)
            }
            Text(
                estado,
                fontWeight = FontWeight.Medium,
                color = when(estado) {
                    "En revisión" -> Color(0xFFFF9800)
                    "Entrevista programada" -> Color(0xFF2196F3)
                    else -> Color(0xFF757575)
                }
            )
        }
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // ✅ SIN WEIGHT
            .height(80.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6C28D0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = text, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}
