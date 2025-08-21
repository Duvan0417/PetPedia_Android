@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.primerproyecto

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource

@Composable
fun PerfilScreen(onBack: () -> Unit) {
    // Estado de los datos
    var nombre by remember { mutableStateOf("Juan Pérez") }
    var correo by remember { mutableStateOf("juanperez@email.com") }
    var telefono by remember { mutableStateOf("+57 320 123 4567") }
    var direccion by remember { mutableStateOf("Calle 123 #45-67, Bogotá") }
    var mascotas by remember { mutableStateOf("2 (Max y Luna)") }

    var isEditing by remember { mutableStateOf(false) }

    // Estado de la imagen seleccionada (URI)
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para abrir la galería (GetContent)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // se recibe la uri (puede ser null si cancela)
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Avatar + botón para seleccionar imagen
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(8.dp)) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Avatar con iniciales
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6C28D0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = nombre.take(2).uppercase(),
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Botón pequeño para elegir imagen
                ExtendedFloatingActionButton(
                    onClick = { launcher.launch("image/*") },
                    containerColor = Color(0xFF6C28D0),
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Editar foto", tint = Color.White) },
                    text = {},
                    elevation = FloatingActionButtonDefaults.elevation(4.dp),
                    modifier = Modifier
                        .offset(x = (-8).dp, y = (-8).dp)
                        .size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre y correo (o campos editables)
            if (!isEditing) {
                Text(nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(correo, fontSize = 14.sp, color = Color.Gray)
            } else {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjetas/Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!isEditing) {
                    InfoCard(title = "Teléfono", value = telefono)
                    InfoCard(title = "Dirección", value = direccion)
                    InfoCard(title = "Mascotas", value = mascotas)
                } else {
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mascotas,
                        onValueChange = { mascotas = it },
                        label = { Text("Mascotas") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones acción: editar/guardar y eliminar foto
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        // si estaba en edición, al guardar cerramos el modo edición
                        if (isEditing) {
                            // aquí podrías persistir los cambios (ViewModel / repository)
                        }
                        isEditing = !isEditing
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                ) {
                    Text(if (isEditing) "Guardar" else "Editar", color = Color.White)
                }

                OutlinedButton(
                    onClick = { selectedImageUri = null }, // quitar foto y volver a iniciales
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Quitar foto")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}
