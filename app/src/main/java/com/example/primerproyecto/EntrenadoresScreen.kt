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
fun EntrenadoresScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val entrenadores = listOf(
        Entrenador(
            nombre = "Carlos Ramírez",
            especialidades = listOf("Adiestramiento básico", "Conducta"),
            rating = 4.7,
            experiencia = "5 años de experiencia",
            descripcion = "Experto en comportamiento canino, especializado en perros adoptados.",
            imagenRes = R.drawable.pet_hotel2
        ),
        Entrenador(
            nombre = "María López",
            especialidades = listOf("Agility", "Obediencia avanzada"),
            rating = 4.9,
            experiencia = "8 años de experiencia",
            descripcion = "Entrenadora de alto rendimiento, enfocada en deportes y obediencia avanzada.",
            imagenRes = R.drawable.pet_hotel2
        )
    )

    val filtrados = entrenadores.filter { ent ->
        ent.nombre.contains(searchQuery, ignoreCase = true) ||
                ent.especialidades.any { tag -> tag.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar entrenador o especialidad...") },
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
            items(filtrados) { ent ->
                EntrenadorCard(entrenador = ent)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// ---------------- Modelo ----------------
data class Entrenador(
    val nombre: String,
    val especialidades: List<String>,
    val rating: Double,
    val experiencia: String,
    val descripcion: String,
    val imagenRes: Int
)

// ---------------- Tarjeta Entrenador ----------------
@Composable
fun EntrenadorCard(entrenador: Entrenador) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Imagen + rating
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = entrenador.imagenRes),
                    contentDescription = entrenador.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

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
                        Text("${entrenador.rating}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(entrenador.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(entrenador.experiencia, fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(6.dp))

                // Chips especialidades
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    entrenador.especialidades.forEach { esp ->
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

                Spacer(modifier = Modifier.height(8.dp))
                Text(entrenador.descripcion, fontSize = 14.sp, color = Color.DarkGray, maxLines = 3, overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Pets, contentDescription = "Agendar", tint = Color.White, modifier = Modifier.padding(end = 6.dp))
                    Text("Agendar Sesión", color = Color.White)
                }
            }
        }
    }

    if (showDialog) {
        EntrenadorFormDialog(entrenador = entrenador, onDismiss = { showDialog = false })
    }
}

// ---------------- Formulario de Agendamiento ----------------
@Composable
fun EntrenadorFormDialog(entrenador: Entrenador, onDismiss: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
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
            Text("Agendar con ${entrenador.nombre}", fontWeight = FontWeight.Bold)
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
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha preferida") },
                    singleLine = true,
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
