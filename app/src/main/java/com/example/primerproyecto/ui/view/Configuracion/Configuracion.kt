// ===== Configuracion.kt =====
package com.example.primerproyecto.ui.view.Configuracion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfiguracionScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {} // ✅ Nuevo parámetro para cerrar sesión
) {
    var notificacionesActivas by remember { mutableStateOf(true) }
    var modoOscuro by remember { mutableStateOf(false) }
    var sonidosActivos by remember { mutableStateOf(true) }
    var ubicacionActiva by remember { mutableStateOf(true) }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF6C28D0),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Configuración",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección: Preferencias
            SectionTitle("Preferencias")

            SettingsCard {
                Column {
                    SettingSwitchItem(
                        icon = Icons.Default.Notifications,
                        title = "Notificaciones",
                        subtitle = "Recibir alertas y actualizaciones",
                        checked = notificacionesActivas,
                        onCheckedChange = { notificacionesActivas = it }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingSwitchItem(
                        icon = Icons.Default.DarkMode,
                        title = "Modo Oscuro",
                        subtitle = "Cambiar tema de la aplicación",
                        checked = modoOscuro,
                        onCheckedChange = { modoOscuro = it }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingSwitchItem(
                        icon = Icons.Default.VolumeUp,
                        title = "Sonidos",
                        subtitle = "Efectos de sonido y audio",
                        checked = sonidosActivos,
                        onCheckedChange = { sonidosActivos = it }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingSwitchItem(
                        icon = Icons.Default.LocationOn,
                        title = "Ubicación",
                        subtitle = "Servicios basados en ubicación",
                        checked = ubicacionActiva,
                        onCheckedChange = { ubicacionActiva = it }
                    )
                }
            }

            // Sección: Cuenta
            SectionTitle("Cuenta")

            SettingsCard {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Person,
                        title = "Editar Perfil",
                        subtitle = "Actualizar información personal",
                        onClick = { /* Navegar a editar perfil */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.Lock,
                        title = "Cambiar Contraseña",
                        subtitle = "Actualizar contraseña de acceso",
                        onClick = { /* Navegar a cambiar contraseña */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.Email,
                        title = "Correo Electrónico",
                        subtitle = "Gestionar correo vinculado",
                        onClick = { /* Navegar a gestionar email */ }
                    )
                }
            }

            // Sección: Privacidad y Seguridad
            SectionTitle("Privacidad y Seguridad")

            SettingsCard {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Privacidad",
                        subtitle = "Controlar visibilidad de datos",
                        onClick = { /* Navegar a privacidad */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.Shield,
                        title = "Seguridad",
                        subtitle = "Configuración de seguridad",
                        onClick = { /* Navegar a seguridad */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.Block,
                        title = "Usuarios Bloqueados",
                        subtitle = "Administrar bloqueos",
                        onClick = { /* Navegar a bloqueados */ }
                    )
                }
            }

            // Sección: Soporte
            SectionTitle("Soporte")

            SettingsCard {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Help,
                        title = "Centro de Ayuda",
                        subtitle = "Preguntas frecuentes y tutoriales",
                        onClick = { /* Navegar a ayuda */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.ContactSupport,
                        title = "Contactar Soporte",
                        subtitle = "Enviar consulta o reporte",
                        onClick = { /* Navegar a soporte */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.Feedback,
                        title = "Enviar Comentarios",
                        subtitle = "Compartir sugerencias",
                        onClick = { /* Navegar a feedback */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.Star,
                        title = "Calificar App",
                        subtitle = "Valora nuestra aplicación",
                        onClick = { /* Abrir rating */ }
                    )
                }
            }

            // Sección: Legal
            SectionTitle("Legal")

            SettingsCard {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Description,
                        title = "Términos y Condiciones",
                        subtitle = "Leer términos de uso",
                        onClick = { /* Navegar a términos */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.Policy,
                        title = "Política de Privacidad",
                        subtitle = "Ver política de privacidad",
                        onClick = { /* Navegar a política */ }
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.Gavel,
                        title = "Licencias",
                        subtitle = "Software de terceros",
                        onClick = { /* Navegar a licencias */ }
                    )
                }
            }

            // Sección: Sesión
            SectionTitle("Sesión")

            SettingsCard {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Logout,
                        title = "Cerrar Sesión",
                        subtitle = "Salir de tu cuenta",
                        onClick = { showLogoutDialog = true },
                        iconColor = Color(0xFFFF9800)
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.3f))

                    SettingsItem(
                        icon = Icons.Default.DeleteForever,
                        title = "Eliminar Cuenta",
                        subtitle = "Borrar cuenta permanentemente",
                        onClick = { showDeleteAccountDialog = true },
                        iconColor = Color(0xFFE53935)
                    )
                }
            }

            // Información de la app
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "PetCare App",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Versión 1.0.0",
                    fontSize = 12.sp,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }
        }
    }

    // Diálogo de cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFF9800).copy(alpha = 0.2f),
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            },
            title = {
                Text(
                    "¿Cerrar Sesión?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas cerrar sesión? Podrás volver a iniciar sesión en cualquier momento.",
                    color = Color.Gray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout() // ✅ Ejecutar el callback de logout
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800)
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Diálogo de eliminar cuenta
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE53935).copy(alpha = 0.2f),
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            },
            title = {
                Text(
                    "¿Eliminar Cuenta?",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE53935)
                )
            },
            text = {
                Column {
                    Text(
                        "Esta acción es permanente e irreversible.",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Se eliminarán todos tus datos, incluyendo:",
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Perfil y configuración", color = Color.Gray, fontSize = 14.sp)
                    Text("• Solicitudes y pedidos", color = Color.Gray, fontSize = 14.sp)
                    Text("• Historial de servicios", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "¿Estás completamente seguro?",
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        // Aquí iría la lógica para eliminar cuenta
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    )
                ) {
                    Text("Sí, Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF6C28D0),
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        content()
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    iconColor: Color = Color(0xFF6C28D0)
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2E2E2E)
                )
                Text(
                    subtitle,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SettingSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF6C28D0).copy(alpha = 0.1f),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF6C28D0),
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2E2E2E)
            )
            Text(
                subtitle,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF6C28D0),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
    }
}

// ===== MainActivity.kt - ACTUALIZACIÓN =====
// En ClienteApp, actualizar el bloque de ConfiguracionScreen:

/*
mostrarConfiguracion -> ConfiguracionScreen(
    onBack = { mostrarConfiguracion = false },
    onLogout = { 
        // Limpiar el token
        RetrofitService.setAuthToken(null)
        // Regresar al login cambiando el estado
        // Esto se debe hacer en el nivel de MainActivity
    }
)
*/