package com.example.primerproyecto.ui.view.UserEntrenador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primerproyecto.model.Request
import com.example.primerproyecto.ui.viewmodel.TrainerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEntrenadorSolicitudesScreen(
    userId: Int,
    modifier: Modifier = Modifier
) {
    val viewModel: TrainerViewModel = viewModel()
    val requests by viewModel.requests.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar el trainer_id primero
    LaunchedEffect(userId) {
        viewModel.loadTrainerId(userId)
    }

    // Mostrar mensajes
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Solicitudes",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${requests.size} solicitud(es)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.AssignmentTurnedIn,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Filtros de estado (opcional)
            val pendingCount = requests.count { it.application_status == "pending" }
            val acceptedCount = requests.count { it.application_status == "accepted" }
            val rejectedCount = requests.count { it.application_status == "rejected" }

            if (requests.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusChip(
                        label = "Pendientes",
                        count = pendingCount,
                        color = Color(0xFFFFA726),
                        modifier = Modifier.weight(1f)
                    )
                    StatusChip(
                        label = "Aceptadas",
                        count = acceptedCount,
                        color = Color(0xFF66BB6A),
                        modifier = Modifier.weight(1f)
                    )
                    StatusChip(
                        label = "Rechazadas",
                        count = rejectedCount,
                        color = Color(0xFFEF5350),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Lista de solicitudes o mensaje vacío
            if (requests.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "No hay solicitudes",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = "Cuando recibas solicitudes aparecerán aquí",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = requests,
                        key = { it.id }
                    ) { request ->
                        RequestCard(
                            request = request,
                            onAccept = { viewModel.acceptRequest(request.id) },
                            onReject = { viewModel.rejectRequest(request.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}

@Composable
fun RequestCard(
    request: Request,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val isPending = request.application_status == "pending"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con ID y prioridad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Solicitud #${request.id}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                PriorityBadge(priority = request.priority)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información de la solicitud
            InfoRow(
                icon = Icons.Default.Person,
                label = "Usuario",
                value = "ID: ${request.user_id ?: "N/A"}"
            )

            if (request.adoption_id != null) {
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(
                    icon = Icons.Default.Pets,
                    label = "Adopción",
                    value = "ID: ${request.adoption_id}"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                icon = Icons.Default.CheckCircle,
                label = "Estado",
                value = getStatusText(request.application_status)
            )

            // Botones de acción solo si está pendiente
            if (isPending) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFEF5350)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rechazar")
                    }

                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF66BB6A)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aceptar")
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                StatusIndicator(status = request.application_status)
            }
        }
    }
}

@Composable
fun PriorityBadge(priority: String) {
    val (backgroundColor, textColor, icon) = when (priority.lowercase()) {
        "urgent" -> Triple(Color(0xFFEF5350), Color.White, Icons.Default.PriorityHigh)
        "high" -> Triple(Color(0xFFFFA726), Color.White, Icons.Default.ArrowUpward)
        "medium" -> Triple(Color(0xFFFFCA28), Color.Black, Icons.Default.Remove)
        else -> Triple(Color(0xFF66BB6A), Color.White, Icons.Default.ArrowDownward)
    }

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = textColor
            )
            Text(
                text = priority.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun StatusIndicator(status: String) {
    val (backgroundColor, textColor, icon, text) = when (status.lowercase()) {
        "accepted" -> Quadruple(
            Color(0xFF66BB6A).copy(alpha = 0.15f),
            Color(0xFF66BB6A),
            Icons.Default.CheckCircle,
            "Solicitud Aceptada"
        )
        "rejected" -> Quadruple(
            Color(0xFFEF5350).copy(alpha = 0.15f),
            Color(0xFFEF5350),
            Icons.Default.Cancel,
            "Solicitud Rechazada"
        )
        "finished" -> Quadruple(
            Color(0xFF42A5F5).copy(alpha = 0.15f),
            Color(0xFF42A5F5),
            Icons.Default.Done,
            "Finalizada"
        )
        else -> Quadruple(
            Color(0xFFFFA726).copy(alpha = 0.15f),
            Color(0xFFFFA726),
            Icons.Default.Schedule,
            "Pendiente"
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = textColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}

fun getStatusText(status: String): String {
    return when (status.lowercase()) {
        "pending" -> "⏳ Pendiente"
        "accepted" -> "✅ Aceptada"
        "rejected" -> "❌ Rechazada"
        "finished" -> "🏁 Finalizada"
        else -> status.capitalize()
    }
}

// Helper para Quadruple
data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)