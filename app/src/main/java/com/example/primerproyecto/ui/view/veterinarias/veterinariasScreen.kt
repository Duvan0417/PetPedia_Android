package com.example.primerproyecto.ui.view.veterinarias

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.Veterinarian
import com.example.primerproyecto.ui.viewmodel.VeterinarianViewModel
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariasScreen() {
    val viewModel: VeterinarianViewModel = viewModel()
    val veterinarians by viewModel.veterinarians.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var veterinariaSeleccionada by remember { mutableStateOf<Veterinarian?>(null) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var mostrarMensaje by remember { mutableStateOf(false) }

    // Lista de mascotas del usuario (ejemplo)
    val mascotas = listOf("Firulais", "Michi", "Max")

    val filtradas = veterinarians.filter { vet ->
        vet.name.contains(searchQuery, ignoreCase = true) ||
                vet.email.contains(searchQuery, ignoreCase = true) ||
                vet.address.contains(searchQuery, ignoreCase = true)
    }

    // Manejar errores
    error?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar veterinaria, email o dirección...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6C28D0))
            }
        } else if (filtradas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontraron veterinarias", color = Color.Gray)
            }
        } else {
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
                    Text(
                        text = veterinariaSeleccionada!!.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Información de contacto
                    Text(
                        text = "📧 ${veterinariaSeleccionada!!.email}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "📞 ${veterinariaSeleccionada!!.phone}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "📍 ${veterinariaSeleccionada!!.address}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    // ---------------- Servicios disponibles ----------------
                    var servicioSeleccionado by remember {
                        mutableStateOf("Consulta general")
                    }
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
                            listOf("Consulta general", "Vacunación", "Cirugía", "Urgencias", "Peluquería").forEach { servicio ->
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

                    // ---------------- Fecha y Hora ----------------
                    var fecha by remember { mutableStateOf("") }
                    var hora by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text("Fecha de la cita (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = hora,
                        onValueChange = { hora = it },
                        label = { Text("Hora de la cita (HH:MM)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ---------------- Botón Confirmar ----------------
                    Button(
                        onClick = {
                            // Validar campos
                            if (fecha.isNotEmpty() && hora.isNotEmpty()) {
                                mostrarFormulario = false
                                mostrarMensaje = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
                        enabled = fecha.isNotEmpty() && hora.isNotEmpty()
                    ) {
                        Text("Confirmar Cita", color = Color.White)
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
            text = {
                Column {
                    Text("Su cita ha sido solicitada exitosamente")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Veterinaria: ${veterinariaSeleccionada?.name ?: ""}", fontWeight = FontWeight.Bold)
                    Text("Pronto será contactado para confirmar la cita")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarMensaje = false
                    veterinariaSeleccionada = null
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

// ---------------- Tarjeta Veterinaria actualizada con datos reales ----------------
@Composable
fun VeterinariaCard(vet: Veterinarian, onPedirCita: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Imagen de la veterinaria
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val imageUrl = if (vet.image.isNullOrEmpty()) {
                    // Imagen por defecto si no hay imagen
                    null
                } else {
                    // Si la imagen es una URL completa o una ruta relativa
                    if (vet.image!!.startsWith("http")) {
                        vet.image
                    } else {
                        "http://127.0.0.1:8000/storage/${vet.image}"
                    }
                }

                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.veterinary),
                    error = painterResource(R.drawable.veterinary)
                )

                Image(
                    painter = painter,
                    contentDescription = vet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                // Badge de información
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
                        Icon(Icons.Default.MedicalServices, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Veterinaria", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = vet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Información de contacto
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(vet.email, color = Color.Gray, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(vet.phone, color = Color.Gray, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(vet.address, color = Color.Gray, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }

                Spacer(modifier = Modifier.height(8.dp))

                vet.schedules?.let { schedules ->
                    if (schedules.isNotEmpty()) {
                        Text("Horarios:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        val cleanSchedules = schedules
                            .replace("[", "")
                            .replace("]", "")
                            .replace("\"", "")
                            .split(",")
                            .joinToString("\n") { it.trim() }

                        Text(cleanSchedules, fontSize = 12.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Botón de acción
                Button(
                    onClick = onPedirCita,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                ) {
                    Text("Pedir Cita", color = Color.White)
                }

                // Botones adicionales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { /* Llamar */ }) {
                        Icon(Icons.Default.Phone, contentDescription = "Llamar", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Llamar")
                    }

                    TextButton(onClick = { /* Email */ }) {
                        Icon(Icons.Default.Email, contentDescription = "Email", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Email")
                    }

                    TextButton(onClick = { /* Compartir */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Compartir")
                    }
                }
            }
        }
    }
}