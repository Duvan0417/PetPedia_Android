package com.example.primerproyecto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariasScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var veterinariaSeleccionada by remember { mutableStateOf<Veterinaria?>(null) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var mostrarMensaje by remember { mutableStateOf(false) }

    // Lista de mascotas del usuario (ejemplo)
    val mascotas = listOf("Firulais", "Michi", "Max")

    // Lista de veterinarias de ejemplo (asegúrate de tener vet1, vet2 en drawable)
    val veterinarias = listOf(
        Veterinaria(
            nombre = "AnimalCare Centro Veterinario",
            rating = 4.5,
            distancia = "1.2 km",
            ubicacion = "Sur",
            etiquetas = listOf("Vacunación", "Cardiología", "Peluquería", "Farmacia"),
            descripcion = "Centro con especialistas en medicina intensiva, diagnóstico avanzado y cuidados.",
            estadoAbierto = true,
            horario = "Lun-Vie: 8:00 - 20:00",
            imagenRes = R.drawable.veterinary
        ),
        Veterinaria(
            nombre = "VetLife Clínica Veterinaria",
            rating = 4.8,
            distancia = "2.5 km",
            ubicacion = "Norte",
            etiquetas = listOf("Consulta general", "Exóticos", "Dermatología"),
            descripcion = "Especialistas en mascotas exóticas y dermatología avanzada.",
            estadoAbierto = false,
            horario = "Lun-Vie: 9:00 - 18:00",
            imagenRes = R.drawable.veterinary2
        )
        // agrega más si quieres...
    )

    val filtradas = veterinarias.filter { vet ->
        vet.nombre.contains(searchQuery, ignoreCase = true) ||
                vet.etiquetas.any { tag -> tag.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar veterinaria o servicio...") },
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
            items(filtradas) { vet ->
                VeterinariaCard(
                    vet = vet,
                    onPedirCita = {
                        veterinariaSeleccionada = vet
                        mostrarFormulario = true
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    // ---------------- Formulario emergente (Dialog) ----------------
    if (mostrarFormulario && veterinariaSeleccionada != null) {
        Dialog(onDismissRequest = { mostrarFormulario = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Título: nombre de la veterinaria (no editable)
                    Text(
                        text = veterinariaSeleccionada!!.nombre,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // ---------------- Servicio (Combo) ----------------
                    var servicioSeleccionado by remember { mutableStateOf(veterinariaSeleccionada!!.etiquetas.firstOrNull() ?: "") }
                    var expandedServicios by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = servicioSeleccionado,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Servicio") },
                            trailingIcon = {
                                IconButton(onClick = { expandedServicios = !expandedServicios }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expandedServicios,
                            onDismissRequest = { expandedServicios = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            veterinariaSeleccionada!!.etiquetas.forEach { servicio ->
                                DropdownMenuItem(
                                    text = { Text(servicio) },
                                    onClick = {
                                        servicioSeleccionado = servicio
                                        expandedServicios = false
                                    }
                                )
                            }
                        }
                    }

                    // ---------------- Mascota (Combo) ----------------
                    var mascotaSeleccionada by remember { mutableStateOf(mascotas.first()) }
                    var expandedMascotas by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = mascotaSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Mascota") },
                            trailingIcon = {
                                IconButton(onClick = { expandedMascotas = !expandedMascotas }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expandedMascotas,
                            onDismissRequest = { expandedMascotas = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            mascotas.forEach { mascota ->
                                DropdownMenuItem(
                                    text = { Text(mascota) },
                                    onClick = {
                                        mascotaSeleccionada = mascota
                                        expandedMascotas = false
                                    }
                                )
                            }
                        }
                    }

                    // ---------------- Hora ----------------
                    var hora by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = hora,
                        onValueChange = { hora = it },
                        label = { Text("Hora de la cita (HH:MM)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ---------------- Botón Confirmar ----------------
                    Button(
                        onClick = {
                            // aquí podrías validar/guardar / enviar al backend
                            mostrarFormulario = false
                            mostrarMensaje = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                    ) {
                        Text("Confirmar", color = Color.White)
                    }
                }
            }
        }
    }

    // ---------------- Mensaje de confirmación ----------------
    if (mostrarMensaje) {
        AlertDialog(
            onDismissRequest = { mostrarMensaje = false },
            title = { Text("Cita solicitada") },
            text = { Text("Su servicio está pendiente y pronto será aceptado") },
            confirmButton = {
                TextButton(onClick = { mostrarMensaje = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

// ---------------- Modelo ----------------
data class Veterinaria(
    val nombre: String,
    val rating: Double,
    val distancia: String,
    val ubicacion: String,
    val etiquetas: List<String>,
    val descripcion: String,
    val estadoAbierto: Boolean,
    val horario: String,
    val imagenRes: Int
)

// ---------------- Tarjeta Veterinaria (diseño completo) ----------------
@Composable
fun VeterinariaCard(vet: Veterinaria, onPedirCita: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Imagen con badge de rating
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = vet.imagenRes),
                    contentDescription = vet.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                // Badge de rating
                Surface(
                    tonalElevation = 6.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF6C28D0),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "${vet.rating}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = vet.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("${vet.ubicacion} • ${vet.distancia}", color = Color.Gray, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Chips con scroll horizontal (si hay muchas etiquetas)
                val scrollState = rememberScrollState()
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    vet.etiquetas.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF6C63FF),
                        ) {
                            Text(
                                text = tag,
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = vet.descripcion, fontSize = 14.sp, color = Color.DarkGray, maxLines = 3, overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(8.dp))

                // Estado y acciones
                val estadoColor = if (vet.estadoAbierto) Color(0xFF4CAF50) else Color.Red
                val estadoTexto = if (vet.estadoAbierto) "Abierto ahora" else "Cerrado ahora"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = estadoTexto, color = estadoColor, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = vet.horario, color = Color.Gray, fontSize = 12.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = onPedirCita,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                        ) {
                            Text("Pedir Cita", color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = { /* compartir */ }) {
                            Icon(Icons.Default.Share, contentDescription = "Compartir")
                        }
                        IconButton(onClick = { /* favorito */ }) {
                            Icon(Icons.Default.Pets, contentDescription = "Favorito")
                        }
                    }
                }
            }
        }
    }
}
