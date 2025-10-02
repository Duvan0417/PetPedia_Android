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
    onLoginSuccess: (String) -> Unit,
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
            val token = response.token
            if (token != null) {
                onLoginSuccess(token)
            } else {
                localErrorMessage = "Token no recibido del servidor"
            }
        }
    }

    // Manejar errores del ViewModel
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            localErrorMessage = message
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
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Iniciando sesión...", color = Color.Gray)
                    }
                } else {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (localErrorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(localErrorMessage, color = Color.Red, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                localErrorMessage = "Por favor completa todos los campos"
                            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                localErrorMessage = "Correo inválido"
                            } else {
                                localErrorMessage = ""
                                val loginRequest = LoginRequest(
                                    email = email,
                                    password = password
                                )
                                viewModel.loginUser(loginRequest)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                    ) {
                        Text("Entrar", color = Color.White, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { onGuestLogin() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6C28D0))
                    ) {
                        Text("Ingresar como invitado", fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    Text("O inicia con", color = Color.Gray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SocialButton(R.drawable.google)
                        SocialButton(R.drawable.facebook)
                        SocialButton(R.drawable.x)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "¿No tienes cuenta? Regístrate",
                        color = Color(0xFF6C28D0),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onGoToRegister() }
                    )
                }
            }
        }
    }
}

@Composable
fun SocialButton(logoResId: Int) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color(0xFFF5F5F5))
            .clickable { /* Acción social */ },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )
    }
}