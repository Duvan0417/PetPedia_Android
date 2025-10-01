package com.example.primerproyecto.ui.view.entrenadores

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.Trainer
import com.example.primerproyecto.ui.viewmodel.TrainerViewModel

@Composable
fun EntrenadoresScreen() {
    val viewModel: TrainerViewModel = viewModel()
    val trainers by viewModel.trainers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedTrainer by remember { mutableStateOf<Trainer?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val filtrados = if (searchQuery.isBlank()) {
        trainers
    } else {
        trainers.filter { trainer ->
            trainer.name.contains(searchQuery, ignoreCase = true) ||
                    trainer.specialty.contains(searchQuery, ignoreCase = true) ||
                    trainer.biography.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar entrenador o especialidad...") },
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
                        Icon(
                            Icons.Default.Error,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadTrainers() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                        ) {
                            Text("Reintentar")
                        }
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
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.PersonOff,
                            contentDescription = "Sin entrenadores",
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No se encontraron entrenadores",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (searchQuery.isNotBlank()) "Intenta con otros términos de búsqueda"
                            else "No hay entrenadores registrados",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
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
                        FlippingTrainerCard(
                            trainer = trainer,
                            onContactClick = {
                                selectedTrainer = trainer
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog para contactar
    if (showDialog && selectedTrainer != null) {
        ContactTrainerDialog(
            trainer = selectedTrainer!!,
            onDismiss = {
                showDialog = false
                selectedTrainer = null
            }
        )
    }
}

// ---------------- Tarjeta Entrenador que se voltea ----------------
@Composable
fun FlippingTrainerCard(
    trainer: Trainer,
    onContactClick: () -> Unit
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500), label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(480.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        if (rotation <= 90f) {
            // Front Side
            TrainerCardFront(
                trainer = trainer,
                onFlipClick = { isFlipped = !isFlipped },
                onContactClick = onContactClick
            )
        } else {
            // Back Side
            Box(
                modifier = Modifier.graphicsLayer { rotationY = 180f }
            ) {
                TrainerCardBack(
                    trainer = trainer,
                    onFlipClick = { isFlipped = !isFlipped },
                    onContactClick = onContactClick
                )
            }
        }
    }
}

@Composable
fun TrainerCardFront(
    trainer: Trainer,
    onFlipClick: () -> Unit,
    onContactClick: () -> Unit
) {
    Column {
        // Imagen + rating desde la API
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            // Imagen desde la API
            if (!trainer.image.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = trainer.image,
                        error = painterResource(id = R.drawable.ic_user_placeholder)
                    ),
                    contentDescription = trainer.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
            } else {
                // Imagen por defecto
                Image(
                    painter = painterResource(id = R.drawable.ic_user_placeholder),
                    contentDescription = trainer.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
            }

            // Rating desde la API
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
                        "${trainer.rating ?: 4.5}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre y experiencia
            Text(
                trainer.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E2E2E)
            )
            Text(
                "${trainer.experience} años de experiencia",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Especialidades desde la API
            val especialidades = trainer.specialty.split(",").map { it.trim() }
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                especialidades.forEach { esp ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF6C63FF),
                    ) {
                        Text(
                            esp,
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción desde la API
            Text(
                trainer.biography.take(120) + if (trainer.biography.length > 120) "..." else "",
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // Botón para voltear
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onFlipClick)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Flip, contentDescription = null, tint = Color(0xFF6C28D0), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Toca para ver más información",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6C28D0),
                )
            }

            // Botón de contacto
            Button(
                onClick = onContactClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.ContactPhone, contentDescription = "Contactar", tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contactar Entrenador", color = Color.White)
            }
        }
    }
}

@Composable
fun TrainerCardBack(
    trainer: Trainer,
    onFlipClick: () -> Unit,
    onContactClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val especialidades = trainer.specialty.split(",").map { it.trim() }
    val certificaciones = trainer.qualifications.split(",").map { it.trim() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Título
        Text(
            "Información Completa",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        Spacer(modifier = Modifier.height(12.dp))

        // Biografía completa desde la API
        Text(
            trainer.biography,
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // En TrainerCardBack - reemplaza TODO el FlowRow por:
        Text("Especialidades", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(especialidades) { esp ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF6C28D0).copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, Color(0xFF6C28D0).copy(alpha = 0.3f))
                ) {
                    Text(
                        esp,
                        color = Color(0xFF6C28D0),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Certificaciones desde la API
        Text("Certificaciones", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            certificaciones.forEach { cert ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(cert, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Información de contacto
        Text("Información de Contacto", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        ContactInfoItem(
            icon = Icons.Default.Phone,
            title = "Teléfono",
            value = trainer.phone
        )
        Spacer(modifier = Modifier.height(8.dp))
        ContactInfoItem(
            icon = Icons.Default.Email,
            title = "Email",
            value = trainer.email
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botones de acción
        Button(
            onClick = onContactClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.ContactPhone, contentDescription = "Contactar", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Contactar Ahora", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Toca para voltear",
            fontSize = 12.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onFlipClick)
        )
    }
}

// ---------------- Dialog de Contacto ----------------
@Composable
fun ContactTrainerDialog(
    trainer: Trainer,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Contactar a ${trainer.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Rating: ${trainer.rating ?: "No disponible"}", fontSize = 14.sp)
                }

                // Información de contacto
                ContactInfoItem(
                    icon = Icons.Default.Phone,
                    title = "Teléfono",
                    value = trainer.phone
                )

                ContactInfoItem(
                    icon = Icons.Default.Email,
                    title = "Email",
                    value = trainer.email
                )

                // Experiencia
                ContactInfoItem(
                    icon = Icons.Default.Work,
                    title = "Experiencia",
                    value = "${trainer.experience} años"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
            ) {
                Text("Cerrar")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ContactInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = Color(0xFF6C28D0),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                value,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}