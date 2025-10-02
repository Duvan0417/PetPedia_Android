package com.example.primerproyecto.ui.view.register

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primerproyecto.data.model.RegisterRequest
import com.example.primerproyecto.ui.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onGoToLogin: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registerResponse by viewModel.registerResponse.collectAsState()

    val context = LocalContext.current

    // Mapeo de roles visuales a role_id
    val roleMapping = mapOf(
        "Veterinario" to 1,
        "Usuario" to 2,
        "Refugio" to 3,
        "Entrenador" to 4

        )

    val roles = listOf("Veterinario", "Usuario", "Refugio", "Entrenador")
    var selectedRole by remember { mutableStateOf("Usuario") }
    var expanded by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localErrorMessage by remember { mutableStateOf("") }

    // ✅ Campos específicos DECLARADOS FUERA del when
    var nombreClinica by remember { mutableStateOf("") }
    var licenciaVeterinaria by remember { mutableStateOf("") }
    var especializacion by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var anosExperiencia by remember { mutableStateOf("") }
    var cualificaciones by remember { mutableStateOf("") }
    var tarifaHora by remember { mutableStateOf("") }
    var nombreRefugio by remember { mutableStateOf("") }
    var personaResponsable by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }

    // Manejar respuesta del registro
    LaunchedEffect(registerResponse) {
        registerResponse?.let { response ->
            val token = response.token
            if (token != null) {
                onRegisterSuccess(token)
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
                Brush.verticalGradient(
                    listOf(Color(0xFF6C28D0), Color(0xFF9C4DFF))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Crear Cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Completa tus datos para registrarte",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Estado de loading
                    if (isLoading) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Registrando...", color = Color.Gray)
                        }
                    } else {
                        // Selección de rol
                        OutlinedTextField(
                            value = selectedRole,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Selecciona un rol") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(Icons.Default.ArrowDropDown, null)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = !expanded }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            roles.forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role) },
                                    onClick = {
                                        selectedRole = role
                                        expanded = false
                                        // ✅ CORREGIDO: Limpiar campos específicos al cambiar rol
                                        nombreClinica = ""
                                        licenciaVeterinaria = ""
                                        especializacion = ""
                                        especialidad = ""
                                        anosExperiencia = ""
                                        cualificaciones = ""
                                        tarifaHora = ""
                                        nombreRefugio = ""
                                        personaResponsable = ""
                                        capacidad = ""
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Campos del formulario
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (selectedRole == "Usuario") {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = apellido,
                                onValueChange = { apellido = it },
                                label = { Text("Apellido") },
                                leadingIcon = { Icon(Icons.Default.PersonOutline, null) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = telefono,
                            onValueChange = { telefono = it },
                            label = { Text("Teléfono") },
                            leadingIcon = { Icon(Icons.Default.Phone, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = direccion,
                            onValueChange = { direccion = it },
                            label = { Text("Dirección") },
                            leadingIcon = { Icon(Icons.Default.Home, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Campos específicos por rol
                        when (selectedRole) {
                            "Veterinario" -> {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = nombreClinica,
                                    onValueChange = { nombreClinica = it },
                                    label = { Text("Nombre de la Clínica") },
                                    leadingIcon = { Icon(Icons.Default.Business, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = licenciaVeterinaria,
                                    onValueChange = { licenciaVeterinaria = it },
                                    label = { Text("Licencia Veterinaria") },
                                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = especializacion,
                                    onValueChange = { especializacion = it },
                                    label = { Text("Especialización") },
                                    leadingIcon = { Icon(Icons.Default.School, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            "Entrenador" -> {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = especialidad,
                                    onValueChange = { especialidad = it },
                                    label = { Text("Especialidad") },
                                    leadingIcon = { Icon(Icons.Default.Sports, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = anosExperiencia,
                                    onValueChange = { anosExperiencia = it },
                                    label = { Text("Años de Experiencia") },
                                    leadingIcon = { Icon(Icons.Default.Work, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = cualificaciones,
                                    onValueChange = { cualificaciones = it },
                                    label = { Text("Cualificaciones") },
                                    leadingIcon = { Icon(Icons.Default.School, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = tarifaHora,
                                    onValueChange = { tarifaHora = it },
                                    label = { Text("Tarifa por Hora") },
                                    leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            "Refugio" -> {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = nombreRefugio,
                                    onValueChange = { nombreRefugio = it },
                                    label = { Text("Nombre del Refugio") },
                                    leadingIcon = { Icon(Icons.Default.Business, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = personaResponsable,
                                    onValueChange = { personaResponsable = it },
                                    label = { Text("Persona Responsable") },
                                    leadingIcon = { Icon(Icons.Default.Person, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = capacidad,
                                    onValueChange = { capacidad = it },
                                    label = { Text("Capacidad") },
                                    leadingIcon = { Icon(Icons.Default.Home, null) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmar Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

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

                        Button(
                            onClick = {
                                // Validaciones
                                if (nombre.isBlank() || correo.isBlank() || password.isBlank() || confirmPassword.isBlank() ||
                                    telefono.isBlank() || direccion.isBlank()) {
                                    localErrorMessage = "Por favor completa todos los campos obligatorios"
                                    return@Button
                                }
                                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                                    localErrorMessage = "Correo electrónico inválido"
                                    return@Button
                                }
                                if (password != confirmPassword) {
                                    localErrorMessage = "Las contraseñas no coinciden"
                                    return@Button
                                }
                                if (password.length < 8) {
                                    localErrorMessage = "La contraseña debe tener al menos 8 caracteres"
                                    return@Button
                                }

                                localErrorMessage = ""

                                val roleId = roleMapping[selectedRole] ?: 3
                                val fullName = if (selectedRole == "Usuario") "$nombre $apellido" else nombre

                                val registerRequest = RegisterRequest(
                                    name = fullName,
                                    email = correo,
                                    password = password,
                                    password_confirmation = confirmPassword,
                                    role_id = roleId,
                                    phone = telefono,
                                    address = direccion,
                                    clinic_name = if (selectedRole == "Veterinario") nombreClinica else null,
                                    veterinary_license = if (selectedRole == "Veterinario") licenciaVeterinaria else null,
                                    specialization = if (selectedRole == "Veterinario") especializacion else null,
                                    specialty = if (selectedRole == "Entrenador") especialidad else null,
                                    experience_years = if (selectedRole == "Entrenador") anosExperiencia.toIntOrNull() ?: 0 else null,
                                    qualifications = if (selectedRole == "Entrenador") cualificaciones else null,
                                    hourly_rate = if (selectedRole == "Entrenador") tarifaHora.toDoubleOrNull() ?: 0.0 else null,
                                    shelter_name = if (selectedRole == "Refugio") nombreRefugio else null,
                                    responsible_person = if (selectedRole == "Refugio") personaResponsable else null,
                                    capacity = if (selectedRole == "Refugio") capacidad.toIntOrNull() ?: 0 else null
                                )

                                viewModel.registerUser(registerRequest)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C28D0))
                        ) {
                            Text("Registrar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "¿Ya tienes cuenta? Inicia sesión",
                        color = Color(0xFF6C28D0),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onGoToLogin() }
                    )
                }
            }
        }
    }
}