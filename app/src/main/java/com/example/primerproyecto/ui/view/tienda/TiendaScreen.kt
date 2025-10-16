package com.example.primerproyecto.ui.view.tienda

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.primerproyecto.CarritoItem
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.Products
import com.example.primerproyecto.ui.viewmodel.ProductsViewModel

// Colores personalizados
private val PrimaryPurple = Color(0xFF7C3AED)
private val SecondaryPurple = Color(0xFF9F67FF)
private val AccentOrange = Color(0xFFFF6B35)
private val AccentPink = Color(0xFFFF2E97)
private val AccentYellow = Color(0xFFFFC107)
private val LightBackground = Color(0xFFFAF8FF)
private val CardBackground = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaScreen(
    carrito: MutableList<CarritoItem>,
    onAgregar: (Products) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ProductsViewModel = viewModel()
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var busqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var mostrarMensaje by remember { mutableStateOf(false) }

    // Categorías con emojis
    val categorias = listOf(
        Categoria("Todos", R.drawable.logopet, "🏠"),
        Categoria("Perros", R.drawable.perro, "🐕"),
        Categoria("Gatos", R.drawable.cat, "🐱"),
        Categoria("Juguetes", R.drawable.juguetespe, "🎾"),
        Categoria("Comida", R.drawable.comida, "🍖"),
        Categoria("Accesorios", R.drawable.accesorios, "🎀")
    )

    val productosFiltrados = products.filter { product ->
        (categoriaSeleccionada == "Todos" || product.name.contains(categoriaSeleccionada, ignoreCase = true)) &&
                product.name.contains(busqueda, ignoreCase = true)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBackground)
        ) {
            // Header con gradiente y carrito
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(PrimaryPurple, SecondaryPurple)
                        )
                    )
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    // Título y carrito
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🛍️", fontSize = 28.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Pet Shop",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 16.sp
                                )
                                Text(
                                    "¡Todo para tu mascota!",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Badge del carrito
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                .clickable { /* Navegar al carrito */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            if (carrito.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .align(Alignment.TopEnd)
                                        .background(AccentOrange, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        carrito.size.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Barra de búsqueda mejorada
                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = { busqueda = it },
                        placeholder = { Text("Buscar productos mágicos... ✨") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryPurple)
                        },
                        trailingIcon = {
                            if (busqueda.isNotEmpty()) {
                                IconButton(onClick = { busqueda = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = Color.Gray)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            // Manejo de estados
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = PrimaryPurple,
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Preparando productos increíbles... 🎁",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text("😿", fontSize = 72.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "¡Ups! Algo salió mal",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                error!!,
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { viewModel.loadProducts() },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Reintentar", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                products.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text("🏪", fontSize = 72.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Tienda vacía",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Pronto tendremos productos disponibles",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Categorías horizontal con diseño mejorado
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            items(categorias) { cat ->
                                CategoriaChip(
                                    categoria = cat,
                                    isSelected = categoriaSeleccionada == cat.nombre,
                                    onClick = { categoriaSeleccionada = cat.nombre }
                                )
                            }
                        }

                        // Contador de productos
                        if (productosFiltrados.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "✨ ${productosFiltrados.size} productos encontrados",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryPurple
                                )
                            }
                        }

                        // Grid de productos
                        if (productosFiltrados.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(32.dp)
                                ) {
                                    Text("🔍", fontSize = 72.sp)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "No encontramos productos",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                    if (busqueda.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "para: '$busqueda'",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(productosFiltrados) { product ->
                                    ProductoCardAPI(
                                        producto = product,
                                        onAgregar = {
                                            onAgregar(product)
                                            mostrarMensaje = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Snackbar cuando se agrega al carrito
        AnimatedVisibility(
            visible = mostrarMensaje,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrimaryPurple,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "¡Agregado al carrito! 🎉",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                mostrarMensaje = false
            }
        }
    }
}

@Composable
fun CategoriaChip(
    categoria: Categoria,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryPurple else Color.White,
        label = "background"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.DarkGray,
        label = "content"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        tonalElevation = if (isSelected) 8.dp else 2.dp,
        modifier = Modifier.shadow(
            elevation = if (isSelected) 8.dp else 4.dp,
            shape = RoundedCornerShape(20.dp)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                categoria.emoji,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                categoria.nombre,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ProductoCardAPI(producto: Products, onAgregar: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            // Imagen del producto con badge de oferta
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                LightBackground,
                                Color.White
                            )
                        )
                    )
            ) {
                val esProductoDePrueba = esProductoDePrueba(producto)
                val painter = if (esProductoDePrueba) {
                    getImagenLocalParaProducto(producto)
                } else {
                    val imageUrl = buildImageUrl(producto.image)
                    rememberAsyncImagePainter(
                        model = imageUrl,
                        error = painterResource(id = R.drawable.logopet),
                        placeholder = painterResource(id = R.drawable.logopet)
                    )
                }

                Image(
                    painter = painter,
                    contentDescription = producto.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                // Badge de oferta (simulado)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AccentOrange,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        "🔥 HOT",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }

            // Contenido del producto
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    producto.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    producto.description,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Precio y rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "$${producto.price}",
                            color = PrimaryPurple,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Rating simulado
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = AccentYellow,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            "4.5",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón de agregar
                Button(
                    onClick = onAgregar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Agregar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// Función para detectar si es un producto de prueba
fun esProductoDePrueba(producto: Products): Boolean {
    val idsProductosPrueba = listOf(1, 2, 3, 4, 5, 6)
    val nombresProductosPrueba = listOf(
        "Royal Canin",
        "Pro Plan",
        "Pelota Interactiva",
        "Rascador para Gatos",
        "Shampoo Anti Pulgas",
        "Cepillo Deslanador"
    )

    return producto.id in idsProductosPrueba ||
            producto.name in nombresProductosPrueba
}

// Función para obtener imagen local según el producto
@Composable
fun getImagenLocalParaProducto(producto: Products): Painter {
    return when {
        producto.name.contains("Royal Canin", ignoreCase = true) -> painterResource(R.drawable.producto1)
        producto.name.contains("Pro Plan", ignoreCase = true) -> painterResource(R.drawable.producto2)
        producto.name.contains("Pelota Interactiva", ignoreCase = true) -> painterResource(R.drawable.producto3)
        producto.name.contains("Rascador", ignoreCase = true) -> painterResource(R.drawable.producto4)
        producto.name.contains("Shampoo", ignoreCase = true) -> painterResource(R.drawable.producto5)
        producto.name.contains("Cepillo", ignoreCase = true) -> painterResource(R.drawable.producto6)
        else -> {
            when (producto.id) {
                1 -> painterResource(R.drawable.producto1)
                2 -> painterResource(R.drawable.producto2)
                3 -> painterResource(R.drawable.producto3)
                4 -> painterResource(R.drawable.producto4)
                5 -> painterResource(R.drawable.producto5)
                6 -> painterResource(R.drawable.producto6)
                else -> painterResource(R.drawable.logopet)
            }
        }
    }
}

// Función para construir URL de imagen
fun buildImageUrl(imagePath: String?): String? {
    return when {
        imagePath.isNullOrEmpty() -> null
        imagePath.startsWith("http") -> imagePath
        else -> "http://10.0.2.2:8000/storage/$imagePath"
    }
}

// Modelo local para categoría con emoji
data class Categoria(
    val nombre: String,
    val icono: Int,
    val emoji: String
)