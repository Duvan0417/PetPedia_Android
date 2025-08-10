package com.example.primerproyecto

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

@Composable
fun PetAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6C28D0),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFF5F5F5)
        ),
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            PetAppTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var showRegister by remember { mutableStateOf(false) }

                if (!isLoggedIn) {
                    if (showRegister) {
                        RegisterScreen(
                            onRegister = { isLoggedIn = true },
                            onGoToLogin = { showRegister = false }
                        )
                    } else {
                        LoginScreen(
                            onLogin = { isLoggedIn = true },
                            onGoToRegister = { showRegister = true }
                        )
                    }
                } else {
                    PetApp()
                }
            }
        }
    }
}


data class TabItem(
    val title: String,
    val iconVector: ImageVector? = null,
    val iconRes: Int? = null
)

data class Producto(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenRes: Int
)

data class CarritoItem(
    val producto: Producto,
    var cantidad: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val carrito = remember { mutableStateListOf<CarritoItem>() }
    var mostrarCarrito by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "🐾 PetPedia",
                onMoreClick = { /* Más opciones */ }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFEEDCFF))
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val tabs = listOf(
                        TabItem("Tienda", iconVector = Icons.Default.ShoppingCart),
                        TabItem("Inicio", iconVector = Icons.Default.Home),
                        TabItem("Veterinarias", iconVector = Icons.Default.LocalHospital), // Cambiado
                        TabItem("Más", iconVector = Icons.Default.MoreVert)
                    )

                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            icon = {
                                if (tab.iconVector != null) {
                                    Icon(
                                        imageVector = tab.iconVector,
                                        contentDescription = tab.title,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                } else if (tab.iconRes != null) {
                                    Image(
                                        painter = painterResource(id = tab.iconRes),
                                        contentDescription = tab.title,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(top = 4.dp)
                                    )
                                }
                            },
                            label = { Text(tab.title) },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = { mostrarCarrito = true },
                    containerColor = Color(0xFF6C28D0),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                }

                if (carrito.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .offset(x = 28.dp, y = (-10).dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = carrito.sumOf { it.cantidad }.toString(),
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                1 -> HomeScreen(searchQuery = "")
                0 -> TiendaScreen(
                    carrito = carrito,
                    onAgregar = { producto ->
                        val itemExistente = carrito.find { it.producto == producto }
                        if (itemExistente != null) {
                            carrito[carrito.indexOf(itemExistente)] =
                                itemExistente.copy(cantidad = itemExistente.cantidad + 1)
                        } else {
                            carrito.add(CarritoItem(producto, 1))
                        }
                    }
                )
                2 -> VeterinariasScreen()
                3 -> MasOpcionesScreen()
            }
        }

        if (mostrarCarrito) {
            CarritoDialog(
                carrito = carrito,
                onCerrar = { mostrarCarrito = false },
                onEliminar = { item -> carrito.remove(item) },
                onActualizarCantidad = { item, nuevaCantidad ->
                    val index = carrito.indexOf(item)
                    if (index != -1) {
                        carrito[index] = item.copy(cantidad = nuevaCantidad)
                    }
                },
                onFinalizarCompra = {
                    mostrarCarrito = false
                    carrito.clear()
                }
            )
        }
    }
}

@Composable
fun CarritoDialog(
    carrito: MutableList<CarritoItem>,
    onCerrar: () -> Unit,
    onEliminar: (CarritoItem) -> Unit,
    onActualizarCantidad: (CarritoItem, Int) -> Unit,
    onFinalizarCompra: () -> Unit
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
                    onClick = {
                        compraFinalizada = false
                        onCerrar()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                ) {
                    Text("Aceptar")
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
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(item.producto.nombre, fontWeight = FontWeight.Bold)
                                            Text(
                                                item.producto.descripcion,
                                                fontSize = 12.sp,
                                                color = Color.Gray,
                                                maxLines = 2
                                            )
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = {
                                                if (item.cantidad > 1)
                                                    onActualizarCantidad(item, item.cantidad - 1)
                                            }) {
                                                Icon(Icons.Default.Remove, contentDescription = "Menos")
                                            }
                                            Text("${item.cantidad}", fontWeight = FontWeight.Bold)
                                            IconButton(onClick = {
                                                onActualizarCantidad(item, item.cantidad + 1)
                                            }) {
                                                Icon(Icons.Default.Add, contentDescription = "Más")
                                            }
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                "$${item.producto.precio * item.cantidad}",
                                                fontWeight = FontWeight.Bold
                                            )
                                            IconButton(
                                                onClick = { onEliminar(item) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Eliminar",
                                                    tint = Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider(thickness = 1.dp)
                        Spacer(modifier = Modifier.height(4.dp))

                        val total = carrito.sumOf { it.producto.precio * it.cantidad }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "$${total}",
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
fun MetodosPagoDialog(
    onSeleccionarTarjeta: (String) -> Unit,
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
                onClick = { onSeleccionarTarjeta(tarjetaSeleccionada) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    onMoreClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Comentado hasta que tengas el recurso
                /*
                Icon(
                    painter = painterResource(id = R.drawable.logopet),
                    contentDescription = "Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp)
                )
                */
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = onMoreClick) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF6C28D0),
            titleContentColor = Color.White
        )
    )
}



@Composable
fun MasOpcionesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de más opciones", modifier = Modifier.padding(16.dp))
    }
}