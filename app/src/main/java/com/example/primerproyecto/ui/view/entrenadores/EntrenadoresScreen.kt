package com.example.primerproyecto.ui.view.entrenadores

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.Trainer
import com.example.primerproyecto.ui.viewmodel.TrainersViewModel

@Composable
fun EntrenadoresScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: TrainersViewModel = viewModel()
    val trainers by viewModel.trainers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filtrados = if (searchQuery.isBlank()) {
        trainers
    } else {
        trainers.filter { trainer ->
            trainer.specialty?.contains(searchQuery, ignoreCase = true) == true ||
                    trainer.qualifications?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar por especialidad o certificaciones...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6C28D0))
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Error al cargar entrenadores",
                            color = Color.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            error!!,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            filtrados.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontraron entrenadores",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filtrados) { trainer ->
                        TrainerCard(trainer = trainer)
                    }
                }
            }
        }
    }
}

@Composable
fun TrainerCard(trainer: Trainer) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Determinar si es entrenador de prueba
                val esEntrenadorDePrueba = esEntrenadorDePrueba(trainer)

                val painter = if (esEntrenadorDePrueba) {
                    // Usar imagen local para entrenadores de prueba
                    getImagenLocalParaEntrenador(trainer)
                } else {
                    // Usar imagen de la API para entrenadores reales
                    val imageUrl = buildImageUrl(trainer.image)
                    rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_user_placeholder),
                        error = painterResource(R.drawable.ic_user_placeholder)
                    )
                }

                Image(
                    painter = painter,
                    contentDescription = "Entrenador",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )

                // Rating
                Surface(
                    tonalElevation = 6.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF6C28D0),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${trainer.rating ?: 0.0}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(16.dp)) {
                // Especialidad
                trainer.specialty?.let { specialty ->
                    Text(
                        specialty,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E2E2E)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Experiencia
                trainer.experience_years?.let { experience ->
                    Text(
                        "$experience años de experiencia",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tarifa por hora
                trainer.hourly_rate?.let { rate ->
                    Text(
                        "Tarifa: $$rate por hora",
                        fontSize = 14.sp,
                        color = Color(0xFF6C28D0),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Certificaciones
                trainer.qualifications?.let { qualifications ->
                    if (qualifications.isNotEmpty()) {
                        Text(
                            "Certificaciones:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            qualifications,
                            fontSize = 13.sp,
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Reviews
                trainer.review_count?.let { reviews ->
                    if (reviews > 0) {
                        Text(
                            "$reviews reviews",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// Función para detectar si es un entrenador de prueba
fun esEntrenadorDePrueba(trainer: Trainer): Boolean {
    // IDs de los entrenadores de prueba (1, 2, 3, 4)
    val idsEntrenadoresPrueba = listOf(1, 2, 3, 4)

    // Especialidades de los entrenadores de prueba (como backup)
    val especialidadesPrueba = listOf(
        "Adiestramiento Básico",
        "Obediencia Avanzada",
        "Modificación de Conducta",
        "Agility"
    )

    return trainer.id in idsEntrenadoresPrueba ||
            trainer.specialty in especialidadesPrueba
}

// Función para obtener imagen local según el entrenador
@Composable
fun getImagenLocalParaEntrenador(trainer: Trainer): Painter {
    return when (trainer.id) {
        1 -> painterResource(R.drawable.entrenador1)
        2 -> painterResource(R.drawable.entrenador2)
        3 -> painterResource(R.drawable.entrenador3)
        4 -> painterResource(R.drawable.entrenador4)
        else -> {
            // Si no coincide por ID, intentar por especialidad
            when {
                trainer.specialty?.contains("Adiestramiento Básico") == true -> painterResource(R.drawable.entrenador1)
                trainer.specialty?.contains("Obediencia Avanzada") == true -> painterResource(R.drawable.entrenador2)
                trainer.specialty?.contains("Modificación de Conducta") == true -> painterResource(R.drawable.entrenador3)
                trainer.specialty?.contains("Agility") == true -> painterResource(R.drawable.entrenador4)
                else -> painterResource(R.drawable.ic_user_placeholder) // Imagen por defecto
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