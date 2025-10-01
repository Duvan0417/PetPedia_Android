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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.Orders
import com.example.primerproyecto.ui.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    onBack: () -> Unit
) {
    val viewModel: OrderViewModel = viewModel()
    val orders by viewModel.orders.collectAsState()
    val orderItems by viewModel.orderItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

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
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color(0xFFF7F7F7)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6C28D0))
                }
            }

            error != null -> {
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
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Error al cargar pedidos",
                            color = Color.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            error!!,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadOrders() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            orders.isEmpty() -> {
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
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color(0xFFF7F7F7))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(orders) { order ->
                        OrderCard(
                            order = order,
                            orderItems = viewModel.getOrderItemsForOrder(order.id)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Orders,
    orderItems: List<com.example.primerproyecto.data.model.OrderItems>
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val inputFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val formattedDate = try {
        val date = inputFormat.parse(order.order_date)
        dateFormat.format(date!!)
    } catch (e: Exception) {
        order.order_date
    }

    val fechaEntregaEstimada = remember {
        try {
            val calendar = Calendar.getInstance()
            val date = inputFormat.parse(order.order_date)
            calendar.time = date!!
            calendar.add(Calendar.DAY_OF_YEAR, 3)
            SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(calendar.time)
        } catch (e: Exception) {
            "Próximamente"
        }
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
                        "Pedido #${order.id}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Realizado: $formattedDate",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Badge(
                    containerColor = when(order.status) {
                        "completed" -> Color(0xFF4CAF50)
                        "cancelled" -> Color(0xFFF44336)
                        else -> Color(0xFFFF9800) // pending
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        when(order.status) {
                            "pending" -> "Pendiente"
                            "completed" -> "Completado"
                            "cancelled" -> "Cancelado"
                            else -> order.status
                        },
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Productos del pedido
            if (orderItems.isNotEmpty()) {
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
                    orderItems.take(3).forEach { item ->
                        OrderItemCard(item = item)
                    }

                    if (orderItems.size > 3) {
                        Text(
                            "+ ${orderItems.size - 3} productos más...",
                            fontSize = 12.sp,
                            color = Color(0xFF6C28D0),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

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
                        "$${String.format("%.2f", order.total_amount)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF6C28D0)
                    )
                }
            }

            // Barra de progreso de entrega
            if (order.status == "pending") {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = 0.3f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF6C28D0),
                    trackColor = Color(0xFFE0E0E0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Procesando pedido...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            } else if (order.status == "completed") {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = 1f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE0E0E0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Pedido entregado",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun OrderItemCard(item: com.example.primerproyecto.data.model.OrderItems) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        val imageUrl = if (!item.product?.image.isNullOrEmpty()) {
            if (item.product!!.image!!.startsWith("http")) item.product.image
            else "http://10.0.2.2:8000/storage/${item.product.image}"
        } else null

        AsyncImage(
            model = imageUrl,
            contentDescription = item.product?.name ?: "Producto",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFEFEF)),
            error = androidx.compose.ui.res.painterResource(id = R.drawable.logopet)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Información del producto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.product?.name ?: "Producto ${item.product_id}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                maxLines = 1
            )
            Text(
                "Cantidad: ${item.quantity}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Precio del producto
        Text(
            "$${String.format("%.2f", item.price * item.quantity)}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6C28D0)
        )
    }
}