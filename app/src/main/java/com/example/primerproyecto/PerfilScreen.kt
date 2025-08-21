@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.primerproyecto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, // ✅ ahora es seguro
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Imagen circular de perfil
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("JP", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }

                FloatingActionButton(
                    onClick = { /* Cambiar foto */ },
                    containerColor = Color(0xFF6C28D0),
                    modifier = Modifier
                        .size(36.dp)
                        .offset(x = (-6).dp, y = (-6).dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar foto", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Juan Pérez", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("juanperez@email.com", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            // Tarjetas con info del usuario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(title = "Teléfono", value = "+57 320 123 4567")
                InfoCard(title = "Dirección", value = "Calle 123 #45-67, Bogotá")
                InfoCard(title = "Mascotas", value = "2 (Max y Luna)")
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { /* Editar perfil */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
            ) {
                Text("Editar Perfil", color = Color.White)
            }
        }
    }
}

@Composable
fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 14.sp, color = Color.Gray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}
