package com.example.primerproyecto

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            bio = "Carlos es un apasionado del mundo canino, con un enfoque en el refuerzo positivo. Su misión es construir una relación de confianza y respeto entre mascotas y dueños. Ha trabajado en refugios, ayudando a perros con historiales difíciles a encontrar un hogar feliz.",
            certificaciones = listOf("Adiestrador Profesional", "Certificado en Conducta Canina"),
            servicios = listOf("Clases Individuales", "Home Visits", "Socialización"),
            filosofia = "El perro no es un problema a resolver, sino una mente a entender.",
            imagenRes = R.drawable.carlos
        ),
        Entrenador(
            nombre = "María López",
            especialidades = listOf("Agility", "Obediencia avanzada"),
            rating = 4.9,
            experiencia = "8 años de experiencia",
            descripcion = "Entrenadora de alto rendimiento, enfocada en deportes y obediencia avanzada.",
            bio = "María es una competidora de agility y ha ganado múltiples premios. Se especializa en la preparación de perros para competiciones, trabajando en la coordinación, velocidad y obediencia en entornos de alto estrés. Su experiencia abarca diversas razas y temperamentos.",
            certificaciones = listOf("Entrenadora de Agility Certificada", "Maestra en Obediencia Avanzada"),
            servicios = listOf("Entrenamiento para Competición", "Clases Grupales", "Talleres de Agility"),
            filosofia = "El éxito en el deporte canino comienza con un fuerte vínculo y confianza mutua.",
            imagenRes = R.drawable.maria
        ),
        Entrenador(
            nombre = "Ana Rodríguez",
            especialidades = listOf("Socialización", "Entrenamiento de cachorros"),
            rating = 4.5,
            experiencia = "3 años de experiencia",
            descripcion = "Ideal para socializar a tu cachorro y establecer bases sólidas.",
            bio = "Ana cree que la socialización temprana es clave para un perro feliz y equilibrado. Ofrece clases grupales y sesiones individuales para ayudar a tu cachorro a explorar el mundo de forma segura y confiada, previniendo problemas de conducta futuros.",
            certificaciones = listOf("Especialista en Cachorros", "Instructor de Clases de Socialización"),
            servicios = listOf("Clases de Cachorros", "Socialización Temprana", "Consultas de Comportamiento"),
            filosofia = "Invertir en el entrenamiento de un cachorro es la mejor inversión para su futuro.",
            imagenRes = R.drawable.ana
        ),
        Entrenador(
            nombre = "Javier Fernández",
            especialidades = listOf("Obediencia básica", "Trucos"),
            rating = 4.6,
            experiencia = "6 años de experiencia",
            descripcion = "Con un enfoque positivo, ayuda a tu mascota a aprender trucos y obediencia básica.",
            bio = "Javier es conocido por sus sesiones divertidas y efectivas. Utiliza el clicker training para enseñar obediencia básica de forma rápida y lúdica. Le encanta enseñar trucos nuevos y complejos que fortalecen el vínculo entre dueño y mascota.",
            certificaciones = listOf("Clicker Trainer Certificado", "Maestro en Adiestramiento Básico"),
            servicios = listOf("Obediencia Básica", "Trucos Caninos", "Home Visits"),
            filosofia = "Aprender debe ser un juego para ambos, no una tarea.",
            imagenRes = R.drawable.javier
        ),
        Entrenador(
            nombre = "Sofía Vargas",
            especialidades = listOf("Rehabilitación de miedos", "Terapia canina"),
            rating = 5.0,
            experiencia = "10 años de experiencia",
            descripcion = "Especialista en casos complejos de miedo y ansiedad en perros.",
            bio = "Sofía es una experta en modificación de conducta. Se dedica a la rehabilitación de perros con ansiedad por separación, fobias y agresividad por miedo. Su método se basa en la paciencia y el entendimiento profundo de la psicología canina.",
            certificaciones = listOf("Terapeuta Canina Certificada", "Especialista en Comportamiento Animal"),
            servicios = listOf("Terapia de Ansiedad", "Modificación de Conducta", "Clases de Relajación"),
            filosofia = "La paciencia y la comprensión son las herramientas más poderosas del adiestramiento.",
            imagenRes = R.drawable.sofia
        ),
        Entrenador(
            nombre = "Luis Castro",
            especialidades = listOf("Protección personal", "Manejo deportivo"),
            rating = 4.8,
            experiencia = "12 años de experiencia",
            descripcion = "Entrenador para perros de trabajo y deportes caninos de alta exigencia.",
            bio = "Luis tiene una vasta experiencia entrenando perros para tareas de seguridad, búsqueda y rescate. Su entrenamiento es riguroso y enfocado en la disciplina, ideal para razas que requieren un alto nivel de actividad mental y física.",
            certificaciones = listOf("Instructor de K-9", "Certificado en Protección y Búsqueda"),
            servicios = listOf("Obediencia para Perros de Trabajo", "Preparación para Competencias", "Entrenamiento de Defensa"),
            filosofia = "La disciplina y el propósito convierten a un perro en un compañero excepcional.",
            imagenRes = R.drawable.luis
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
                FlippingEntrenadorCard(entrenador = ent)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// ---------------- Modelo (Se agregaron más campos) ----------------
data class Entrenador(
    val nombre: String,
    val especialidades: List<String>,
    val rating: Double,
    val experiencia: String,
    val descripcion: String,
    val bio: String,
    val certificaciones: List<String>,
    val servicios: List<String>,
    val filosofia: String,
    val imagenRes: Int
)

// ---------------- Tarjeta Entrenador que se voltea ----------------
@Composable
fun FlippingEntrenadorCard(entrenador: Entrenador) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500), label = ""
    )
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(480.dp) // Aumenta la altura para el contenido extra
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        if (rotation <= 90f) {
            // Front Side
            EntrenadorCardFront(
                entrenador = entrenador,
                onFlipClick = { isFlipped = !isFlipped },
                onScheduleClick = { showDialog = true }
            )
        } else {
            // Back Side
            Box(
                modifier = Modifier.graphicsLayer { rotationY = 180f }
            ) {
                EntrenadorCardBack(
                    entrenador = entrenador,
                    onFlipClick = { isFlipped = !isFlipped },
                    onScheduleClick = { showDialog = true }
                )
            }
        }
    }

    if (showDialog) {
        EntrenadorFormDialog(entrenador = entrenador, onDismiss = { showDialog = false })
    }
}

