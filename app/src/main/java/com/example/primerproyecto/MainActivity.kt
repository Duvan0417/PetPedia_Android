package com.example.primerproyecto

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.primerproyecto.data.model.SessionManager
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
import com.example.primerproyecto.ui.view.entrenadores.EntrenadoresScreen
import com.example.primerproyecto.ui.view.veterinarias.VeterinariasScreen
import com.example.primerproyecto.ui.view.solicitudes.SolicitudScreen
import com.example.primerproyecto.ui.view.Configuracion.ConfiguracionScreen
import com.example.primerproyecto.ui.viewmodel.OrderViewModel
import com.example.primerproyecto.ui.viewmodel.ShoppingCartViewModel
import com.example.primerproyecto.ui.viewmodel.SessionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

// ======================= THEME ======================
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

// ======================= MAIN ACTIVITY ======================
class MainActivity : ComponentActivity() {

    private val orderViewModel: OrderViewModel by viewModels()
    private val shoppingCartViewModel: ShoppingCartViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by lazy {
        SessionViewModel(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            PetAppTheme {
                // ✅ NO usar rememberSaveable para estados de sesión
                var isLoggedIn by remember { mutableStateOf(false) }
                var showRegister by remember { mutableStateOf(false) }
                var authToken by remember { mutableStateOf<String?>(null) }
                var userRole by remember { mutableStateOf<String?>(null) }
                var currentUserId by remember { mutableStateOf(0) }
                var isLoggingOut by remember { mutableStateOf(false) }

                val coroutineScope = rememberCoroutineScope()

                // ✅ Función de logout simplificada y más directa
                val handleLogout: () -> Unit = {
                    coroutineScope.launch {
                        isLoggingOut = true

                        // Limpiar ViewModels
                        shoppingCartViewModel.clearCart()
                        orderViewModel.resetOrderCreated()
                        sessionViewModel.logout()

                        // Limpiar SharedPreferences
                        val sharedPrefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                        sharedPrefs.edit().clear().apply()

                        val sessionPrefs = getSharedPreferences("PetPediaSession", Context.MODE_PRIVATE)
                        sessionPrefs.edit().clear().apply()

                        // Limpiar RetrofitService
                        RetrofitService.setAuthToken(null)

                        // Pequeño delay para asegurar que todo se limpie
                        delay(200)

                        // ✅ RESETEAR TODOS LOS ESTADOS EN EL ORDEN CORRECTO
                        authToken = null
                        userRole = null
                        currentUserId = 0
                        showRegister = false
                        isLoggedIn = false

                        delay(300)
                        isLoggingOut = false
                    }
                }

                // Pantalla de carga durante logout
                if (isLoggingOut) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF6C28D0),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Cerrando sesión...",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    // ✅ Renderizado condicional simple sin AnimatedVisibility para evitar bugs
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
                                onLoginSuccess = { token, role, userId ->
                                    authToken = token
                                    userRole = role
                                    currentUserId = userId
                                    RetrofitService.setAuthToken(token)
                                    isLoggedIn = true
                                },
                                onGoToRegister = { showRegister = true },
                                onGuestLogin = {
                                    userRole = "guest"
                                    currentUserId = 0
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
                            shoppingCartViewModel = shoppingCartViewModel,
                            onLogout = handleLogout
                        )
                    }
                }
            }
        }
    }

    fun processCheckout(userId: Int, carritoItems: List<CarritoItem>) {
        if (carritoItems.isEmpty()) {
            return
        }

        val cartItemsForAPI = carritoItems.map { it.product to it.cantidad }
        orderViewModel.createOrder(userId, cartItemsForAPI)

        lifecycleScope.launch {
            orderViewModel.orderCreated.collect { created ->
                if (created) {
                    shoppingCartViewModel.clearCart()
                    orderViewModel.resetOrderCreated()
                }
            }
        }

        lifecycleScope.launch {
            orderViewModel.error.collectLatest { error ->
                error?.let {
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

data class CarritoItem(
    val product: Products,
    var cantidad: Int
)

data class ShoppingCart(
    val id: Int,
    val amount: Double,
    val date: String,
    val userId: Int?
)

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
    shoppingCartViewModel: ShoppingCartViewModel,
    onLogout: () -> Unit = {}
) {
    when {
        userRole == "Veterinaria" || userRole?.contains("veterinaria", ignoreCase = true) == true ->
            UserVeterinarioScreen()

        userRole == "Entrenador" || userRole?.contains("entrenador", ignoreCase = true) == true ->
            UserEntrenadorMainScreen(currentUserId = currentUserId)

        userRole == "Refugio" || userRole?.contains("refugio", ignoreCase = true) == true ->
            UserRefugioScreen(userId = currentUserId)

        else -> {
            ClienteApp(
                currentUserId = currentUserId,
                orderViewModel = orderViewModel,
                shoppingCartViewModel = shoppingCartViewModel,
                onLogout = onLogout
            )
        }
    }
}

// ======================= APP PARA CLIENTES =======================
@Composable
fun ClienteApp(
    currentUserId: Int,
    orderViewModel: OrderViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val carrito = remember { mutableStateListOf<CarritoItem>() }
    var mostrarCarrito by remember { mutableStateOf(false) }

    // Estados para el proceso de checkout
    var mostrarCheckoutSuccess by remember { mutableStateOf(false) }
    var mostrarCheckoutError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Estados para navegación
    var mostrarPerfil by remember { mutableStateOf(false) }
    var mostrarEntrenadores by remember { mutableStateOf(false) }
    var mostrarAdopciones by remember { mutableStateOf(false) }
    var mostrarGestionarServicios by remember { mutableStateOf(false) }
    var mostrarForo by remember { mutableStateOf(false) }
    var mostrarPedidos by remember { mutableStateOf(false) }
    var mostrarSolicitudes by remember { mutableStateOf(false) }
    var mostrarConfiguracion by remember { mutableStateOf(false) }

    // Observar el estado del ViewModel
    val orderCreated by orderViewModel.orderCreated.collectAsState()
    val orderError by orderViewModel.error.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()

    LaunchedEffect(orderCreated) {
        if (orderCreated) {
            mostrarCheckoutSuccess = true
            carrito.clear()
            orderViewModel.resetOrderCreated()
        }
    }

    LaunchedEffect(orderError) {
        orderError?.let { error ->
            mostrarCheckoutError = true
            errorMessage = error
            orderViewModel.clearError()
        }
    }

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
                        mostrarSolicitudes -> SolicitudScreen()
                        mostrarEntrenadores -> EntrenadoresScreen()
                        mostrarAdopciones -> AdopcionesScreen()
                        mostrarConfiguracion -> ConfiguracionScreen(
                            onBack = { mostrarConfiguracion = false },
                            onLogout = {
                                // ✅ Cerrar configuración primero
                                mostrarConfiguracion = false
                                // ✅ Ejecutar logout después
                                onLogout()
                            }
                        )
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
                                    shoppingCartViewModel.addToCart(product)
                                }
                            )
                            2 -> VeterinariasScreen()
                            3 -> MasOpcionesScreen(
                                onNavigateToPerfil = { mostrarPerfil = true },
                                onNavigateToSolicitudes = { mostrarSolicitudes = true },
                                onNavigateToEntrenadores = { mostrarEntrenadores = true },
                                onNavigateToAdopciones = { mostrarAdopciones = true },
                                onNavigateToGestionarServicios = { mostrarGestionarServicios = true },
                                onNavigateToForo = { mostrarForo = true },
                                onNavigateToPedidos = {
                                    mostrarPedidos = true
                                    if (currentUserId > 0) {
                                        orderViewModel.loadOrdersByUser(currentUserId)
                                    }
                                },
                                onNavigateToConfiguracion = { mostrarConfiguracion = true }
                            )
                        }
                    }
                }
            }
        )

        // Navbar flotante
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
                                mostrarPerfil = false
                                mostrarSolicitudes = false
                                mostrarEntrenadores = false
                                mostrarAdopciones = false
                                mostrarGestionarServicios = false
                                mostrarForo = false
                                mostrarPedidos = false
                                mostrarConfiguracion = false
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

        // Botón de carrito
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

        // Diálogo de carrito
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
                        mostrarCheckoutError = true
                        errorMessage = "Debes iniciar sesión para realizar una compra"
                        return@CarritoDialog
                    }

                    if (carrito.isNotEmpty()) {
                        val cartItemsForAPI = carrito.map { it.product to it.cantidad }
                        orderViewModel.createOrder(currentUserId, cartItemsForAPI)
                    }
                    mostrarCarrito = false
                },
                isLoading = isLoading
            )
        }

        // Alerta de éxito
        if (mostrarCheckoutSuccess) {
            AlertDialog(
                onDismissRequest = { mostrarCheckoutSuccess = false },
                title = { Text("¡Compra Exitosa!") },
                text = { Text("Tu pedido ha sido procesado correctamente.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarCheckoutSuccess = false
                            mostrarPedidos = true
                        }
                    ) {
                        Text("Ver Pedidos")
                    }
                }
            )
        }

        // Alerta de error
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