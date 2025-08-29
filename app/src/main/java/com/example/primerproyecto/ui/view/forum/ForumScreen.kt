package com.example.primerproyecto.ui.view.forum

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.util.UUID

// Colores foro - Mejorados
val PurplePrimary = Color(0xFF4F46E5)
val PurpleLight = Color(0xFF6366F1)
val PurpleDark = Color(0xFF3730A3)
val BackgroundLight = Color(0xFFF8FAFC)
val CardBackground = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF1F2937)
val TextSecondary = Color(0xFF6B7280)

val SuccessColor = Color(0xFF10B981)
val ErrorColor = Color(0xFFEF4444)

val WarningColor = Color(0xFFF59E0B)

// data clases
data class Publicacion(
    val id: String = UUID.randomUUID().toString(),
    val usuario: String,
    val contenido: String,
    val imagenUri: String? = null,
    val fecha: String,
    var meGusta: Int = 0,
    var comentarios: MutableList<Comentario> = mutableListOf(),
    var meGustaDado: Boolean = false
)

data class Comentario(
    val id: String = UUID.randomUUID().toString(),
    val usuario: String,
    val contenido: String,
    val fecha: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    onBack: () -> Unit = {}
) {
    var publicaciones by remember { mutableStateOf(listOf<Publicacion>()) }
    var mostrarDialogoImagen by remember { mutableStateOf(false) }
    var imagenAmpliada by remember { mutableStateOf("") }
    var mostrarNuevaPublicacion by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        publicaciones = listOf(
            Publicacion(
                usuario = "Ana Martínez",
                contenido = "Hoy llevé a mi perro Max al parque y descubrí un nuevo juego que le encanta: buscar la pelota entre los árboles. ¡Se divirtió muchísimo! ¿Alguien más tiene juegos recomendados para perros activos?",
                imagenUri = "https://images.unsplash.com/photo-1552053831-71594a27632d",
                fecha = "Hace 2 horas",
                meGusta = 24,
                comentarios = mutableListOf(
                    Comentario(
                        usuario = "Carlos López",
                        contenido = "A mi perro le encanta jugar al escondite con sus juguetes. ¡Es muy divertido!",
                        fecha = "Hace 1 hora"
                    ),
                    Comentario(
                        usuario = "María García",
                        contenido = "Prueba con puzzles de comida, a mi golden retriever le mantiene entretenido por horas.",
                        fecha = "Hace 45 min"
                    )
                )
            ),
            Publicacion(
                usuario = "Luis Fernández",
                contenido = "¿Alguien sabe de algún shampoo natural para gatos? Mi gato tiene la piel sensible y necesito algo suave.",
                fecha = "Hace 5 horas",
                imagenUri = "https://images.unsplash.com/photo-1533738363-b7f9aef128ce",
                meGusta = 15,
                comentarios = mutableListOf(
                    Comentario(
                        usuario = "Elena Torres",
                        contenido = "Yo uso uno de avena, es muy suave y a mi gato le va genial.",
                        fecha = "Hace 3 horas"
                    )
                )
            ),
            Publicacion(
                usuario = "Camilo Salazar",
                contenido = "¿Me pueden recomendar un alimento bueno para mi gato de 6 meses?.",
                fecha = "Hace 5 horas",
                imagenUri = "https://images.unsplash.com/photo-1592194996308-7b43878e84a6",
                meGusta = 15,
                comentarios = mutableListOf(
                    Comentario(
                        usuario = "Elena Torres",
                        contenido = "Yo uso uno de avena, es muy suave y a mi gato le va genial.",
                        fecha = "Hace 3 horas"
                    )
                )
            ),
            Publicacion(
                usuario = "Sofía Mendoza",
                contenido = "¡Mi gatito aprendió a usar su arenero hoy! 🎉 Estoy tan orgullosa",
                fecha = "Hace 1 hora",
                imagenUri = "https://images.unsplash.com/photo-1574144113081-9f3375ef788c",
                meGusta = 32
            )
        )
    }

    if (mostrarDialogoImagen) {
        Dialog(onDismissRequest = { mostrarDialogoImagen = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Black, RoundedCornerShape(24.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imagenAmpliada)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen ampliada",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { mostrarDialogoImagen = false },
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = { mostrarDialogoImagen = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            .padding(6.dp)
                    )
                }
            }
        }
    }

    if (mostrarNuevaPublicacion) {
        NuevaPublicacionDialog(
            onPublicar = { nuevaPublicacion ->
                publicaciones = listOf(nuevaPublicacion) + publicaciones
                mostrarNuevaPublicacion = false
            },
            onCancelar = { mostrarNuevaPublicacion = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Header mejorado
        Surface(
            color = PurplePrimary,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 12.dp,
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Foro de Mascotas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { mostrarNuevaPublicacion = true },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Nueva publicación",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Lista de publicaciones con mejor espaciado
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(publicaciones) { publicacion ->
                TarjetaPublicacion(
                    publicacion = publicacion,
                    onMeGusta = { id ->
                        publicaciones = publicaciones.map {
                            if (it.id == id) {
                                it.copy(
                                    meGusta = if (it.meGustaDado) it.meGusta - 1 else it.meGusta + 1,
                                    meGustaDado = !it.meGustaDado
                                )
                            } else it
                        }
                    },
                    onComentar = { id, comentario ->
                        publicaciones = publicaciones.map {
                            if (it.id == id) {
                                it.copy(comentarios = (it.comentarios + comentario).toMutableList())
                            } else it
                        }
                    },
                    onAmpliarImagen = { uri ->
                        imagenAmpliada = uri
                        mostrarDialogoImagen = true
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaPublicacion(
    publicacion: Publicacion,
    onMeGusta: (String) -> Unit,
    onComentar: (String, Comentario) -> Unit,
    onAmpliarImagen: (String) -> Unit
) {
    var mostrarComentarios by remember { mutableStateOf(false) }
    var textoComentario by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Header mejorado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar del usuario mejorado
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(PurpleLight.copy(alpha = 0.15f), CircleShape)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = PurplePrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = publicacion.usuario,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = publicacion.fecha,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Indicador de me gusta en header
                if (publicacion.meGusta > 0) {
                    Box(
                        modifier = Modifier
                            .background(PurpleLight.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Me gusta",
                                tint = PurplePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${publicacion.meGusta}",
                                color = PurplePrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contenido de la publicación mejorado
            Text(
                text = publicacion.contenido,
                fontSize = 15.sp,
                color = TextPrimary,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Imagen con mejor diseño
            publicacion.imagenUri?.let { uri ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onAmpliarImagen(uri) }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen de publicación",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Overlay para efecto al hacer click
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.02f))
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Estadísticas mejoradas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${publicacion.meGusta} me gusta",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (publicacion.comentarios.isNotEmpty()) {
                    Text(
                        text = "${publicacion.comentarios.size} comentarios",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Acciones mejoradas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Me gusta mejorado
                Button(
                    onClick = { onMeGusta(publicacion.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (publicacion.meGustaDado) PurplePrimary else Color.Transparent,
                        contentColor = if (publicacion.meGustaDado) Color.White else PurplePrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    border = if (!publicacion.meGustaDado) ButtonDefaults.outlinedButtonBorder else null
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Me gusta",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Me gusta",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Botón Comentar mejorado
                Button(
                    onClick = { mostrarComentarios = !mostrarComentarios },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (mostrarComentarios) PurpleLight.copy(alpha = 0.1f) else Color.Transparent,
                        contentColor = if (mostrarComentarios) PurplePrimary else TextSecondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    border = if (!mostrarComentarios) ButtonDefaults.outlinedButtonBorder else null
                ) {
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = "Comentar",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Comentar",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Sección de comentarios mejorada
            if (mostrarComentarios) {
                Spacer(modifier = Modifier.height(24.dp))

                // Indicador de sección
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(PurpleLight.copy(alpha = 0.2f))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Lista de comentarios
                if (publicacion.comentarios.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundLight, RoundedCornerShape(20.dp))
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "💬 Comentarios",
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        publicacion.comentarios.forEach { comentario ->
                            ComentarioItem(comentario = comentario)
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }

                // Formulario para comentarios mejorado
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "Deja tu comentario",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = textoComentario,
                            onValueChange = { textoComentario = it },
                            placeholder = { Text("Escribe algo...", color = TextSecondary) },
                            modifier = Modifier
                                .weight(1f)
                                .height(58.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = true
                            ),
                            shape = RoundedCornerShape(18.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = BackgroundLight,
                                focusedContainerColor = Color.White,
                                unfocusedTextColor = TextPrimary,
                                focusedTextColor = TextPrimary,
                                unfocusedPlaceholderColor = TextSecondary,
                                focusedPlaceholderColor = TextSecondary,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = PurplePrimary
                            ),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Create,
                                    contentDescription = "Comentar",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = {
                                if (textoComentario.isNotBlank()) {
                                    val nuevoComentario = Comentario(
                                        usuario = "Tú",
                                        contenido = textoComentario,
                                        fecha = "Ahora"
                                    )
                                    onComentar(publicacion.id, nuevoComentario)
                                    textoComentario = ""
                                }
                            },
                            modifier = Modifier
                                .size(58.dp)
                                .background(
                                    if (textoComentario.isNotBlank()) PurplePrimary else TextSecondary.copy(alpha = 0.3f),
                                    CircleShape
                                ),
                            enabled = textoComentario.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Enviar comentario",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComentarioItem(comentario: Comentario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(PurpleLight.copy(alpha = 0.15f), CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = PurplePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = comentario.usuario,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PurpleDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = comentario.fecha,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = comentario.contenido,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaPublicacionDialog(
    onPublicar: (Publicacion) -> Unit,
    onCancelar: () -> Unit
) {
    var textoPublicacion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
    }

    Dialog(onDismissRequest = onCancelar) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(
                modifier = Modifier.padding(28.dp)
            ) {
                // Header del diálogo mejorado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Nueva publicación",
                        tint = PurplePrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Crear Publicación",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurplePrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = onCancelar,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Campo de texto mejorado
                OutlinedTextField(
                    value = textoPublicacion,
                    onValueChange = { textoPublicacion = it },
                    placeholder = {
                        Text(
                            "¿Qué quieres compartir con la comunidad? 🐾",
                            color = TextSecondary,
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = true
                    ),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = BackgroundLight,
                        focusedContainerColor = Color.White,
                        unfocusedTextColor = TextPrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedPlaceholderColor = TextSecondary,
                        focusedPlaceholderColor = TextSecondary,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = PurplePrimary
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Selector de imagen mejorado
                Column {
                    Text(
                        text = "📸 Añadir imagen",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Button(
                        onClick = { launcher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PurpleLight.copy(alpha = 0.1f),
                            contentColor = PurplePrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Seleccionar imagen",
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Seleccionar imagen",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Vista previa de imagen mejorada
                imagenUri?.let { uri ->
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        Text(
                            text = "👀 Vista previa:",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(20.dp))
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen seleccionada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Botón flotante para eliminar
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                            ) {
                                IconButton(
                                    onClick = { imagenUri = null },
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar imagen",
                                        tint = ErrorColor,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Botones de acción mejorados
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onCancelar,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            "Cancelar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = {
                            if (textoPublicacion.isNotBlank()) {
                                val nuevaPublicacion = Publicacion(
                                    usuario = "Tú",
                                    contenido = textoPublicacion,
                                    imagenUri = imagenUri?.toString(),
                                    fecha = "Ahora"
                                )
                                onPublicar(nuevaPublicacion)
                            }
                        },
                        enabled = textoPublicacion.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PurplePrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(52.dp),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Publicar",
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Publicar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}