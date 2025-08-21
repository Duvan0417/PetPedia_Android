package com.example.primerproyecto

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onGoToRegister: () -> Unit,
    onGuestLogin: () -> Unit // 👉 nuevo parámetro
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logo PetPedia
            Image(
                painter = painterResource(id = R.drawable.logopet),
                contentDescription = "Logo PetPedia",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 8.dp)
            )

            // Nombre del proyecto
            Text(
                text = "PetPedia",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C28D0)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Iniciar Sesión", fontSize = 26.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
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
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de login
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Por favor completa todos los campos"
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        errorMessage = "Correo inválido"
                    } else {
                        errorMessage = ""
                        onLogin()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
            ) {
                Text("Entrar", color = Color.White)
            }

            // 👉 Botón de invitado
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { onGuestLogin() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6C28D0))
            ) {
                Text("Ingresar como invitado")
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("O inicia con", color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                SocialButton(R.drawable.google)
                SocialButton(R.drawable.facebook)
                SocialButton(R.drawable.x)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "¿No tienes cuenta? Regístrate",
                color = Color(0xFF6C28D0),
                modifier = Modifier.clickable { onGoToRegister() }
            )
        }
    }
}

@Composable
fun SocialButton(logoResId: Int) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { /* Acción social */ },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
    }
}
