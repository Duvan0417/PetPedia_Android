package com.example.primerproyecto.ui.view.tienda

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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    // Categorías locales
    val categorias = listOf(
        Categoria("Todos", R.drawable.logopet),
        Categoria("Perros", R.drawable.perro),
        Categoria("Gatos", R.drawable.cat),
        Categoria("Juguetes", R.drawable.juguetespe),
        Categoria("Comida", R.drawable.comida),
        Categoria("Accesorios", R.drawable.accesorios)
    )

    // Filtrar productos
    val productosFiltrados = products.filter { product ->
        (categoriaSeleccionada == "Todos" || product.name.contains(categoriaSeleccionada, ignoreCase = true)) &&
                product.name.contains(busqueda, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(8.dp)
    ) {
        // Buscador
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar producto...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6C28D0),
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        // Manejo de estados
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6C28D0))
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Error al cargar productos",
                            color = Color.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            error!!,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadProducts() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                        ) {
                            Text("Reintentar")
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
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Sin productos",
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No hay productos disponibles",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            else -> {
                // Categorías horizontal
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(categorias) { cat ->
                        val selected = categoriaSeleccionada == cat.nombre
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { categoriaSeleccionada = cat.nombre }
                                .padding(4.dp)
                                .background(
                                    if (selected) Color(0xFF6C28D0).copy(alpha = 0.12f) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(if (selected) Color(0xFF6C28D0) else Color.LightGray.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = cat.icono),
                                    contentDescription = cat.nombre,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = cat.nombre,
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) Color(0xFF6C28D0) else Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Grid de productos desde API
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productosFiltrados) { product ->
                        ProductoCardAPI(producto = product, onAgregar = { onAgregar(product) })
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCardAPI(producto: Products, onAgregar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen desde API
            val imageUrl = if (!producto.image.isNullOrEmpty()) {
                if (producto.image!!.startsWith("http")) producto.image
                else "http://10.0.2.2:8000/storage/${producto.image}"
            } else null

            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    error = painterResource(id = R.drawable.logopet),
                    placeholder = painterResource(id = R.drawable.logopet)
                ),
                contentDescription = producto.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFEFEF))
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(producto.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                producto.description,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text("$${producto.price}", color = Color(0xFF6C28D0), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAgregar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
            ) {
                Text("Agregar", color = Color.White)
            }
        }
    }
}

// Modelo local para categoría
data class Categoria(
    val nombre: String,
    val icono: Int
)