@Composable
fun EntrenadorCardFront(
    entrenador: Entrenador,
    onFlipClick: () -> Unit,
    onScheduleClick: () -> Unit
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

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // Sección de 'Voltear' mejorada
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
                    "Toca para ver más",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6C28D0),
                )
            }

            // Nuevo botón para Agendar Cita
            Button(
                onClick = onScheduleClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Pets, contentDescription = "Agendar", tint = Color.White, modifier = Modifier.padding(end = 6.dp))
                Text("Agendar Cita Rápida", color = Color.White)
            }
        }
    }
}

@Composable
fun EntrenadorCardBack(
    entrenador: Entrenador,
    onFlipClick: () -> Unit,
    onScheduleClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Título y biografía
        Text("Acerca de ${entrenador.nombre}", fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(entrenador.bio, fontSize = 14.sp, color = Color.DarkGray, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Certificaciones
        Text("Certificaciones", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            entrenador.certificaciones.forEach { cert ->
                Text("• $cert", fontSize = 14.sp, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Servicios
        Text("Servicios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            entrenador.servicios.forEach { serv ->
                Text("• $serv", fontSize = 14.sp, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Filosofía
        Text("Filosofía de Entrenamiento", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "\"${entrenador.filosofia}\"",
            fontSize = 14.sp,
            color = Color.DarkGray,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Agendar
        Button(
            onClick = onScheduleClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Pets, contentDescription = "Agendar", tint = Color.White, modifier = Modifier.padding(end = 6.dp))
            Text("Agendar Sesión", color = Color.White)
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