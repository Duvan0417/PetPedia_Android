package com.example.primerproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.primerproyecto.data.model.Products
import com.example.primerproyecto.ui.components.CarritoDialog
import com.example.primerproyecto.ui.screens.HomeScreen
import com.example.primerproyecto.ui.view.adopciones.AdopcionesScreen
import com.example.primerproyecto.ui.view.entrenadores.EntrenadoresScreen
import com.example.primerproyecto.ui.view.forum.ForumScreen
import com.example.primerproyecto.ui.view.gestionarservicios.GestionarServiciosScreen
import com.example.primerproyecto.ui.view.login.LoginScreen
import com.example.primerproyecto.ui.view.masopciones.MasOpcionesScreen
import com.example.primerproyecto.ui.view.pedidos.PedidosScreen
import com.example.primerproyecto.ui.view.perfil.PerfilScreen
import com.example.primerproyecto.ui.view.register.RegisterScreen
import com.example.primerproyecto.ui.view.tienda.TiendaScreen
import com.example.primerproyecto.ui.view.veterinarias.VeterinariasScreen

// ======================= THEME =======================
@Composable
fun PetAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFFFA520), // ¡Este es el color naranja/coral!
            secondary = Color(0xFF3F3DAC),
            background = Color(0xFFF5F5F5)
        ),
        content = content
    )
}

// ======================= MAIN ACTIVITY =======================
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
                            onGoToRegister = { showRegister = true },
                            onGuestLogin = { isLoggedIn = true }
                        )
                    }
                } else {
                    PetApp()
                }
            }
        }
    }
}

// ======================= DATA CLASSES =======================
data class TabItem(
    val title: String,
    val iconVector: ImageVector
)

// Data class para items del carrito
data class CarritoItem(
    val product: Products,
    var cantidad: Int
)

// Data class para representar el carrito de la base de datos
data class ShoppingCart(
    val id: Int,
    val amount: Double,
    val date: String,
    val userId: Int?
)

// ======================= MAIN APP =======================
@Composable
fun PetApp() {
    var selectedTab by remember { mutableStateOf(0) }

    // ✅ UNA SOLA LISTA PARA EL CARRITO
    val carrito = remember { mutableStateListOf<CarritoItem>() }

    val pedidos = remember { mutableStateListOf<Pedido>() }
    var mostrarCarrito by remember { mutableStateOf(false) }

    // Estados para navegación en Más Opciones
    var mostrarPerfil by remember { mutableStateOf(false) }
    var mostrarEntrenadores by remember { mutableStateOf(false) }
    var mostrarAdopciones by remember { mutableStateOf(false) }
    var mostrarGestionarServicios by remember { mutableStateOf(false) }
    var mostrarForo by remember { mutableStateOf(false) }
    var mostrarPedidos by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when {
                    mostrarPerfil -> PerfilScreen(onBack = { mostrarPerfil = false })
                    mostrarEntrenadores -> EntrenadoresScreen()
                    mostrarAdopciones -> AdopcionesScreen()
                    mostrarGestionarServicios -> GestionarServiciosScreen(
                        onBack = { mostrarGestionarServicios = false }
                    )
                    mostrarForo -> ForumScreen(onBack = { mostrarForo = false })
                    mostrarPedidos -> PedidosScreen(onBack = { mostrarPedidos = false })

                    else -> when (selectedTab) {
                        0 -> HomeScreen()
                        1 -> TiendaScreen(
                            carrito = carrito,
                            onAgregar = { product ->
                                val existente = carrito.find { it.product.id == product.id }
                                if (existente != null) {
                                    carrito[carrito.indexOf(existente)] =
                                        existente.copy(cantidad = existente.cantidad + 1)
                                } else {
                                    carrito.add(CarritoItem(product, 1))
                                }
                            }
                        )
                        2 -> VeterinariasScreen()
                        3 -> MasOpcionesScreen(
                            onNavigateToPerfil = { mostrarPerfil = true },
                            onNavigateToEntrenadores = { mostrarEntrenadores = true },
                            onNavigateToAdopciones = { mostrarAdopciones = true },
                            onNavigateToGestionarServicios = { mostrarGestionarServicios = true },
                            onNavigateToForo = { mostrarForo = true },
                            onNavigateToPedidos = { mostrarPedidos = true }
                        )
                    }
                }
            }
        }

        // ======= NAVBAR FLOTANTE ABAJO =======
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(50)),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    val tabs = listOf(
                        TabItem("Inicio", Icons.Default.Home),
                        TabItem("Tienda", Icons.Default.ShoppingCart),
                        TabItem("Veterinarias", Icons.Default.LocalHospital),
                        TabItem("Más", Icons.Default.MoreVert)
                    )

                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = tab.iconVector,
                                    contentDescription = tab.title,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            },
                            label = { Text(tab.title) },
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                                // Resetear subpantallas al cambiar tab
                                mostrarPerfil = false
                                mostrarEntrenadores = false
                                mostrarAdopciones = false
                                mostrarGestionarServicios = false
                                mostrarForo = false
                                mostrarPedidos = false
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color(0xFF6C28D0),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }

        // ======= BOTÓN DE CARRITO ENCIMA DEL NAVBAR =======
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 110.dp)
        ) {
            FloatingActionButton(
                onClick = { mostrarCarrito = true },
                containerColor = Color(0xFF6C28D0),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
            }

            // ✅ Mostrar contador de productos
            val totalItems = carrito.sumOf { it.cantidad }

            if (totalItems > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 28.dp, y = (-10).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = totalItems.toString(),
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // ======= CARRITO =======
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
                    // Calcular el total para guardar en la base de datos
                    val total = carrito.sumOf { it.product.price * it.cantidad }

                    if (carrito.isNotEmpty()) {
                        // Aquí deberías llamar a tu API para guardar el carrito
                        // shoppingCartRepository.saveShoppingCart(total, carrito)

                        pedidos.add(0, Pedido(
                            productos = carrito.toList(),
                            total = total
                        ))
                    }
                    mostrarCarrito = false
                    carrito.clear()
                }
            )
        }
    }
}

// Data class simplificada para Pedido
data class Pedido(
    val productos: List<CarritoItem>,
    val total: Double
)