package com.example.primerproyecto.ui.view.solicitudes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

// Modelo de datos para las solicitudes
data class Solicitud(
    val id: Int,
    val tipo: TipoSolicitud,
    val titulo: String,
    val descripcion: String,
    val fecha: Date,
    val estado: EstadoSolicitud,
    val precio: Double? = null
)

enum class TipoSolicitud {
    ENTRENADOR,
    ADOPCION,
    SERVICIO,
    FORO
}

enum class EstadoSolicitud {
    PENDIENTE,
    EN_PROGRESO,
    COMPLETADA,
    CANCELADA
}

@Composable
fun SolicitudScreen(
    modifier: Modifier = Modifier
) {
    // Datos de ejemplo - En producción vendrían de un ViewModel
    var solicitudes by remember {
        mutableStateOf(
            listOf(
                Solicitud(
                    id = 1,
                    tipo = TipoSolicitud.ENTRENADOR,
                    titulo = "Adiestramiento Básico",
                    descripcion = "Entrenamiento para obediencia básica y socialización",
                    fecha = Date(),
                    estado = EstadoSolicitud.PENDIENTE,
                    precio = 50.0
                ),
                Solicitud(
                    id = 2,
                    tipo = TipoSolicitud.ADOPCION,
                    titulo = "Adopción de Golden Retriever",
                    descripcion = "Solicitud de adopción para perro de 2 años",
                    fecha = Date(System.currentTimeMillis() - 86400000),
                    estado = EstadoSolicitud.EN_PROGRESO
                ),
                Solicitud(
                    id = 3,
                    tipo = TipoSolicitud.ENTRENADOR,
                    titulo = "Modificación de Conducta",
                    descripcion = "Tratamiento para ansiedad por separación",
                    fecha = Date(System.currentTimeMillis() - 172800000),
                    estado = EstadoSolicitud.COMPLETADA,
                    precio = 75.0
                )
            )
        )
    }

    var filtroEstado by remember { mutableStateOf<EstadoSolicitud?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }

    val solicitudesFiltradas = if (filtroEstado == null) {
        solicitudes
    } else {
        solicitudes.filter { it.estado == filtroEstado }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF6C28D0),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            "Mis Solicitudes",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${solicitudesFiltradas.size} solicitudes",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    // Botón de filtro
                    Box {
                        IconButton(
                            onClick = { showFilterMenu = true },
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filtrar",
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Todas") },
                                onClick = {
                                    filtroEstado = null
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.List, contentDescription = null)
                                }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Pendientes") },
                                onClick = {
                                    filtroEstado = EstadoSolicitud.PENDIENTE
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Schedule, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("En Progreso") },
                                onClick = {
                                    filtroEstado = EstadoSolicitud.EN_PROGRESO
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Autorenew, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Completadas") },
                                onClick = {
                                    filtroEstado = EstadoSolicitud.COMPLETADA
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Canceladas") },
                                onClick = {
                                    filtroEstado = EstadoSolicitud.CANCELADA
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Cancel, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Chips de filtro
        if (filtroEstado != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = true,
                        onClick = { filtroEstado = null },
                        label = { Text(getFiltroNombre(filtroEstado!!)) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Quitar filtro",
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6C28D0).copy(alpha = 0.2f),
                            selectedLabelColor = Color(0xFF6C28D0)
                        )
                    )
                }
            }
        }

        // Lista de solicitudes
        if (solicitudesFiltradas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Inbox,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay solicitudes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        "Tus solicitudes aparecerán aquí",
                        fontSize = 14.sp,
                        color = Color.Gray.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(solicitudesFiltradas) { solicitud ->
                    SolicitudCard(
                        solicitud = solicitud,
                        onCancelar = { id ->
                            solicitudes = solicitudes.map {
                                if (it.id == id) it.copy(estado = EstadoSolicitud.CANCELADA)
                                else it
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SolicitudCard(
    solicitud: Solicitud,
    onCancelar: (Int) -> Unit
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con tipo y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono y tipo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = getTipoColor(solicitud.tipo).copy(alpha = 0.2f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            getTipoIcon(solicitud.tipo),
                            contentDescription = null,
                            tint = getTipoColor(solicitud.tipo),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        getTipoNombre(solicitud.tipo),
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Estado
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = getEstadoColor(solicitud.estado).copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(getEstadoColor(solicitud.estado))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            getEstadoNombre(solicitud.estado),
                            fontSize = 12.sp,
                            color = getEstadoColor(solicitud.estado),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Título
            Text(
                solicitud.titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E2E2E)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Descripción
            Text(
                solicitud.descripcion,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            // Ver más/menos
            if (solicitud.descripcion.length > 60) {
                TextButton(
                    onClick = { expanded = !expanded },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        if (expanded) "Ver menos" else "Ver más",
                        fontSize = 13.sp,
                        color = Color(0xFF6C28D0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            // Footer con fecha y precio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        formatearFecha(solicitud.fecha),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                solicitud.precio?.let { precio ->
                    Text(
                        "$$precio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6C28D0)
                    )
                }
            }

            // Botón de cancelar (solo si está pendiente o en progreso)
            if (solicitud.estado == EstadoSolicitud.PENDIENTE ||
                solicitud.estado == EstadoSolicitud.EN_PROGRESO) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE53935)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE53935))
                    )
                ) {
                    Icon(
                        Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cancelar Solicitud", fontSize = 14.sp)
                }
            }
        }
    }

    // Diálogo de confirmación de cancelación
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text(
                    "¿Cancelar solicitud?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Esta acción no se puede deshacer. ¿Estás seguro de que deseas cancelar esta solicitud?",
                    color = Color.Gray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCancelar(solicitud.id)
                        showCancelDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    )
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("No", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

// Funciones auxiliares
fun getTipoIcon(tipo: TipoSolicitud) = when (tipo) {
    TipoSolicitud.ENTRENADOR -> Icons.Default.FitnessCenter
    TipoSolicitud.ADOPCION -> Icons.Default.Pets
    TipoSolicitud.SERVICIO -> Icons.Default.Build
    TipoSolicitud.FORO -> Icons.Default.Forum
}

fun getTipoColor(tipo: TipoSolicitud) = when (tipo) {
    TipoSolicitud.ENTRENADOR -> Color(0xFF6C28D0)
    TipoSolicitud.ADOPCION -> Color(0xFFE91E63)
    TipoSolicitud.SERVICIO -> Color(0xFF2196F3)
    TipoSolicitud.FORO -> Color(0xFFFF9800)
}

fun getTipoNombre(tipo: TipoSolicitud) = when (tipo) {
    TipoSolicitud.ENTRENADOR -> "Entrenador"
    TipoSolicitud.ADOPCION -> "Adopción"
    TipoSolicitud.SERVICIO -> "Servicio"
    TipoSolicitud.FORO -> "Foro"
}

fun getEstadoColor(estado: EstadoSolicitud) = when (estado) {
    EstadoSolicitud.PENDIENTE -> Color(0xFFFFA726)
    EstadoSolicitud.EN_PROGRESO -> Color(0xFF42A5F5)
    EstadoSolicitud.COMPLETADA -> Color(0xFF66BB6A)
    EstadoSolicitud.CANCELADA -> Color(0xFFEF5350)
}

fun getEstadoNombre(estado: EstadoSolicitud) = when (estado) {
    EstadoSolicitud.PENDIENTE -> "Pendiente"
    EstadoSolicitud.EN_PROGRESO -> "En Progreso"
    EstadoSolicitud.COMPLETADA -> "Completada"
    EstadoSolicitud.CANCELADA -> "Cancelada"
}

fun getFiltroNombre(estado: EstadoSolicitud) = when (estado) {
    EstadoSolicitud.PENDIENTE -> "Pendientes"
    EstadoSolicitud.EN_PROGRESO -> "En Progreso"
    EstadoSolicitud.COMPLETADA -> "Completadas"
    EstadoSolicitud.CANCELADA -> "Canceladas"
}

fun formatearFecha(fecha: Date): String {
    val ahora = Date()
    val diff = ahora.time - fecha.time
    val dias = diff / (1000 * 60 * 60 * 24)

    return when {
        dias == 0L -> "Hoy"
        dias == 1L -> "Ayer"
        dias < 7 -> "Hace $dias días"
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha)
    }
}