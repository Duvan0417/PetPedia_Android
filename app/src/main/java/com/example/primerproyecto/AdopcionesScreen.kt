package com.example.primerproyecto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdopcionesScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val mascotas = listOf(
        MascotaAdopcion(
            nombre = "Luna",
            edad = "2 años",
            raza = "Labrador",
            genero = "Hembra",
            tamano = "Mediana",
            vacunas = listOf("Antirrábica", "Parvovirus"),
            descripcion = "Una perrita muy cariñosa y juguetona, ideal para familias.",
            imagenRes = R.drawable.adopcion2
        ),
        MascotaAdopcion(
            nombre = "Michi",
            edad = "1 año",
            raza = "Siames",
            genero = "Macho",
            tamano = "Pequeño",
            vacunas = listOf("Triple Felina"),
            descripcion = "Gatito curioso, perfecto para departamentos.",
            imagenRes = R.drawable.adopcion3
        ),
        MascotaAdopcion(
            nombre = "Rocky",
            edad = "3 años",
            raza = "Pastor Alemán",
            genero = "Macho",
            tamano = "Grande",
            vacunas = listOf("Antirrábica", "Moquillo"),
            descripcion = "Fuerte y protector, busca un hogar con espacio amplio.",
            imagenRes = R.drawable.adopcion
        )
    )

    val filtradas = mascotas.filter { pet ->
        pet.nombre.contains(searchQuery, ignoreCase = true) ||
                pet.raza.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar mascota...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            items(filtradas) { pet ->
                MascotaCard(pet)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// ---------------- Modelo ----------------
data class MascotaAdopcion(
    val nombre: String,
    val edad: String,
    val raza: String,
    val genero: String,
    val tamano: String,
    val vacunas: List<String>,
    val descripcion: String,
    val imagenRes: Int
)

// ---------------- Tarjeta Mascota ----------------
@Composable
fun MascotaCard(mascota: MascotaAdopcion) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Imagen principal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = mascota.imagenRes),
                    contentDescription = mascota.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(mascota.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${mascota.raza} • ${mascota.edad}", fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(8.dp))

                // Chips de información rápida
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoChip(label = mascota.genero)
                    InfoChip(label = mascota.tamano)
                    mascota.vacunas.forEach { vacuna ->
                        InfoChip(label = vacuna)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(mascota.descripcion, fontSize = 14.sp, color = Color.DarkGray, maxLines = 3, overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Pets, contentDescription = "Adoptar", tint = Color.White, modifier = Modifier.padding(end = 6.dp))
                    Text("Adoptar", color = Color.White)
                }
            }
        }
    }

    // --------- FORMULARIO EMERGENTE ---------
    if (showDialog) {
        AdopcionFormDialog(mascota = mascota, onDismiss = { showDialog = false })
    }
}

// ---------------- Chip de info ----------------
@Composable
fun InfoChip(label: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF6C63FF)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

// ---------------- Formulario de Adopción ----------------
@Composable
fun AdopcionFormDialog(mascota: MascotaAdopcion, onDismiss: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    // Aquí iría la lógica para enviar la solicitud
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
            ) {
                Text("Confirmar", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        },
        title = {
            Text("Adoptar a ${mascota.nombre}", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Tu nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono de contacto") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = comentarios,
                    onValueChange = { comentarios = it },
                    label = { Text("Comentarios adicionales") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
