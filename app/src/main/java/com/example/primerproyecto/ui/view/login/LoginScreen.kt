
package com.example.primerproyecto.ui.view.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primerproyecto.R
import com.example.primerproyecto.data.model.LoginRequest
import com.example.primerproyecto.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit, // ✅ CAMBIO: Ahora recibe token y rol
    onGoToRegister: () -> Unit,
    onGuestLogin: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val loginResponse by viewModel.loginResponse.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localErrorMessage by remember { mutableStateOf("") }

    // Manejar respuesta del login
    LaunchedEffect(loginResponse) {
        loginResponse?.let { response ->
            if (response.success) {
                val token = response.token
                val role = response.role ?: "client" // ✅ Obtener el rol o usar "client" por defecto

                if (token != null) {
                    onLoginSuccess(token, role) // ✅ Pasar tanto token como rol
                } else {
                    localErrorMessage = response.message ?: "Token no recibido del servidor"
                }
            } else {
                localErrorMessage = response.message ?: "Error en el login"
            }
        }
    }

    // Manejar errores del ViewModel
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            localErrorMessage = message
        }
    }

    // Limpiar errores cuando el usuario empiece a escribir
    LaunchedEffect(email, password) {
        if (localErrorMessage.isNotEmpty()) {
            localErrorMessage = ""
            viewModel.clearErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF6C28D0), Color(0xFF9D4EDD))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logopet),
                    contentDescription = "Logo PetPedia",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 8.dp)
                )

                // Nombre
                Text(
                    text = "PetPedia",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6C28D0)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Iniciar Sesión", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = Color(0xFF6C28D0)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Iniciando sesión...", color = Color.Gray)
                    }
                } else {
                    // Campo de email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Color(0xFF6C28D0)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        isError = localErrorMessage.isNotEmpty()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo de contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Contraseña",
                                tint = Color(0xFF6C28D0)
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        isError = localErrorMessage.isNotEmpty()
                    )

                    // Mostrar mensaje de error
                    if (localErrorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = localErrorMessage,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón de login
                    Button(
                        onClick = {
                            when {
                                email.isBlank() || password.isBlank() -> {
                                    localErrorMessage = "Por favor completa todos los campos"
                                }
                                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                    localErrorMessage = "Por favor ingresa un correo electrónico válido"
                                }
                                password.length < 6 -> {
                                    localErrorMessage = "La contraseña debe tener al menos 6 caracteres"
                                }
                                else -> {
                                    localErrorMessage = ""
                                    val loginRequest = LoginRequest(
                                        email = email.trim(),
                                        password = password
                                    )
                                    viewModel.loginUser(loginRequest)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C28D0)
                        ),
                        enabled = !isLoading
                    ) {
                        Text(
                            "Iniciar Sesión",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botón de invitado
                    OutlinedButton(
                        onClick = { onGuestLogin() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6C28D0)
                        ),
                        enabled = !isLoading
                    ) {
                        Text(
                            "Ingresar como invitado",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Separador
                    Text("O inicia con", color = Color.Gray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Botones sociales
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SocialButton(R.drawable.google) {
                            localErrorMessage = "Login con Google - Próximamente"
                        }
                        SocialButton(R.drawable.facebook) {
                            localErrorMessage = "Login con Facebook - Próximamente"
                        }
                        SocialButton(R.drawable.x) {
                            localErrorMessage = "Login con X - Próximamente"
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Enlace a registro
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("¿No tienes cuenta? ", color = Color.Gray)
                        Text(
                            "Regístrate",
                            color = Color(0xFF6C28D0),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                if (!isLoading) {
                                    onGoToRegister()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SocialButton(
    logoResId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color(0xFFF5F5F5))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = "Social login",
            modifier = Modifier.size(26.dp)
        )
    }
}
