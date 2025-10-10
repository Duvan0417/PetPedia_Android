package com.example.primerproyecto.ui.view.veterinarias

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariasScreen(
    modifier: Modifier = Modifier
) {
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
        vet.getClinicName().contains(searchQuery, ignoreCase = true) ||
                (vet.email?.contains(searchQuery, ignoreCase = true) == true) ||
                (vet.address?.contains(searchQuery, ignoreCase = true) == true) ||
                (vet.specialization?.contains(searchQuery, ignoreCase = true) == true)
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
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar veterinaria, especialidad, email...") },
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = "Sin resultados",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No se encontraron veterinarias", color = Color.Gray)
                    if (searchQuery.isNotEmpty()) {
                        Text("para: '$searchQuery'", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
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
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = veterinariaSeleccionada!!.getClinicName(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Especialización
                    veterinariaSeleccionada!!.specialization?.let { specialization ->
                        Text(
                            text = "🎯 $specialization",
                            fontSize = 14.sp,
                            color = Color(0xFF6C28D0),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Licencia veterinaria
                    veterinariaSeleccionada!!.veterinary_license?.let { license ->
                        Text(
                            text = "📋 Licencia: $license",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Información de contacto
                    veterinariaSeleccionada!!.email?.let { email ->
                        Text(
                            text = "📧 $email",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    veterinariaSeleccionada!!.phone?.let { phone ->
                        Text(
                            text = "📞 $phone",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    veterinariaSeleccionada!!.address?.let { address ->
                        Text(
                            text = "📍 $address",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

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
                    Text("Veterinaria: ${veterinariaSeleccionada?.getClinicName() ?: ""}", fontWeight = FontWeight.Bold)
                    veterinariaSeleccionada?.specialization?.let { specialization ->
                        Text("Especialidad: $specialization")
                    }
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

// ---------------- Tarjeta Veterinaria actualizada ----------------
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
                    null
                } else {
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
                    placeholder = painterResource(R.drawable.veterinary2),
                    error = painterResource(R.drawable.veterinary2)
                )

                Image(
                    painter = painter,
                    contentDescription = vet.getClinicName(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                // Badge de especialización
                vet.specialization?.let { specialization ->
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
                            Text(
                                text = specialization,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.widthIn(max = 120.dp)
                            )
                        }
                    }
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = vet.getClinicName(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Licencia veterinaria
                vet.veterinary_license?.let { license ->
                    Text(
                        text = "Licencia: $license",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Información de contacto
                vet.email?.let { email ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(email, color = Color.Gray, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                vet.phone?.let { phone ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(phone, color = Color.Gray, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                vet.address?.let { address ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(address, color = Color.Gray, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
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