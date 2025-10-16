package com.example.primerproyecto.ui.view.entrenadores

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
        // Search bar mejorado
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 2.dp,
            color = Color.White
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar por especialidad o certificaciones...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF6C28D0)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Limpiar",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C28D0),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
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
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            // Imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                val esEntrenadorDePrueba = esEntrenadorDePrueba(trainer)

                val painter = if (esEntrenadorDePrueba) {
                    getImagenLocalParaEntrenador(trainer)
                } else {
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
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
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
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${trainer.rating ?: 0.0}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(20.dp)) {
                // Especialidad
                trainer.specialty?.let { specialty ->
                    Text(
                        specialty,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E2E2E)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Experiencia y Reviews en fila
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    trainer.experience_years?.let { experience ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.WorkHistory,
                                contentDescription = null,
                                tint = Color(0xFF6C28D0),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "$experience años de experiencia",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Reviews
                trainer.review_count?.let { reviews ->
                    if (reviews > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.RateReview,
                                contentDescription = null,
                                tint = Color(0xFF6C28D0),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "$reviews reviews",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tarifa
                trainer.hourly_rate?.let { rate ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF6C28D0).copy(alpha = 0.1f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = Color(0xFF6C28D0),
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "$$rate por hora",
                            fontSize = 16.sp,
                            color = Color(0xFF6C28D0),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Certificaciones
                trainer.qualifications?.let { qualifications ->
                    if (qualifications.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = Color(0xFF6C28D0),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    "Certificaciones",
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
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de solicitud
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C28D0)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Solicitar Servicio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF6C28D0).copy(alpha = 0.1f),
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF6C28D0),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            },
            title = {
                Text(
                    "Confirmar Solicitud",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "¿Deseas solicitar el servicio de:",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    trainer.specialty?.let { specialty ->
                        Text(
                            specialty,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6C28D0),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    trainer.hourly_rate?.let { rate ->
                        Text(
                            "Tarifa: $$rate/hora",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        // Aquí iría la lógica para solicitar el servicio
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C28D0)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

fun esEntrenadorDePrueba(trainer: Trainer): Boolean {
    val idsEntrenadoresPrueba = listOf(1, 2, 3, 4)
    val especialidadesPrueba = listOf(
        "Adiestramiento Básico",
        "Obediencia Avanzada",
        "Modificación de Conducta",
        "Agility"
    )

    return trainer.id in idsEntrenadoresPrueba ||
            trainer.specialty in especialidadesPrueba
}

@Composable
fun getImagenLocalParaEntrenador(trainer: Trainer): Painter {
    return when (trainer.id) {
        1 -> painterResource(R.drawable.entrenador1)
        2 -> painterResource(R.drawable.entrenador2)
        3 -> painterResource(R.drawable.entrenador3)
        4 -> painterResource(R.drawable.entrenador4)
        else -> {
            when {
                trainer.specialty?.contains("Adiestramiento Básico") == true -> painterResource(R.drawable.entrenador1)
                trainer.specialty?.contains("Obediencia Avanzada") == true -> painterResource(R.drawable.entrenador2)
                trainer.specialty?.contains("Modificación de Conducta") == true -> painterResource(R.drawable.entrenador3)
                trainer.specialty?.contains("Agility") == true -> painterResource(R.drawable.entrenador4)
                else -> painterResource(R.drawable.ic_user_placeholder)
            }
        }
    }
}

fun buildImageUrl(imagePath: String?): String? {
    return when {
        imagePath.isNullOrEmpty() -> null
        imagePath.startsWith("http") -> imagePath
        else -> "http://127.0.0.1:8000/storage/$imagePath"
    }
}