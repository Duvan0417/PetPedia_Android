package com.example.primerproyecto


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ForumScreen(
    onPostClick: (Post) -> Unit,
    onGoToGroups: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    val posts = remember {
        listOf(
            Post(1, "Consejos para gatos", "Cuida a tu gato con estos tips...", "Ana López"),
            Post(2, "Alimentos para perros", "¿Qué comida es mejor?", "Carlos Pérez"),
            Post(3, "Juguetes para conejos", "Ideas de entretenimiento...", "Lucía Gómez")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foro de Mascotas") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onGoToGroups) {
                        Icon(Icons.Default.Group, contentDescription = "Grupos")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(posts) { post ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { onPostClick(post) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = post.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = post.content, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Autor: ${post.author}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
