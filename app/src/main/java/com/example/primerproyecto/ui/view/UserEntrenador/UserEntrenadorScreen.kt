
package com.example.primerproyecto.ui.view.UserEntrenador

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
fun UserEntrenadorScreen() { // ✅ Cambié el nombre para que coincida con el archivo
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                "Panel de Entrenador",
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
                    StatCard("Clientes", "8", Icons.Default.People, Color(0xFF6C28D0))
                }
                Box(modifier = Modifier.width(100.dp)) {
                    StatCard("Sesiones Hoy", "3", Icons.Default.FitnessCenter, Color(0xFF4CAF50))
                }
                Box(modifier = Modifier.width(100.dp)) {
                    StatCard("Ingresos", "$120", Icons.Default.AttachMoney, Color(0xFF2196F3))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sesiones de hoy
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Sesiones de Hoy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF6C28D0)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SesionItem("9:00 AM", "Firulais", "Entrenamiento básico")
                    SesionItem("11:00 AM", "Michi", "Socialización")
                    SesionItem("4:00 PM", "Max", "Obediencia avanzada")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Programas de entrenamiento
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Programas Activos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF6C28D0)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ProgramaItem("Firulais", "Obediencia Básica", "3/8 sesiones")
                    ProgramaItem("Luna", "Agility", "5/10 sesiones")
                    ProgramaItem("Rocky", "Corrección de Conducta", "2/6 sesiones")
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
                    ActionButton("Nueva Sesión", Icons.Default.Add)
                }
                Box(modifier = Modifier.width(100.dp)) {
                    ActionButton("Clientes", Icons.Default.Person)
                }
                Box(modifier = Modifier.width(100.dp)) {
                    ActionButton("Programas", Icons.Default.List)
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
fun SesionItem(hora: String, mascota: String, tipo: String) {
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
                Text(mascota, fontWeight = FontWeight.Bold)
                Text(tipo, fontSize = 14.sp, color = Color.Gray)
            }
            Text(hora, fontWeight = FontWeight.Medium, color = Color(0xFF6C28D0))
        }
    }
}

@Composable
fun ProgramaItem(mascota: String, programa: String, progreso: String) {
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
                Text(mascota, fontWeight = FontWeight.Bold)
                Text(programa, fontSize = 14.sp, color = Color.Gray)
            }
            Text(progreso, fontWeight = FontWeight.Medium, color = Color(0xFF4CAF50))
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
