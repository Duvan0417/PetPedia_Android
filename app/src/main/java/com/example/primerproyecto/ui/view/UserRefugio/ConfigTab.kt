package com.example.primerproyecto.ui.view.UserRefugio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primerproyecto.data.model.Shelter

@Composable
fun ConfigTab(shelter: Shelter?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (shelter == null) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Cargando información del refugio...")
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = Color(0xFF6C28D0),
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Configuración",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6C28D0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Te encuentras en la configuración",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            "Información del Refugio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6C28D0)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ConfigInfoRow(
                            icon = Icons.Default.Home,
                            label = "Nombre",
                            value = shelter.shelter_name
                        )

                        ConfigInfoRow(
                            icon = Icons.Default.Person,
                            label = "Responsable",
                            value = shelter.responsible_person
                        )

                        ConfigInfoRow(
                            icon = Icons.Default.Info,
                            label = "Capacidad",
                            value = "${shelter.capacity} mascotas"
                        )

                        ConfigInfoRow(
                            icon = Icons.Default.Star,
                            label = "Calificación",
                            value = "${shelter.rating} (${shelter.review_count} reseñas)"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConfigInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFF6C28D0),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}