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
            salud = "Desparasitada, esterilizada",
            descripcion = "Una perrita muy cariñosa y juguetona, ideal para familias con niños.",
            imagenRes = R.drawable.adopcion2
        ),
        MascotaAdopcion(
            nombre = "Michi",
            edad = "1 año",
            raza = "Siames",
            genero = "Macho",
            tamano = "Pequeño",
            vacunas = listOf("Triple Felina"),
            salud = "Saludable, sin historial médico relevante",
            descripcion = "Gatito curioso y muy sociable, perfecto para departamentos tranquilos.",
            imagenRes = R.drawable.adopcion3
        ),
        MascotaAdopcion(
            nombre = "Toby",
            edad = "3 años",
            raza = "Golden Retriever",
            genero = "Macho",
            tamano = "Grande",
            vacunas = listOf("Antirrábica", "Moquillo"),
            salud = "Vacunado al día, castrado",
            descripcion = "Toby es un perro muy enérgico, le encanta correr y jugar en el parque.",
            imagenRes = R.drawable.adopcion4
        ),
        MascotaAdopcion(
            nombre = "Kiara",
            edad = "6 meses",
            raza = "conejo",
            genero = "Hembra",
            tamano = "Mediana",
            vacunas = listOf("Parvovirus", "Moquillo"),
            salud = "Vacunas  completas",
            descripcion = " Ideal para familias activas.",
            imagenRes = R.drawable.adopcion5
        ),
        MascotaAdopcion(
            nombre = "Oliver",
            edad = "4 años",
            raza = "Gato Común",
            genero = "Macho",
            tamano = "Pequeño",
            vacunas = listOf("Triple Felina", "Leucemia"),
            salud = "Desparasitado, esterilizado",
            descripcion = "Oliver es un gato independiente, le gusta pasar tiempo a solas pero disfruta de los mimos.",
            imagenRes = R.drawable.adopcion6
        ),
        MascotaAdopcion(
            nombre = "Max",
            edad = "1 año",
            raza = "gato",
            genero = "Macho",
            tamano = "Pequeño",
            vacunas = listOf("Antirrábica", "Parvovirus"),
            salud = "Certificado de salud, microchip",
            descripcion = "Max es un gato divertido y bonachón. Le encanta dormir siestas y dar paseos cortos.",
            imagenRes = R.drawable.adopcion7
        ),

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
            label = { Text("Buscar por nombre o raza...") },
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ---------------- Modelo con nuevo campo de salud ----------------
data class MascotaAdopcion(
    val nombre: String,
    val edad: String,
    val raza: String,
    val genero: String,
    val tamano: String,
    val vacunas: List<String>,
    val salud: String,
    val descripcion: String,
    val imagenRes: Int
)

// ---------------- Tarjeta de Mascota mejorada ----------------
@Composable
fun MascotaCard(mascota: MascotaAdopcion) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            // Imagen principal
            Image(
                painter = painterResource(id = mascota.imagenRes),
                contentDescription = mascota.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        mascota.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (mascota.genero == "Macho") Icons.Default.Male else Icons.Default.Female,
                            contentDescription = "Género",
                            tint = if (mascota.genero == "Macho") Color(0xFF6C63FF) else Color(0xFFE91E63),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            mascota.genero,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
                Text("${mascota.raza} • ${mascota.edad}", fontSize = 16.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))

                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                Spacer(modifier = Modifier.height(12.dp))

                // Fila de iconos de información rápida
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    InfoIcon(icon = Icons.Default.Cake, label = mascota.edad)
                    InfoIcon(icon = Icons.Default.Height, label = mascota.tamano)
                    InfoIcon(icon = Icons.Default.MedicalServices, label = "Salud")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción y estado de salud
                Text(
                    mascota.descripcion,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Vacunas: ${mascota.vacunas.joinToString(", ")}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )

                Text(
                    "Estado de Salud: ${mascota.salud}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Default.Pets, contentDescription = "Adoptar", tint = Color.White, modifier = Modifier.padding(end = 6.dp))
                    Text("¡Adoptar a ${mascota.nombre}!", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }

    // --------- FORMULARIO EMERGENTE ---------
    if (showDialog) {
        AdopcionFormDialog(mascota = mascota, onDismiss = { showDialog = false })
    }
}

// ---------------- Chip de info mejorado ----------------
@Composable
fun InfoIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF6C28D0),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
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