package com.example.primerproyecto.ui.view.UserEntrenador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pending
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

@Composable
fun UserEntrenadorSolicitudesScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: TrainerViewModel = viewModel()
    val requests by viewModel.requests.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRequests()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (requests.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Pending,
                        contentDescription = "No requests",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Text("No hay solicitudes pendientes")
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(requests) { request ->
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

@Composable
fun RequestCard(
    request: Request,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (request.priority) {
                "urgent" -> Color(0xFFFFE6E6)
                "high" -> Color(0xFFFFF0E6)
                "medium" -> Color(0xFFFFF9E6)
                else -> Color(0xFFE6FFE6)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Solicitud #${request.id}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Prioridad: ${request.priority.uppercase()}")
            Text("Estado: ${request.application_status}")
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Rechazar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onAccept
                ) {
                    Text("Aceptar")
                }
            }
        }
    }
}
