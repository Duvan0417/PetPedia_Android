package com.example.primerproyecto.ui.view.pedidos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primerproyecto.CarritoItem
import java.text.SimpleDateFormat
import java.util.*

data class Pedido(
    val id: String = UUID.randomUUID().toString(),
    val productos: List<CarritoItem>,
    val fecha: Date = Date(),
    val total: Double,
    val estado: String = "En proceso"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    pedidos: List<Pedido>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis Pedidos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6C28D0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        if (pedidos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF7F7F7)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = "Sin pedidos",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No tienes pedidos aún",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Realiza tu primera compra en la tienda",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF7F7F7))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pedidos) { pedido ->
                    PedidoCard(pedido = pedido)
                }
            }
        }
    }
}

@Composable
fun PedidoCard(pedido: Pedido) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault()) }
    val fechaEntregaEstimada = remember(pedido.fecha) {
        val calendar = Calendar.getInstance()
        calendar.time = pedido.fecha
        calendar.add(Calendar.DAY_OF_YEAR, 3) // 3 días para entrega
        SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(calendar.time)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Encabezado del pedido
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Pedido #${pedido.id.take(6).uppercase()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Realizado: ${dateFormat.format(pedido.fecha)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Badge(
                    containerColor = when(pedido.estado) {
                        "Completado" -> Color(0xFF4CAF50)
                        "Cancelado" -> Color(0xFFF44336)
                        "En camino" -> Color(0xFF2196F3)
                        else -> Color(0xFFFF9800)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        pedido.estado,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Productos del pedido
            Text(
                "Productos:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF555555),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pedido.productos.take(3).forEach { item ->
                    ProductoPedidoItem(item = item)
                }

                if (pedido.productos.size > 3) {
                    Text(
                        "+ ${pedido.productos.size - 3} productos más...",
                        fontSize = 12.sp,
                        color = Color(0xFF6C28D0),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información de entrega y total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Entrega estimada:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        fechaEntregaEstimada,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6C28D0)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Total:",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        "$${pedido.total}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF6C28D0)
                    )
                }
            }

            // Barra de progreso de entrega (opcional)
            if (pedido.estado == "En proceso" || pedido.estado == "En camino") {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = when(pedido.estado) {
                        "En proceso" -> 0.3f
                        "En camino" -> 0.7f
                        else -> 1f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF6C28D0),
                    trackColor = Color(0xFFE0E0E0)
                )
            }
        }
    }
}

@Composable
fun ProductoPedidoItem(item: CarritoItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp))
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = item.producto.imagenRes),
                contentDescription = item.producto.nombre,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Información del producto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.producto.nombre,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                maxLines = 1
            )
            Text(
                "Cantidad: ${item.cantidad}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Precio del producto
        Text(
            "$${item.producto.precio * item.cantidad}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6C28D0)
        )
    }
}

// Función de extensión para formatear el precio
fun Double.formatPrice(): String {
    return "%.0f".format(this)
}