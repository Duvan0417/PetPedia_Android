package com.example.primerproyecto.ui.view.veterinarias

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

// Colores personalizados
private val PrimaryPurple = Color(0xFF7C3AED)
private val SecondaryPurple = Color(0xFF9F67FF)
private val AccentOrange = Color(0xFFFF6B35)
private val AccentPink = Color(0xFFFF2E97)
private val LightBackground = Color(0xFFFAF8FF)
private val CardBackground = Color(0xFFFFFFFF)

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

    val mascotas = listOf("🐕 Firulais", "🐱 Michi", "🐶 Max", "🐈 Luna", "🐩 Rocky")

    val filtradas = veterinarians.filter { vet ->
        vet.getClinicName().contains(searchQuery, ignoreCase = true) ||
                (vet.email?.contains(searchQuery, ignoreCase = true) == true) ||
                (vet.address?.contains(searchQuery, ignoreCase = true) == true) ||
                (vet.specialization?.contains(searchQuery, ignoreCase = true) == true)
    }

    error?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("¡Ups! 🐾", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearError() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Entendido")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(PrimaryPurple, SecondaryPurple)
                        )
                    )
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🐾", fontSize = 28.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Encuentra tu",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp
                            )
                            Text(
                                "Veterinaria Ideal",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Barra de búsqueda mejorada
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar veterinaria...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryPurple)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = Color.Gray)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            // Contenido principal
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = PrimaryPurple,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Buscando las mejores veterinarias... 🔍",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
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
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text("😿", fontSize = 72.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No encontramos veterinarias",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        if (searchQuery.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "para: '$searchQuery'",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Intenta con otra búsqueda",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                // Indicador de resultados
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🏥 ${filtradas.size} veterinarias disponibles",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryPurple
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filtradas) { vet ->
                        VeterinariaCard(
                            vet = vet,
                            onPedirCita = {
                                veterinariaSeleccionada = vet
                                mostrarFormulario = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Formulario mejorado
    if (mostrarFormulario && veterinariaSeleccionada != null) {
        Dialog(onDismissRequest = { mostrarFormulario = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                tonalElevation = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header del formulario
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "📅 Agendar Cita",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            )
                            Text(
                                veterinariaSeleccionada!!.getClinicName(),
                                fontSize = 16.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(onClick = { mostrarFormulario = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    // Información de la veterinaria
                    veterinariaSeleccionada!!.specialization?.let { specialization ->
                        InfoChip(
                            icon = "🎯",
                            text = specialization,
                            backgroundColor = PrimaryPurple.copy(alpha = 0.1f)
                        )
                    }

                    veterinariaSeleccionada!!.veterinary_license?.let { license ->
                        InfoChip(
                            icon = "📋",
                            text = "Licencia: $license",
                            backgroundColor = AccentOrange.copy(alpha = 0.1f)
                        )
                    }

                    // Servicios
                    var servicioSeleccionado by remember { mutableStateOf("Consulta general") }
                    var expandedServicios by remember { mutableStateOf(false) }

                    Text("Servicio requerido", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    ExposedDropdownMenuBox(
                        expanded = expandedServicios,
                        onExpandedChange = { expandedServicios = !expandedServicios }
                    ) {
                        OutlinedTextField(
                            value = servicioSeleccionado,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedServicios) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedServicios,
                            onDismissRequest = { expandedServicios = false }
                        ) {
                            listOf(
                                "🩺 Consulta general",
                                "💉 Vacunación",
                                "⚕️ Cirugía",
                                "🚨 Urgencias",
                                "✂️ Peluquería"
                            ).forEach { servicio ->
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

                    // Mascota
                    var mascotaSeleccionada by remember { mutableStateOf(mascotas.first()) }
                    var expandedMascotas by remember { mutableStateOf(false) }

                    Text("Selecciona tu mascota", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    ExposedDropdownMenuBox(
                        expanded = expandedMascotas,
                        onExpandedChange = { expandedMascotas = !expandedMascotas }
                    ) {
                        OutlinedTextField(
                            value = mascotaSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMascotas) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMascotas,
                            onDismissRequest = { expandedMascotas = false }
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

                    // Fecha y Hora
                    var fecha by remember { mutableStateOf("") }
                    var hora by remember { mutableStateOf("") }

                    Text("Fecha y hora de la cita", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        placeholder = { Text("YYYY-MM-DD") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = PrimaryPurple) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = hora,
                        onValueChange = { hora = it },
                        placeholder = { Text("HH:MM") },
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = PrimaryPurple) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Botón confirmar
                    Button(
                        onClick = {
                            if (fecha.isNotEmpty() && hora.isNotEmpty()) {
                                mostrarFormulario = false
                                mostrarMensaje = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple,
                            disabledContainerColor = Color.LightGray
                        ),
                        enabled = fecha.isNotEmpty() && hora.isNotEmpty(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirmar Cita", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Mensaje de confirmación mejorado
    if (mostrarMensaje) {
        AlertDialog(
            onDismissRequest = { mostrarMensaje = false },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "¡Cita Agendada!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )
                }
            },
            text = {
                Column {
                    Text(
                        "Tu cita ha sido solicitada exitosamente",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = LightBackground),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("🏥 ${veterinariaSeleccionada?.getClinicName() ?: ""}", fontWeight = FontWeight.Bold)
                            veterinariaSeleccionada?.specialization?.let {
                                Text("🎯 $it", color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Pronto serás contactado para confirmar tu cita 📞",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarMensaje = false
                        veterinariaSeleccionada = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entendido", fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun InfoChip(icon: String, text: String, backgroundColor: Color) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun VeterinariaCard(vet: Veterinarian, onPedirCita: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Imagen con overlay gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val esVeterinariaDePrueba = esVeterinariaDePrueba(vet)
                val painter = if (esVeterinariaDePrueba) {
                    getImagenLocalParaVeterinaria(vet)
                } else {
                    val imageUrl = buildImageUrl(vet.image)
                    rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.veterinary4),
                        error = painterResource(R.drawable.veterinary4)
                    )
                }

                Image(
                    painter = painter,
                    contentDescription = vet.getClinicName(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )

                // Gradiente overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )

                // Badge de especialización mejorado
                vet.specialization?.let { specialization ->
                    Surface(
                        tonalElevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        color = PrimaryPurple,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = specialization,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Rating badge (simulado)
                Surface(
                    tonalElevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = AccentOrange,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "4.8",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Contenido de la tarjeta
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = vet.getClinicName(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                vet.veterinary_license?.let { license ->
                    Text(
                        text = "📋 Licencia: $license",
                        fontSize = 12.sp,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Información de contacto compacta
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    vet.address?.let { address ->
                        InfoRow(
                            icon = Icons.Outlined.LocationOn,
                            text = address,
                            iconColor = AccentOrange
                        )
                    }

                    vet.phone?.let { phone ->
                        InfoRow(
                            icon = Icons.Outlined.Phone,
                            text = phone,
                            iconColor = PrimaryPurple
                        )
                    }

                    vet.schedules?.let { schedules ->
                        if (schedules.isNotEmpty()) {
                            val cleanSchedules = schedules
                                .replace("[", "")
                                .replace("]", "")
                                .replace("\"", "")
                                .split(",")
                                .take(2)
                                .joinToString(", ") { it.trim() }

                            InfoRow(
                                icon = Icons.Outlined.Schedule,
                                text = cleanSchedules,
                                iconColor = AccentPink
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón principal mejorado
                Button(
                    onClick = onPedirCita,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agendar Cita", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // Botones de acción secundarios
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionButton(
                        icon = Icons.Outlined.Phone,
                        label = "Llamar",
                        onClick = { /* Llamar */ }
                    )
                    ActionButton(
                        icon = Icons.Outlined.Email,
                        label = "Email",
                        onClick = { /* Email */ }
                    )
                    ActionButton(
                        icon = Icons.Outlined.Share,
                        label = "Compartir",
                        onClick = { /* Compartir */ }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, iconColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(iconColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text,
            color = Color.DarkGray,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryPurple)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Función para detectar si es una veterinaria de prueba
fun esVeterinariaDePrueba(vet: Veterinarian): Boolean {
    val idsVeterinariasPrueba = listOf(1, 2, 3, 4)
    val nombresVeterinariasPrueba = listOf(
        "Clínica Veterinaria Central",
        "Hospital Animal San Francisco",
        "Veterinaria Pet Care",
        "Centro Médico Veterinario"
    )

    return vet.id in idsVeterinariasPrueba ||
            vet.getClinicName() in nombresVeterinariasPrueba
}

// Función para obtener imagen local según la veterinaria
@Composable
fun getImagenLocalParaVeterinaria(vet: Veterinarian): Painter {
    return when (vet.id) {
        1 -> painterResource(R.drawable.veterinary3)
        2 -> painterResource(R.drawable.veterinary4)
        3 -> painterResource(R.drawable.veterinary6)
        4 -> painterResource(R.drawable.veterinary7)
        else -> {
            when {
                vet.getClinicName().contains("Central") -> painterResource(R.drawable.veterinary3)
                vet.getClinicName().contains("San Francisco") -> painterResource(R.drawable.veterinary4)
                vet.getClinicName().contains("Pet Care") -> painterResource(R.drawable.veterinary6)
                vet.getClinicName().contains("Centro Médico") -> painterResource(R.drawable.veterinary7)
                else -> painterResource(R.drawable.veterinary4)
            }
        }
    }
}

// Función para construir URL de imagen
fun buildImageUrl(imagePath: String?): String? {
    return when {
        imagePath.isNullOrEmpty() -> null
        imagePath.startsWith("http") -> imagePath
        else -> "http://127.0.0.1:8000/storage/$imagePath"
    }
}