package com.example.primerproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.lifecycle.lifecycleScope
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.data.model.Products
import com.example.primerproyecto.ui.components.CarritoDialog
import com.example.primerproyecto.ui.screens.HomeScreen
import com.example.primerproyecto.ui.view.UserRefugio.UserRefugioScreen
import com.example.primerproyecto.ui.view.UserVeterinario.UserVeterinarioScreen
import com.example.primerproyecto.ui.view.adopciones.AdopcionesScreen
import com.example.primerproyecto.ui.view.forum.ForumScreen
import com.example.primerproyecto.ui.view.gestionarservicios.GestionarServiciosScreen
import com.example.primerproyecto.ui.view.login.LoginScreen
import com.example.primerproyecto.ui.view.masopciones.MasOpcionesScreen
import com.example.primerproyecto.ui.view.pedidos.PedidosScreen
import com.example.primerproyecto.ui.view.perfil.PerfilScreen
import com.example.primerproyecto.ui.view.register.RegisterScreen
import com.example.primerproyecto.ui.view.tienda.TiendaScreen
import com.example.primerproyecto.ui.view.UserEntrenador.UserEntrenadorMainScreen
import com.example.primerproyecto.ui.view.veterinarias.VeterinariasScreen
import com.example.primerproyecto.ui.viewmodel.OrderViewModel
import com.example.primerproyecto.ui.viewmodel.ShoppingCartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// ======================= THEME =======================
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

// ======================= MAIN ACTIVITY =======================
class MainActivity : ComponentActivity() {

    // ViewModels a nivel de Activity
    private val orderViewModel: OrderViewModel by viewModels()
    private val shoppingCartViewModel: ShoppingCartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            PetAppTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var showRegister by remember { mutableStateOf(false) }
                var authToken by remember { mutableStateOf<String?>(null) }
                var userRole by remember { mutableStateOf<String?>(null) }
                var currentUserId by remember { mutableStateOf(0) }

