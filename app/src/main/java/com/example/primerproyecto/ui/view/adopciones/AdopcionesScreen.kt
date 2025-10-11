package com.example.primerproyecto.ui.view.adopciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.Adoption
import com.example.primerproyecto.ui.viewmodel.AdoptionViewModel

@Composable
fun AdopcionesScreen() {
    val viewModel: AdoptionViewModel = viewModel()
    val adoptions by viewModel.adoptions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

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

    // Filtrar solo adopciones que tienen pet
    val adopcionesConMascota = adoptions.filter { it.pet != null }

    val mascotasFiltradas = adopcionesConMascota.filter { adoption ->
        adoption.pet?.name?.contains(searchQuery, ignoreCase = true) == true ||
                adoption.pet?.breed?.contains(searchQuery, ignoreCase = true) == true
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

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6C28D0))
            }
        } else if (adopcionesConMascota.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No se encontraron mascotas para adoptar", color = Color.Gray)
                    Text("La API no incluye datos de mascotas", color = Color.LightGray, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                items(mascotasFiltradas) { adoption ->
                    adoption.pet?.let { pet ->
                        MascotaCard(
                            adoption = adoption,
                            pet = pet,
                            onAdoptClick = { /* Lógica de adopción */ }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MascotaCard(adoption: Adoption, pet: com.example.primerproyecto.data.model.Pet, onAdoptClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            // Imagen principal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // Determinar si es mascota de prueba
                val esMascotaDePrueba = esMascotaDePrueba(pet)

                val painter = if (esMascotaDePrueba) {
                    // Usar imagen local para mascotas de prueba
                    getImagenLocalParaMascota(pet)
                } else {
                    // Usar imagen de la API para mascotas reales
                    val imageUrl = buildImageUrl(pet.image)
                    rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.adopcion3),
                        error = painterResource(R.drawable.adopcion3)
                    )
                }

                Image(
                    painter = painter,
                    contentDescription = pet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )

                // Badge de estado de adopción
                Surface(
                    tonalElevation = 6.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = when (adoption.status) {
                        "approved" -> Color(0xFF4CAF50)
                        "rejected" -> Color.Red
                        else -> Color(0xFFFF9800) // pending
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = when (adoption.status) {
                            "approved" -> "Aprobado"
                            "rejected" -> "Rechazado"
                            else -> "Pendiente"
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

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
                        pet.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (pet.sex.lowercase().contains("macho")) Icons.Default.Male else Icons.Default.Female,
                            contentDescription = "Género",
                            tint = if (pet.sex.lowercase().contains("macho")) Color(0xFF6C63FF) else Color(0xFFE91E63),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            pet.sex,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }

                Text("${pet.species} • ${pet.breed} • ${pet.age}", fontSize = 16.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Fila de iconos de información rápida
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    InfoIcon(icon = Icons.Default.Cake, label = pet.age)
                    InfoIcon(icon = Icons.Default.Height, label = "${pet.size} cm")
                    InfoIcon(icon = Icons.Default.Pets, label = pet.species)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción
                Text(
                    pet.description,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Información adicional opcional
                pet.vaccines?.let { vaccines ->
                    if (vaccines.isNotEmpty()) {
                        Text(
                            "Vacunas: ${vaccines.joinToString(", ")}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                pet.health?.let { health ->
                    Text(
                        "Estado de Salud: $health",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Default.Pets, contentDescription = "Adoptar", tint = Color.White, modifier = Modifier.padding(end = 6.dp))
                    Text("¡Adoptar a ${pet.name}!", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }

    // Formulario emergente
    if (showDialog) {
        AdopcionFormDialog(
            mascotaNombre = pet.name,
            onDismiss = { showDialog = false },
            onConfirm = { nombre, telefono, direccion, comentarios ->
                // Aquí iría la lógica para enviar la solicitud de adopción a la API
                showDialog = false
            }
        )
    }
}

@Composable
fun InfoIcon(icon: ImageVector, label: String) {
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

@Composable
fun AdopcionFormDialog(
    mascotaNombre: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotEmpty() && telefono.isNotEmpty()) {
                        onConfirm(nombre, telefono, direccion, comentarios)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
                enabled = nombre.isNotEmpty() && telefono.isNotEmpty()
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
            Text("Adoptar a $mascotaNombre", fontWeight = FontWeight.Bold)
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

// Función para detectar si es una mascota de prueba
fun esMascotaDePrueba(pet: com.example.primerproyecto.data.model.Pet): Boolean {
    // IDs de las mascotas de prueba (ajusta según tus datos)
    val idsMascotasPrueba = listOf(1, 2, 3)

    // Nombres de las mascotas de prueba (como backup)
    val nombresMascotasPrueba = listOf(
        "Simba", "Max", "Luna", "Bobby", "Molly"
    )

    // Razas de las mascotas de prueba
    val razasMascotasPrueba = listOf(
        "Siamés", "Bulldog Francés", "Labrador"
    )

    return pet.id in idsMascotasPrueba ||
            pet.name in nombresMascotasPrueba ||
            pet.breed in razasMascotasPrueba
}

// Función para obtener imagen local según la mascota
@Composable
fun getImagenLocalParaMascota(pet: com.example.primerproyecto.data.model.Pet): Painter {
    return when {
        pet.breed.contains("Siamés", ignoreCase = true) -> painterResource(R.drawable.adopcion1)
        pet.breed.contains("Bulldog Francés", ignoreCase = true) -> painterResource(R.drawable.adopcion3)
        pet.breed.contains("Labrador", ignoreCase = true) -> painterResource(R.drawable.adopcion4)
        else -> {
            // Si no coincide por raza, intentar por nombre
            when {
                pet.name.contains("Simba", ignoreCase = true) -> painterResource(R.drawable.adopcion1)
                pet.name.contains("Max", ignoreCase = true) -> painterResource(R.drawable.adopcion3)
                pet.name.contains("Luna", ignoreCase = true) -> painterResource(R.drawable.adopcion4)
                else -> painterResource(R.drawable.adopcion3) // Imagen por defecto
            }
        }
    }
}

// Función para construir URL de imagen
fun buildImageUrl(imagePath: String?): String? {
    return when {
        imagePath.isNullOrEmpty() -> null
        imagePath.startsWith("http") -> imagePath
        else -> "http://10.0.2.2:8000/storage/$imagePath"
    }
}