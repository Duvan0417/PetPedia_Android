package com.example.primerproyecto.ui.view.UserEntrenador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.primerproyecto.model.Service
import android.util.Log

@Composable
fun AddServiceDialog(
    trainerId: Int,
    onDismiss: () -> Unit,
    onConfirm: (Service) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Crear Nuevo Servicio",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del servicio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            price = it
                        }
                    },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix = { Text("$") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duración") },
                    placeholder = { Text("Ej: 1 hora") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank() &&
                                price.isNotBlank() &&
                                description.isNotBlank() &&
                                duration.isNotBlank()) {

                                val priceValue = price.toDoubleOrNull() ?: 0.0

                                val service = Service(
                                    id = null,
                                    name = name.trim(),
                                    price = priceValue,
                                    description = description.trim(),
                                    duration = duration.trim(),
                                    trainer_id = trainerId
                                )

                                Log.d("AddServiceDialog", "📤 Enviando servicio: $service")
                                Log.d("AddServiceDialog", "trainer_id: $trainerId")

                                onConfirm(service)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = name.isNotBlank() &&
                                price.isNotBlank() &&
                                description.isNotBlank() &&
                                duration.isNotBlank()
                    ) {
                        Text("Crear")
                    }
                }
            }
        }
    }
}

@Composable
fun EditServiceDialog(
    service: Service,
    onDismiss: () -> Unit,
    onConfirm: (Service) -> Unit
) {
    var name by remember { mutableStateOf(service.name) }
    var price by remember { mutableStateOf(service.price.toString()) }
    var description by remember { mutableStateOf(service.description) }
    var duration by remember { mutableStateOf(service.duration) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Editar Servicio",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del servicio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            price = it
                        }
                    },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix = { Text("$") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duración") },
                    placeholder = { Text("Ej: 1 hora") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank() &&
                                price.isNotBlank() &&
                                description.isNotBlank() &&
                                duration.isNotBlank()) {

                                val updatedService = service.copy(
                                    name = name.trim(),
                                    price = price.toDoubleOrNull() ?: 0.0,
                                    description = description.trim(),
                                    duration = duration.trim()
                                )
                                onConfirm(updatedService)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = name.isNotBlank() &&
                                price.isNotBlank() &&
                                description.isNotBlank() &&
                                duration.isNotBlank()
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}