                if (!isLoggedIn) {
                    if (showRegister) {
                        RegisterScreen(
                            onRegisterSuccess = {
                                showRegister = false
                            },
                            onGoToLogin = { showRegister = false }
                        )
                    } else {
                        LoginScreen(
                            onLoginSuccess = { token, role, userId -> // ✅ Ahora también recibimos userId
                                authToken = token
                                userRole = role
                                currentUserId = userId
                                RetrofitService.setAuthToken(token)
                                isLoggedIn = true
                            },
                            onGoToRegister = { showRegister = true },
                            onGuestLogin = {
                                userRole = "guest"
                                currentUserId = 0 // Usuario invitado
                                isLoggedIn = true
                            }
                        )
                    }
                } else {
                    PetApp(
                        authToken = authToken,
                        userRole = userRole,
                        currentUserId = currentUserId,
                        orderViewModel = orderViewModel,
                        shoppingCartViewModel = shoppingCartViewModel
                    )
                }
            }
        }
    }

    // Función para procesar el checkout
    fun processCheckout(userId: Int, carritoItems: List<CarritoItem>) {
        if (carritoItems.isEmpty()) {
            // Mostrar mensaje de carrito vacío
            return
        }

        // Convertir CarritoItem a formato para el ViewModel
        val cartItemsForAPI = carritoItems.map { it.product to it.cantidad }

        orderViewModel.createOrder(userId, cartItemsForAPI)

        // Observar el resultado
        lifecycleScope.launch {
            orderViewModel.orderCreated.collect { created ->
                if (created) {
                    // Limpiar carrito local
                    shoppingCartViewModel.clearCart()
                    orderViewModel.resetOrderCreated()
                    // Aquí puedes mostrar un mensaje de éxito
                }
            }
        }

        // Observar errores
        lifecycleScope.launch {
            orderViewModel.error.collectLatest { error ->
                error?.let {
                    // Mostrar mensaje de error
                    orderViewModel.clearError()
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

// Data class simplificada para Pedido
data class Pedido(
    val productos: List<CarritoItem>,
    val total: Double
)

// ======================= MAIN APP =======================
@Composable
fun PetApp(
    authToken: String? = null,
    userRole: String? = null,
    currentUserId: Int = 0,
    orderViewModel: OrderViewModel,
    shoppingCartViewModel: ShoppingCartViewModel
) {
    // ✅ DETERMINAR QUÉ INTERFAZ MOSTRAR SEGÚN EL ROL
    when {
        userRole == "Veterinaria" || userRole?.contains("veterinaria", ignoreCase = true) == true ->
            UserVeterinarioScreen() // ✅ PARA VETERINARIOS

        userRole == "Entrenador" || userRole?.contains("entrenador", ignoreCase = true) == true ->
            UserEntrenadorMainScreen() // ✅ NUEVO MAIN SCREEN PARA ENTRENADORES

        userRole == "Refugio" || userRole?.contains("refugio", ignoreCase = true) == true ->
            UserRefugioScreen() // ✅ PARA REFUGIOS

        else -> ClienteApp(
            currentUserId = currentUserId,
            orderViewModel = orderViewModel,
            shoppingCartViewModel = shoppingCartViewModel
        ) // Cliente, guest o cualquier otro
    }
}

// ======================= APP PARA CLIENTES =======================
@Composable
fun ClienteApp(
    currentUserId: Int,
    orderViewModel: OrderViewModel,
    shoppingCartViewModel: ShoppingCartViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    val carrito = remember { mutableStateListOf<CarritoItem>() }
    val pedidos = remember { mutableStateListOf<Pedido>() }
    var mostrarCarrito by remember { mutableStateOf(false) }

    // Estados para el proceso de checkout
    var mostrarCheckoutSuccess by remember { mutableStateOf(false) }
    var mostrarCheckoutError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Estados para navegación en Más Opciones
    var mostrarPerfil by remember { mutableStateOf(false) }
    var mostrarEntrenadores by remember { mutableStateOf(false) }
    var mostrarAdopciones by remember { mutableStateOf(false) }
    var mostrarGestionarServicios by remember { mutableStateOf(false) }
    var mostrarForo by remember { mutableStateOf(false) }
    var mostrarPedidos by remember { mutableStateOf(false) }

    // Observar el estado del ViewModel
    val orderCreated by orderViewModel.orderCreated.collectAsState()
    val orderError by orderViewModel.error.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()

    // Efecto para manejar éxito del checkout
    LaunchedEffect(orderCreated) {
        if (orderCreated) {
            mostrarCheckoutSuccess = true
            carrito.clear()
            orderViewModel.resetOrderCreated()
        }
    }

    // Efecto para manejar errores del checkout
    LaunchedEffect(orderError) {
        orderError?.let { error ->
            mostrarCheckoutError = true
            errorMessage = error
            orderViewModel.clearError()
        }
    }

    // Tabs para clientes
    val tabs = listOf(
        TabItem("Inicio", Icons.Default.Home),
        TabItem("Tienda", Icons.Default.ShoppingCart),
        TabItem("Veterinarias", Icons.Default.LocalHospital),
        TabItem("Más", Icons.Default.MoreVert)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when {
                        mostrarPerfil -> PerfilScreen(onBack = { mostrarPerfil = false })
                        //mostrarEntrenadores -> EntrenadoresScreen()
                        mostrarAdopciones -> AdopcionesScreen()
                        mostrarGestionarServicios -> GestionarServiciosScreen(
                            onBack = { mostrarGestionarServicios = false }
                        )
                        mostrarForo -> ForumScreen(onBack = { mostrarForo = false })
                        mostrarPedidos -> PedidosScreen(
                            onBack = { mostrarPedidos = false },
                            orderViewModel = orderViewModel,
                            userId = currentUserId
                        )

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
                                    // También actualizar el ViewModel
                                    shoppingCartViewModel.addToCart(product)
                                }
                            )
                            2 -> VeterinariasScreen()
                            3 -> MasOpcionesScreen(
                                onNavigateToPerfil = { mostrarPerfil = true },
                                onNavigateToEntrenadores = { mostrarEntrenadores = true },
                                onNavigateToAdopciones = { mostrarAdopciones = true },
                                onNavigateToGestionarServicios = { mostrarGestionarServicios = true },
                                onNavigateToForo = { mostrarForo = true },
                                onNavigateToPedidos = {
                                    mostrarPedidos = true
                                    // Cargar pedidos del usuario
                                    if (currentUserId > 0) {
                                        orderViewModel.loadOrdersByUser(currentUserId)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        )

        // ======= NAVBAR FLOTANTE PARA CLIENTES =======
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

        // ======= BOTÓN DE CARRITO PARA CLIENTES =======
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

        // ======= CARRITO PARA CLIENTES =======
        if (mostrarCarrito) {
            CarritoDialog(
                carrito = carrito,
                onCerrar = { mostrarCarrito = false },
                onEliminar = { item ->
                    carrito.remove(item)
                    shoppingCartViewModel.removeFromCart(item.product)
                },
                onActualizarCantidad = { item, nuevaCantidad ->
                    val index = carrito.indexOf(item)
                    if (index != -1) {
                        carrito[index] = item.copy(cantidad = nuevaCantidad)
                        shoppingCartViewModel.updateQuantity(item.product, nuevaCantidad)
                    }
                },
                onFinalizarCompra = {
                    if (currentUserId == 0) {
                        // Usuario no logueado
                        mostrarCheckoutError = true
                        errorMessage = "Debes iniciar sesión para realizar una compra"
                        return@CarritoDialog
                    }

                    if (carrito.isNotEmpty()) {
                        // Usar el ViewModel para crear la orden
                        val cartItemsForAPI = carrito.map { it.product to it.cantidad }
                        orderViewModel.createOrder(currentUserId, cartItemsForAPI)
                    }
                    mostrarCarrito = false
                },
                isLoading = isLoading
            )
        }

        // ======= ALERTA DE ÉXITO =======
        if (mostrarCheckoutSuccess) {
            AlertDialog(
                onDismissRequest = { mostrarCheckoutSuccess = false },
                title = { Text("¡Compra Exitosa!") },
                text = { Text("Tu pedido ha sido procesado correctamente y se ha guardado en la base de datos.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarCheckoutSuccess = false
                            // Opcional: navegar a pedidos
                            mostrarPedidos = true
                        }
                    ) {
                        Text("Ver Pedidos")
                    }
                }
            )
        }

        // ======= ALERTA DE ERROR =======
        if (mostrarCheckoutError) {
            AlertDialog(
                onDismissRequest = { mostrarCheckoutError = false },
                title = { Text("Error en la Compra") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(
                        onClick = { mostrarCheckoutError = false }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}