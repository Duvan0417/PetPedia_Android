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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.Orders
import com.example.primerproyecto.data.model.OrderItems
import com.example.primerproyecto.data.model.Products
import com.example.primerproyecto.ui.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    onBack: () -> Unit,
    orderViewModel: OrderViewModel,
    userId: Int
) {
    // CARGAR PEDIDOS ESPECÍFICOS DEL USUARIO
    LaunchedEffect(userId) {
        if (userId > 0) {
            orderViewModel.loadOrdersByUser(userId)
        } else {
            orderViewModel.loadOrders()
        }
    }

    val orders by orderViewModel.orders.collectAsStateWithLifecycle()
    val orderItems by orderViewModel.orderItems.collectAsStateWithLifecycle()
    val isLoading by orderViewModel.isLoading.collectAsStateWithLifecycle()
    val error by orderViewModel.error.collectAsStateWithLifecycle()

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
                            onClick = {
                                if (userId > 0) {
                                    orderViewModel.loadOrdersByUser(userId)
                                } else {
                                    orderViewModel.loadOrders()
                                }
                            },
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
                            if (userId == 0) "Inicia sesión para ver tus pedidos" else "No tienes pedidos aún",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (userId == 0) "Los usuarios invitados no pueden ver el historial" else "Realiza tu primera compra en la tienda",
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
                            orderItems = orderViewModel.getOrderItemsForOrder(order.id)
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
    orderItems: List<OrderItems>
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    // Manejar diferentes formatos de fecha
    val formattedDate = remember(order.orderDate) {
        try {
            // Intentar diferentes formatos de fecha
            val formats = listOf(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            )

            var parsedDate: Date? = null
            for (format in formats) {
                try {
                    parsedDate = format.parse(order.orderDate)
                    if (parsedDate != null) break
                } catch (e: Exception) {
                    // Continuar con el siguiente formato
                }
            }

            if (parsedDate != null) {
                dateFormat.format(parsedDate)
            } else {
                order.orderDate // Devolver la fecha original si no se puede parsear
            }
        } catch (e: Exception) {
            order.orderDate
        }
    }

    val fechaEntregaEstimada = remember(order.orderDate) {
        try {
            val calendar = Calendar.getInstance()
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(order.orderDate)
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
            modifier = Modifier.padding(16.dp) // Reducido de 20dp a 16dp
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
                        fontSize = 16.sp, // Reducido de 18sp
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(2.dp)) // Reducido
                    Text(
                        "Realizado: $formattedDate",
                        fontSize = 11.sp, // Reducido de 12sp
                        color = Color.Gray
                    )
                }

                Badge(
                    containerColor = when(order.status.toLowerCase(Locale.ROOT)) {
                        "completed" -> Color(0xFF4CAF50)
                        "cancelled" -> Color(0xFFF44336)
                        else -> Color(0xFFFF9800) // pending
                    }
                ) {
                    Text(
                        when(order.status.toLowerCase(Locale.ROOT)) {
                            "pending" -> "Pendiente"
                            "completed" -> "Completado"
                            "cancelled" -> "Cancelado"
                            else -> order.status
                        },
                        color = Color.White,
                        fontSize = 10.sp, // Reducido de 11sp
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp)) // Reducido de 16dp

            // Productos del pedido
            if (orderItems.isNotEmpty()) {
                Text(
                    "Productos:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp, // Reducido de 14sp
                    color = Color(0xFF555555),
                    modifier = Modifier.padding(bottom = 6.dp) // Reducido
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp)) // Reducido de 12dp
                        .padding(8.dp), // Reducido de 12dp
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Reducido de 12dp
                ) {
                    orderItems.forEach { item -> // Mostrar todos los productos, no solo 3
                        OrderItemCard(item = item)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp)) // Reducido de 16dp
            }

            // Información de entrega y total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Entrega estimada:",
                        fontSize = 11.sp, // Reducido de 12sp
                        color = Color.Gray
                    )
                    Text(
                        fechaEntregaEstimada,
                        fontSize = 13.sp, // Reducido de 14sp
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6C28D0)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Total:",
                        fontSize = 13.sp, // Reducido de 14sp
                        color = Color.Gray
                    )
                    Text(
                        "$${"%.2f".format(order.totalAmount)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, // Reducido de 18sp
                        color = Color(0xFF6C28D0)
                    )
                }
            }

            // Barra de progreso de entrega (solo si está pendiente o completado)
            if (order.status.toLowerCase(Locale.ROOT) == "pending" ||
                order.status.toLowerCase(Locale.ROOT) == "completed") {

                Spacer(modifier = Modifier.height(12.dp)) // Reducido de 16dp

                val progress = if (order.status.toLowerCase(Locale.ROOT) == "completed") 1f else 0.3f
                val progressColor = if (order.status.toLowerCase(Locale.ROOT) == "completed")
                    Color(0xFF4CAF50) else Color(0xFF6C28D0)
                val progressText = if (order.status.toLowerCase(Locale.ROOT) == "completed")
                    "Pedido entregado" else "Procesando pedido..."
                val textColor = if (order.status.toLowerCase(Locale.ROOT) == "completed")
                    Color(0xFF4CAF50) else Color.Gray

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp) // Reducido de 6dp
                        .clip(RoundedCornerShape(2.dp)), // Reducido de 3dp
                    color = progressColor,
                    trackColor = Color(0xFFE0E0E0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    progressText,
                    fontSize = 11.sp, // Reducido de 12sp
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun OrderItemCard(item: OrderItems) { // ✅ CORREGIDO: Usar OrderItems directamente
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        val imageUrl = if (!item.product?.image.isNullOrEmpty()) {
            if (item.product!!.image!!.startsWith("http")) item.product.image
            else "http://10.0.2.2:8000/storage/${item.product.image}"
        } else null

        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = item.product?.name ?: "Producto ${item.productId}", // ✅ CORREGIDO: productId en lugar de product_id
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFEFEF)),
            error = painterResource(id = R.drawable.logopet) // ✅ CORREGIDO: painterResource
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Información del producto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.product?.name ?: "Producto ${item.productId}", // ✅ CORREGIDO: productId
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
            "$${"%.2f".format(item.price * item.quantity)}", // ✅ CORREGIDO: String.format -> "%.2f".format
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6C28D0)
        )
    }
}