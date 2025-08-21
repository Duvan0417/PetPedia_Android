package com.example.primerproyecto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Si tu versión de Compose necesita OptIn para LazyVerticalGrid, descomenta la línea siguiente:
// @OptIn(ExperimentalFoundationApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaScreen(
    carrito: MutableList<CarritoItem>,
    onAgregar: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
    var busqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    // Categorías locales con icono drawable (usa fallback si no existe)
    val categorias = listOf(
        Categoria("Todos", R.drawable.logopet),        // fallback
        Categoria("Perros", R.drawable.perro),        // reemplaza por tu drawable o usa logopet
        Categoria("Gatos", R.drawable.cat),
        Categoria("Juguetes", R.drawable.juguetespe),
        Categoria("Comida", R.drawable.comida),
        Categoria("Accesorios", R.drawable.accesorios)
    ).map { cat ->
        // Si alguno de los drawables no existe, usar logopet como fallback para evitar crash
        val iconSafe = try {
            // referenciar directamente; si no existe lanzará exception en tiempo de compilación.
            // En ejecución esto está OK si los drawables existen en /res/drawable.
            cat.icono
        } catch (_: Exception) {
            R.drawable.logopet
        }
        cat.copy(icono = iconSafe)
    }

    // Usa la clase Producto definida en MainActivity (ver snippet más arriba)
    val productos = listOf(
        Producto("Collar para perro", "Collar resistente y ajustable para perros", 25000.0, R.drawable.collarp, "Perros"),
        Producto("Cama para gato", "Cama suave y cómoda para gatos", 80000.0, R.drawable.camap, "Gatos"),
        Producto("Juguete mordedor", "Juguete para entretener y cuidar los dientes", 15000.0, R.drawable.juguetespe, "Juguetes"),
        Producto("Comida para perro 5kg", "Alimento balanceado de alta calidad", 95000.0, R.drawable.comidaperro, "Comida"),
        Producto("Comida para gato 2kg", "Alimento premium para gatos", 62000.0, R.drawable.comidagato, "Comida"),
        Producto("Rascador para gatos", "Rascador grande con juguetes colgantes", 110000.0, R.drawable.rascador, "Accesorios")
    )

    val productosFiltrados = productos.filter { prod ->
        (categoriaSeleccionada == "Todos" || prod.categoria == categoriaSeleccionada) &&
                prod.nombre.contains(busqueda, ignoreCase = true)
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

        // Categorías horizontal (imagen + nombre)
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
                    // icono circular
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

        // Grid de productos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(productosFiltrados) { producto ->
                ProductoCard(producto = producto, onAgregar = { onAgregar(producto) })
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto, onAgregar: () -> Unit) {
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
            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFEFEF))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(producto.descripcion, fontSize = 12.sp, color = Color.Gray, maxLines = 2)
            Text("$${producto.precio}", color = Color(0xFF6C28D0), fontSize = 14.sp)
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

// Modelo local para categoría (no hay choque con Producto)
data class Categoria(
    val nombre: String,
    val icono: Int
)
