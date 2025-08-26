package com.example.primerproyecto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Publicaciones", "Grupos")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foro de Mascotas") },
                actions = {
                    IconButton(onClick = { /* Acción de búsqueda */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )
        },
        floatingActionButton = {
            when (selectedTab) {
                0 -> FloatingActionButton(
                    onClick = { navController.navigate("createPost") }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva publicación")
                }
                1 -> FloatingActionButton(
                    onClick = { navController.navigate("createGroup") }
                ) {
                    Icon(Icons.Default.Group, contentDescription = "Crear grupo")
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            when (selectedTab) {
                0 -> PublicacionesTab(navController)
                1 -> GruposTab(navController)
            }
        }
    }
}

@Composable
fun PublicacionesTab(navController: NavController) {
    val publicaciones = remember {
        listOf(
            Publicacion(
                id = 1,
                usuario = "Ana García",
                titulo = "¿Cómo educar a un cachorro?",
                contenido = "Acabo de adoptar un cachorro de 2 meses y necesito consejos para educarlo...",
                likes = 15,
                comentarios = 8,
                tiempo = "hace 3 horas"
            ),
            Publicacion(
                id = 2,
                usuario = "Carlos López",
                titulo = "Alimentación para gatos senior",
                contenido = "Mi gato tiene 12 años y quería saber qué alimentación es la más adecuada...",
                likes = 22,
                comentarios = 12,
                tiempo = "hace 1 día"
            )
        )
    }

    LazyColumn {
        items(publicaciones) { publicacion ->
            TarjetaPublicacion(publicacion, navController)
            Divider()
        }
    }
}

@Composable
fun GruposTab(navController: NavController) {
    val grupos = remember {
        listOf(
            Grupo(
                id = 1,
                nombre = "Dueños de Golden Retriever",
                descripcion = "Comparte experiencias sobre esta maravillosa raza",
                miembros = 245,
                imagenUrl = null
            ),
            Grupo(
                id = 2,
                nombre = "Gatos en apartamentos",
                descripcion = "Consejos para tener gatos felices en espacios pequeños",
                miembros = 178,
                imagenUrl = null
            )
        )
    }

    LazyColumn {
        items(grupos) { grupo ->
            TarjetaGrupo(grupo, navController)
            Divider()
        }
    }
}

@Composable
fun TarjetaPublicacion(publicacion: Publicacion, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { navController.navigate("detallePublicacion/${publicacion.id}") }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = publicacion.titulo,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = publicacion.contenido,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Por: ${publicacion.usuario}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = publicacion.tiempo,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                IconText(Icons.Default.ThumbUp, "${publicacion.likes}")
                Spacer(modifier = Modifier.width(16.dp))
                IconText(Icons.Default.Comment, "${publicacion.comentarios}")
            }
        }
    }
}

@Composable
fun TarjetaGrupo(grupo: Grupo, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { navController.navigate("detalleGrupo/${grupo.id}") }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Icon(
                    Icons.Default.Group,
                    contentDescription = "Grupo",
                    modifier = Modifier.size(30.dp).align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = grupo.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = grupo.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${grupo.miembros} miembros",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Button(onClick = { /* Unirse al grupo */ }) {
                Text("Unirse")
            }
        }
    }
}

@Composable
fun IconText(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.labelSmall)
    }
}

data class Publicacion(
    val id: Int,
    val usuario: String,
    val titulo: String,
    val contenido: String,
    val likes: Int,
    val comentarios: Int,
    val tiempo: String
)

data class Grupo(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val miembros: Int,
    val imagenUrl: String?
)