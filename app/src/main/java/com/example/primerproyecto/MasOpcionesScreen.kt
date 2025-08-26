package com.example.primerproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.primerproyecto.ui.screens.HomeScreen

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

data class Producto(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenRes: Int,
    val categoria: String
)

data class CarritoItem(
    val producto: Producto,
    var cantidad: Int
)

// ======================= MAIN APP =======================
@Composable
fun PetApp() {
    var selectedTab by remember { mutableStateOf(0) }
    val carrito = remember { mutableStateListOf<CarritoItem>() }
    var mostrarCarrito by remember { mutableStateOf(false) }

    // Estados para navegación
    var mostrarPerfil by remember { mutableStateOf(false) }
    var mostrarEntrenadores by remember { mutableStateOf(false) }
    var mostrarAdopciones by remember { mutableStateOf(false) }
    var mostrarGestionarServicios by remember { mutableStateOf(false) }
    var mostrarConfiguracion by remember { mutableStateOf(false) }
    var mostrarPedidos by remember { mutableStateOf(false) }
    var mostrarForo by remember { mutableStateOf(false) }
    var mostrarSolicitudes by remember { mutableStateOf(false) }

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
                    mostrarGestionarServicios -> GestionarServiciosScreen { mostrarGestionarServicios = false }
                    mostrarConfiguracion -> ConfiguracionScreen { mostrarConfiguracion = false }
                    mostrarPedidos -> PedidosScreen()
                    mostrarForo -> ForoScreen()
                    mostrarSolicitudes -> SolicitudesScreen()
                    else -> when (selectedTab) {
                        0 -> HomeScreen()
                        1 -> TiendaScreen(
                            carrito = carrito,
                            onAgregar = { producto ->
                                val existente = carrito.find { it.producto == producto }
                                if (existente != null) {
                                    carrito[carrito.indexOf(existente)] =
                                        existente.copy(cantidad = existente.cantidad + 1)
                                } else {
                                    carrito.add(CarritoItem(producto, 1))
                                }
                            }
                        )
                        2 -> VeterinariasScreen()
                        3 -> MasOpcionesScreen(
                            onNavigateToPerfil = { mostrarPerfil = true },
                            onNavigateToEntrenadores = { mostrarEntrenadores = true },
                            onNavigateToAdopciones = { mostrarAdopciones = true },
                            onNavigateToGestionarServicios = { mostrarGestionarServicios = true },
                            onNavigateToConfiguracion = { mostrarConfiguracion = true },
                            onNavigateToPedidos = { mostrarPedidos = true },
                            onNavigateToForo = { mostrarForo = true },
                            onNavigateToSolicitudes = { mostrarSolicitudes = true }
                        )
                    }
                }
            }
        }

        // ======= NAVBAR =======
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
                                mostrarPerfil = false
                                mostrarEntrenadores = false
                                mostrarAdopciones = false
                                mostrarGestionarServicios = false
                                mostrarConfiguracion = false
                                mostrarPedidos = false
                                mostrarForo = false
                                mostrarSolicitudes = false
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

        // ======= BOTÓN DE CARRITO =======
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

        // ======= CARRITO DIALOG =======
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

// ======================= MÁS OPCIONES =======================
@Composable
fun MasOpcionesScreen(
    onNavigateToPerfil: () -> Unit,
    onNavigateToEntrenadores: () -> Unit,
    onNavigateToAdopciones: () -> Unit,
    onNavigateToGestionarServicios: () -> Unit,
    onNavigateToConfiguracion: () -> Unit,
    onNavigateToPedidos: () -> Unit,
    onNavigateToForo: () -> Unit,
    onNavigateToSolicitudes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Más Opciones",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6C28D0),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OpcionItem("Perfil", Icons.Default.Person, onNavigateToPerfil)
        OpcionItem("Entrenadores", Icons.Default.Build, onNavigateToEntrenadores)
        OpcionItem("Adopciones", Icons.Default.Pets, onNavigateToAdopciones)
        OpcionItem("Gestionar Servicios", Icons.Default.Settings, onNavigateToGestionarServicios)
        OpcionItem("Configuración", Icons.Default.SettingsApplications, onNavigateToConfiguracion)
        OpcionItem("Pedidos", Icons.Default.List, onNavigateToPedidos)
        OpcionItem("Foro", Icons.Default.Forum, onNavigateToForo)
        OpcionItem("Solicitudes", Icons.Default.Mail, onNavigateToSolicitudes)
    }
}

@Composable
fun OpcionItem(
    titulo: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6C28D0).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = titulo,
                    tint = Color(0xFF6C28D0)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = titulo,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Ir a $titulo",
                tint = Color.Gray
            )
        }
    }
}

// ======================= PANTALLAS NUEVAS =======================
@Composable
fun PedidosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Pantalla de Pedidos", fontSize = 18.sp)
    }
}

@Composable
fun ForoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Pantalla del Foro", fontSize = 18.sp)
    }
}

@Composable
fun SolicitudesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Pantalla de Solicitudes", fontSize = 18.sp)
    }
}
