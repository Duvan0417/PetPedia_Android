package com.example.primerproyecto.ui.view.forum

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.Forum
import com.example.primerproyecto.data.model.ForumComment
import com.example.primerproyecto.ui.viewmodel.ForumViewModel
import com.example.primerproyecto.ui.viewmodel.ForumViewModelFactory

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    // Usar el factory para pasar el contexto
    val viewModel: ForumViewModel = viewModel(factory = ForumViewModelFactory(context))

    // ... el resto de tu código permanece igual ...
    var mostrarDialogoImagen by remember { mutableStateOf(false) }
    var imagenAmpliada by remember { mutableStateOf("") }
    var mostrarNuevaPublicacion by remember { mutableStateOf(false) }

    // Cargar posts al iniciar
    LaunchedEffect(Unit) {
        println("🎬 ForumScreen iniciado, cargando posts...")
        viewModel.loadPosts()
    }

    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    println("📊 Estado actual - Posts: ${posts.size}, Loading: $isLoading, Error: $errorMessage")

    if (mostrarDialogoImagen) {
        Dialog(onDismissRequest = { mostrarDialogoImagen = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Black, RoundedCornerShape(24.dp))
            ) {
                // Determinar si es imagen local o de API
                if (esImagenLocal(imagenAmpliada)) {
                    // Usar imagen local
                    val resourceId = getImagenLocalResourceId(imagenAmpliada)
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = "Imagen ampliada",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { mostrarDialogoImagen = false },
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // Usar imagen de API
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
                }

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
            onPublicar = { titulo, contenido, descripcion, imagen ->
                viewModel.createPost(titulo, contenido)
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

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = PurplePrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando publicaciones...")
                }
            }
        } else if (errorMessage != null) {
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
                        tint = ErrorColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Error: $errorMessage",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadPosts() },
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        } else if (posts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Forum,
                        contentDescription = "Foro vacío",
                        tint = TextSecondary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay publicaciones aún",
                        color = TextSecondary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Sé el primero en compartir algo",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { mostrarNuevaPublicacion = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear publicación",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Crear primera publicación")
                    }
                }
            }
        } else {
            // Lista de publicaciones con mejor espaciado
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(posts) { post ->
                    println("📝 Renderizando post: ${post.id} - ${post.title}")
                    TarjetaPublicacion(
                        post = post,
                        viewModel = viewModel,
                        onAmpliarImagen = { uri ->
                            imagenAmpliada = uri
                            mostrarDialogoImagen = true
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaPublicacion(
    post: Forum,
    viewModel: ForumViewModel,
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
                        text = post.user?.name ?: "Usuario",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = viewModel.formatRelativeTime(post.createdAt),
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Indicador de me gusta en header
                if (post.likesCount > 0) {
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
                                text = "${post.likesCount}",
                                color = PurplePrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Título de la publicación
            Text(
                text = post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleDark,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción si existe
            post.description?.let { description ->
                if (description.isNotBlank()) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Contenido de la publicación mejorado
            Text(
                text = post.content,
                fontSize = 15.sp,
                color = TextPrimary,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Imagen con mejor diseño
            post.image?.let { imagenUrl ->
                if (imagenUrl.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onAmpliarImagen(imagenUrl) }
                    ) {
                        // Determinar si es post de prueba
                        val esPostDePrueba = esPostDePrueba(post)

                        if (esPostDePrueba) {
                            // Usar imagen local para posts de prueba
                            val resourceId = getImagenLocalResourceId(post)
                            Image(
                                painter = painterResource(id = resourceId),
                                contentDescription = "Imagen de publicación",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Usar imagen de la API para posts reales
                            val imageUrl = buildImageUrl(imagenUrl)
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen de publicación",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Overlay para efecto al hacer click
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.02f))
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Estadísticas mejoradas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${post.likesCount} me gusta",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (post.commentsCount > 0) {
                    Text(
                        text = "${post.commentsCount} comentarios",
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
                    onClick = { viewModel.toggleLike(post.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = PurplePrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    border = ButtonDefaults.outlinedButtonBorder
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
                if (post.safeComments.isNotEmpty()) {
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

                        // ✅ Usar safeComments
                        post.safeComments.forEach { comentario ->
                            ComentarioItem(comentario = comentario, viewModel = viewModel)
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
                                    viewModel.addComment(post.id, textoComentario)
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
fun ComentarioItem(comentario: ForumComment, viewModel: ForumViewModel) {
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
                    // ✅ Usar userName en lugar de user.name
                    Text(
                        text = comentario.userName ?: comentario.user?.name ?: "Usuario",
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
                        text = viewModel.formatRelativeTime(comentario.createdAt),
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = comentario.content,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// Función para detectar si es un post de prueba
fun esPostDePrueba(post: Forum): Boolean {
    // IDs de los posts de prueba (1, 2, 3, 4)
    val idsPostsPrueba = listOf(1, 2, 3, 4)

    // Títulos de los posts de prueba (como backup)
    val titulosPostsPrueba = listOf(
        "Mi nueva mascota",
        "Consejos para adiestramiento",
        "Problemas de comportamiento",
        "Alimentación saludable"
    )

    return post.id in idsPostsPrueba ||
            post.title in titulosPostsPrueba
}

// Función para obtener ID de recurso local según el post
fun getImagenLocalResourceId(post: Forum): Int {
    return when (post.id) {
        1 -> R.drawable.foro1
        2 -> R.drawable.foro2
        3 -> R.drawable.foro3
        4 -> R.drawable.foro4
        else -> {
            // Si no coincide por ID, intentar por título
            when {
                post.title.contains("nueva mascota", ignoreCase = true) -> R.drawable.foro1
                post.title.contains("adiestramiento", ignoreCase = true) -> R.drawable.foro2
                post.title.contains("comportamiento", ignoreCase = true) -> R.drawable.foro3
                post.title.contains("alimentación", ignoreCase = true) -> R.drawable.foro4
                else -> R.drawable.ic_user_placeholder // Imagen por defecto
            }
        }
    }
}

// Función auxiliar para determinar si una imagen es local
fun esImagenLocal(imagenUrl: String): Boolean {
    return imagenUrl.contains("foro1") ||
            imagenUrl.contains("foro2") ||
            imagenUrl.contains("foro3") ||
            imagenUrl.contains("foro4")
}

// Función para obtener imagen local por URL (para el diálogo de imagen ampliada)
fun getImagenLocalResourceId(imagenUrl: String): Int {
    return when {
        imagenUrl.contains("foro1") -> R.drawable.foro1
        imagenUrl.contains("foro2") -> R.drawable.foro2
        imagenUrl.contains("foro3") -> R.drawable.foro3
        imagenUrl.contains("foro4") -> R.drawable.foro4
        else -> R.drawable.ic_user_placeholder
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaPublicacionDialog(
    onPublicar: (String, String, String?, String?) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
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

                // Campo de título
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    placeholder = {
                        Text(
                            "Título de la publicación",
                            color = TextSecondary,
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = true
                    ),
                    shape = RoundedCornerShape(16.dp),
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
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo de descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = {
                        Text(
                            "Descripción breve (opcional)",
                            color = TextSecondary,
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = true
                    ),
                    shape = RoundedCornerShape(16.dp),
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
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo de contenido
                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    placeholder = {
                        Text(
                            "¿Qué quieres compartir con la comunidad? 🐾",
                            color = TextSecondary,
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
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
                            if (titulo.isNotBlank() && contenido.isNotBlank()) {
                                onPublicar(
                                    titulo,
                                    contenido,
                                    if (descripcion.isNotBlank()) descripcion else null,
                                    imagenUri?.toString()
                                )
                            }
                        },
                        enabled = titulo.isNotBlank() && contenido.isNotBlank(),
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