package com.example.primerproyecto



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GroupsScreen(onBack: () -> Unit) {
    val groups = remember {
        mutableStateListOf(
            Group(1, "Cuidado de Perros", "Consejos para la salud y alimentación de perros"),
            Group(2, "Cuidado de Gatos", "Tips para mantener a tu gato feliz"),
            Group(3, "Animales Exóticos", "Discusión sobre especies poco comunes")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grupos de Mascotas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
            items(groups) { group ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(group.name, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(group.description, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            group.joined = !group.joined
                        }) {
                            Text(if (group.joined) "Unido" else "Unirse")
                        }
                    }
                }
            }
        }
    }
}
