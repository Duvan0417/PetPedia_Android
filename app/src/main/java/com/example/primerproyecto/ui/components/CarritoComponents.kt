package com.example.primerproyecto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.primerproyecto.CarritoItem
import com.example.primerproyecto.R

@Composable
fun CarritoDialog(
    carrito: List<CarritoItem>,
    onCerrar: () -> Unit,
    onEliminar: (CarritoItem) -> Unit,
    onActualizarCantidad: (CarritoItem, Int) -> Unit,
    onFinalizarCompra: () -> Unit,
    isLoading: Boolean = false
) {
    var mostrarMetodosPago by remember { mutableStateOf(false) }
    var compraFinalizada by remember { mutableStateOf(false) }

    if (mostrarMetodosPago) {
        MetodosPagoDialog(
            onSeleccionarTarjeta = {
                compraFinalizada = true
                mostrarMetodosPago = false
                onFinalizarCompra()
            },
            onCancelar = { mostrarMetodosPago = false }
        )
    } else if (compraFinalizada) {
        AlertDialog(
            onDismissRequest = {
                compraFinalizada = false
                onCerrar()
            },
            title = { Text("¡Compra Finalizada!") },
            text = { Text("Tu compra se ha realizado con éxito. Gracias por tu compra.") },
            confirmButton = {
                Button(
                    onClick = onFinalizarCompra,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading && carrito.isNotEmpty()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Finalizar Compra")
                    }
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onCerrar,
            confirmButton = {},
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFF6C28D0),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Carrito de Compras", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            },
            text = {
                if (carrito.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tu carrito está vacío", color = Color.Gray, fontSize = 14.sp)
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 300.dp)
                                .padding(bottom = 8.dp)
                        ) {
                            items(carrito) { item ->
                                ProductoCard(
                                    item = item,
                                    onEliminar = { onEliminar(item) },
                                    onActualizarCantidad = { nuevaCantidad ->
                                        onActualizarCantidad(item, nuevaCantidad)
                                    }
                                )
                            }
                        }

                        HorizontalDivider(thickness = 1.dp)
                        Spacer(modifier = Modifier.height(4.dp))

                        val total = carrito.sumOf { it.product.price * it.cantidad }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "$${String.format("%.2f", total)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF6C28D0)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { mostrarMetodosPago = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                        ) {
                            Text("Finalizar Compra", color = Color.White)
                        }
                        TextButton(
                            onClick = onCerrar,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar", color = Color.Gray)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ProductoCard(
    item: CarritoItem,
    onEliminar: () -> Unit,
    onActualizarCantidad: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto API
            val imageUrl = if (!item.product.image.isNullOrEmpty()) {
                if (item.product.image!!.startsWith("http")) item.product.image
                else "http://10.0.2.2:8000/storage/${item.product.image}"
            } else null

            AsyncImage(
                model = imageUrl,
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                error = androidx.compose.ui.res.painterResource(id = R.drawable.logopet)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, fontWeight = FontWeight.Bold)
                Text(
                    item.product.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (item.cantidad > 1) onActualizarCantidad(item.cantidad - 1)
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Menos", modifier = Modifier.size(16.dp))
                }
                Text("${item.cantidad}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(
                    onClick = { onActualizarCantidad(item.cantidad + 1) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Más", modifier = Modifier.size(16.dp))
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$${String.format("%.2f", item.product.price * item.cantidad)}",
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MetodosPagoDialog(
    onSeleccionarTarjeta: () -> Unit,
    onCancelar: () -> Unit
) {
    var tarjetaSeleccionada by remember { mutableStateOf("") }
    val tarjetas = listOf("Visa **** 1234", "Mastercard **** 5678", "Nueva tarjeta...")

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Seleccionar método de pago") },
        text = {
            Column {
                tarjetas.forEach { tarjeta ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = tarjetaSeleccionada == tarjeta,
                            onClick = { tarjetaSeleccionada = tarjeta }
                        )
                        Text(
                            text = tarjeta,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSeleccionarTarjeta()
                },
                enabled = tarjetaSeleccionada.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
            ) {
                Text("Seleccionar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}