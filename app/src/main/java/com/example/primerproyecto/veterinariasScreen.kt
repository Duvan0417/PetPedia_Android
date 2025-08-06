package com.example.primerproyecto

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelo de datos
data class Veterinaria(
    val nombre: String,
    val rating: Double,
    val distancia: String,
    val ubicacion: String,
    val etiquetas: List<String>,
    val descripcion: String,
    val estadoAbierto: Boolean,
    val horario: String
)

@Composable
fun VeterinariasScreen() {
    val veterinarias = listOf(
        Veterinaria(
            nombre = "AnimalCare Centro Veterinario",
            rating = 4.5,
            distancia = "1.2 km",
            ubicacion = "Sur",
            etiquetas = listOf("Vacunación", "Cardiología", "Peluquería", "Farmacia"),
            descripcion = "Centro con especialistas en medicina intensiva, diagnóstico avanzado y cuidados.",
            estadoAbierto = true,
            horario = "Lunes a Viernes: 8:00 - 20:00, Sábados: 9:00 - 14:00"
        ),
        Veterinaria(
            nombre = "VetLife Clínica Veterinaria",
            rating = 4.8,
            distancia = "2.5 km",
            ubicacion = "Norte",
            etiquetas = listOf("Consulta general", "Exóticos", "Dermatología"),
            descripcion = "Especialistas en mascotas exóticas y dermatología avanzada.",
            estadoAbierto = false,
            horario = "Lunes a Viernes: 9:00 - 18:00, Sábados: 10:00 - 14:00"
        ),
        Veterinaria(
            nombre = "AnimalCare Veterinaria",
            rating = 4.3,
            distancia = "3.0 km",
            ubicacion = "Centro",
            etiquetas = listOf("Vacunación", "Cardiología", "Cirugía"),
            descripcion = "Veterinaria con atención 24h y especialistas en cirugía avanzada.",
            estadoAbierto = true,
            horario = "Lunes a Domingo: 24 horas"
        ),
        Veterinaria(
            nombre = "PetSalud Integral",
            rating = 4.7,
            distancia = "0.9 km",
            ubicacion = "Occidente",
            etiquetas = listOf("Rayos X", "Vacunación", "Urgencias 24h"),
            descripcion = "Atención integral con urgencias y diagnóstico por imagen.",
            estadoAbierto = true,
            horario = "Todos los días: 7:00 - 22:00"
        ),
        Veterinaria(
            nombre = "Veterinaria Mi Mascota",
            rating = 4.2,
            distancia = "1.5 km",
            ubicacion = "Centro",
            etiquetas = listOf("Consulta general", "Peluquería", "Vacunación"),
            descripcion = "Clínica con ambiente amigable y precios accesibles.",
            estadoAbierto = true,
            horario = "Lunes a Sábado: 8:00 - 18:00"
        ),
        Veterinaria(
            nombre = "VetAndes",
            rating = 4.9,
            distancia = "2.1 km",
            ubicacion = "Norte",
            etiquetas = listOf("Especialistas", "Internación", "Ecografías"),
            descripcion = "Profesionales certificados y tecnología de punta.",
            estadoAbierto = false,
            horario = "Lunes a Viernes: 7:00 - 19:00"
        ),
        Veterinaria(
            nombre = "Centro Animal Plus",
            rating = 4.6,
            distancia = "2.8 km",
            ubicacion = "Sur",
            etiquetas = listOf("Cirugías", "Vacunación", "Dermatología"),
            descripcion = "Atención con especialistas y servicios quirúrgicos.",
            estadoAbierto = true,
            horario = "Lunes a Domingo: 8:00 - 22:00"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(8.dp)
    ) {
        items(veterinarias) { vet ->
            VeterinariaCard(vet)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun VeterinariaCard(vet: Veterinaria) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Imagen", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(vet.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text("${vet.ubicacion}, ${vet.distancia}", color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                vet.etiquetas.forEach {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(Color(0xFF6C63FF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                vet.descripcion,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            val estadoColor = if (vet.estadoAbierto) Color(0xFF4CAF50) else Color.Red
            val estadoTexto = if (vet.estadoAbierto) "Abierto ahora" else "Cerrado ahora"

            Text(
                text = estadoTexto,
                color = estadoColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(vet.horario, fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /* Acción de pedir cita */ }) {
                    Text("Pedir Cita")
                }

                Row {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorito")
                    }
                }
            }
        }
    }
}