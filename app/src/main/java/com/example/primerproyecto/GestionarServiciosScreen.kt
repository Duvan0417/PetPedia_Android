package com.example.primerproyecto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
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

data class Servicio(
    val nombre: String,
    val descripcion: String,
    val proveedor: String, // "Veterinaria" o "Entrenador"
    val precio: String = "",
    val duracion: String = "",
    val disponible: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarServiciosScreen(onBack: () -> Unit) {
    val servicios = remember {
        mutableStateListOf(
            Servicio(
                nombre = "Consulta General",
                descripcion = "Revisión médica básica para tu mascota con examen completo",
                proveedor = "Veterinaria",
                precio = "$50.000",
                duracion = "30 min"
            ),
            Servicio(
                nombre = "Vacunación",
                descripcion = "Aplicación de vacunas necesarias según el calendario de vacunación",
                proveedor = "Veterinaria",
                precio = "$35.000",
                duracion = "15 min"
            ),
            Servicio(
                nombre = "Entrenamiento Básico",
                descripcion = "Órdenes de obediencia y socialización para cachorros y adultos",
                proveedor = "Entrenador",
                precio = "$80.000",
                duracion = "60 min"
            ),
            Servicio(
                nombre = "Grooming Completo",
                descripcion = "Baño, corte de pelo, limpieza de oídos y corte de uñas",
                proveedor = "Veterinaria",
                precio = "$45.000",
                duracion = "45 min",
                disponible = false
            )
        )
    }

    var showDialog by remember { mutableStateOf(false) }
    var servicioEditando by remember { mutableStateOf<Servicio?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val serviciosFiltrados = servicios.filter { servicio ->
        servicio.nombre.contains(searchQuery, ignoreCase = true) ||
                servicio.proveedor.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        // Header con gradiente
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF6C28D0),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Gestionar Servicios",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${servicios.size} servicios disponibles",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Barra de búsqueda
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar servicio...", color = Color.White.copy(alpha = 0.8f)) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        }

        // Estadísticas rápidas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Veterinaria",
                count = servicios.count { it.proveedor == "Veterinaria" },
                icon = Icons.Default.LocalHospital,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Entrenador",
                count = servicios.count { it.proveedor == "Entrenador" },
                icon = Icons.Default.Pets,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Activos",
                count = servicios.count { it.disponible },
                icon = Icons.Default.CheckCircle,
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
        }

        // Lista de servicios
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(serviciosFiltrados) { servicio ->
                ServicioCard(
                    servicio = servicio,
                    onEdit = {
                        servicioEditando = servicio
                        showDialog = true
                    },
                    onDelete = { servicios.remove(servicio) },
                    onToggleDisponibilidad = {
                        val index = servicios.indexOf(servicio)
                        if (index != -1) {
                            servicios[index] = servicio.copy(disponible = !servicio.disponible)
                        }
                    }
                )
            }

            // Espaciado final para el FAB
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    // Floating Action Button
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        ExtendedFloatingActionButton(
            text = { Text("Añadir Servicio", fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.Add, contentDescription = "Añadir") },
            onClick = {
                servicioEditando = null
                showDialog = true
            },
            modifier = Modifier.padding(16.dp),
            containerColor = Color(0xFF6C28D0),
            contentColor = Color.White
        )
    }

    // Diálogo para añadir/editar servicio
    if (showDialog) {
        ServicioDialog(
            servicio = servicioEditando,
            onDismiss = { showDialog = false },
            onConfirm = { nuevoServicio ->
                if (servicioEditando != null) {
                    val index = servicios.indexOf(servicioEditando!!)
                    if (index != -1) {
                        servicios[index] = nuevoServicio
                    }
                } else {
                    servicios.add(nuevoServicio)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ServicioCard(
    servicio: Servicio,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleDisponibilidad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Header con badge de proveedor
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (servicio.proveedor == "Veterinaria")
                            Color(0xFF4CAF50).copy(alpha = 0.1f)
                        else
                            Color(0xFF2196F3).copy(alpha = 0.1f)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (servicio.proveedor == "Veterinaria")
                            Color(0xFF4CAF50)
                        else
                            Color(0xFF2196F3)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (servicio.proveedor == "Veterinaria")
                                    Icons.Default.LocalHospital
                                else
                                    Icons.Default.Pets,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = servicio.proveedor,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (servicio.disponible)
                            Color(0xFF4CAF50).copy(alpha = 0.2f)
                        else
                            Color.Red.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (servicio.disponible) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (servicio.disponible) Color(0xFF4CAF50) else Color.Red,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (servicio.disponible) "Disponible" else "No disponible",
                                color = if (servicio.disponible) Color(0xFF4CAF50) else Color.Red,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Contenido principal
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = servicio.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = servicio.descripcion,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (servicio.precio.isNotEmpty() || servicio.duracion.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (servicio.precio.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = Color(0xFF6C28D0),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = servicio.precio,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF6C28D0)
                                )
                            }
                        }

                        if (servicio.duracion.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = servicio.duracion,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onToggleDisponibilidad,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (servicio.disponible) Color.Red else Color(0xFF4CAF50)
                        )
                    ) {
                        Icon(
                            if (servicio.disponible) Icons.Default.Block else Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (servicio.disponible) "Desactivar" else "Activar",
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", color = Color.White, fontSize = 12.sp)
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Red.copy(alpha = 0.1f)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.Red,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServicioDialog(
    servicio: Servicio?,
    onDismiss: () -> Unit,
    onConfirm: (Servicio) -> Unit
) {
    var nombre by remember { mutableStateOf(servicio?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(servicio?.descripcion ?: "") }
    var proveedor by remember { mutableStateOf(servicio?.proveedor ?: "Veterinaria") }
    var precio by remember { mutableStateOf(servicio?.precio ?: "") }
    var duracion by remember { mutableStateOf(servicio?.duracion ?: "") }
    var disponible by remember { mutableStateOf(servicio?.disponible ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        Servicio(
                            nombre = nombre,
                            descripcion = descripcion,
                            proveedor = proveedor,
                            precio = precio,
                            duracion = duracion,
                            disponible = disponible
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = {
            Text(
                text = if (servicio != null) "Editar Servicio" else "Nuevo Servicio",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del servicio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) }
                )

                OutlinedTextField(
                    value = duracion,
                    onValueChange = { duracion = it },
                    label = { Text("Duración (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) }
                )

                // Selector de proveedor
                Text("Tipo de proveedor:", fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = proveedor == "Veterinaria",
                            onClick = { proveedor = "Veterinaria" }
                        )
                        Icon(Icons.Default.LocalHospital, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Veterinaria")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = proveedor == "Entrenador",
                            onClick = { proveedor = "Entrenador" }
                        )
                        Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Entrenador")
                    }
                }

                // Switch de disponibilidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Servicio disponible:", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = disponible,
                        onCheckedChange = { disponible = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF6C28D0))
                    )
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}