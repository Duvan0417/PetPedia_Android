package com.example.primerproyecto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaScreen(
    carrito: MutableList<CarritoItem>,  // Cambiado a CarritoItem
    onAgregar: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
    var busqueda by remember { mutableStateOf("") }

    val productos = listOf(
        Producto("Collar para perro", "Collar resistente y ajustable para perros", 25000.0, R.drawable.logopet),
        Producto("Cama para gato", "Cama suave y cómoda para gatos", 80000.0, R.drawable.logopet),
        Producto("Juguete mordedor", "Juguete para entretener y cuidar los dientes", 15000.0, R.drawable.logopet),
        Producto("Comida para perro 5kg", "Alimento balanceado de alta calidad", 95000.0, R.drawable.logopet),
        Producto("Comida para gato 2kg", "Alimento premium para gatos", 62000.0, R.drawable.logopet),
        Producto("Rascador para gatos", "Rascador grande con juguetes colgantes", 110000.0, R.drawable.logopet)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar producto...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { /* Acción de filtro */ },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productos.filter { it.nombre.contains(busqueda, ignoreCase = true) }) { producto ->
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
