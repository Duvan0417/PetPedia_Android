package com.example.primerproyecto

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// -------------------------------------------------
// Modelo local (evita conflictos con otras clases)
// -------------------------------------------------
data class HomeService(
    val name: String,
    @DrawableRes val images: List<Int>,
    val description: String
)

// -------------------------------------------------
// HomeScreen: encabezado + búsqueda + carruseles
// -------------------------------------------------
@Composable
fun HomeScreen(initialSearch: String = "", searchQuery: String) {
    var searchQuery by remember { mutableStateOf(initialSearch) }

    val services = getHomeServicesData()
        .filter {
            it.name.contains(searchQuery, ignoreCase = true)
                    || it.description.contains(searchQuery, ignoreCase = true)
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header: logo, título, descripción y buscador
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logopet), // pon tu logo en drawable
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Petpedia",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tu centro integral para el cuidado, adopción y bienestar de tu mascota.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Buscador redondeado y moderno
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar servicios...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }
        }

        // Título sección
        item {
            Text(
                text = "Nuestros Servicios",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Lista de servicios (cada uno con carrusel interno)
        items(services) { service ->
            ServiceWithGlassCard(service = service)
        }
    }
}

// -------------------------------------------------
// Ejemplos de datos (usa tus drawables reales aquí)
// -------------------------------------------------
private fun getHomeServicesData(): List<HomeService> {
    return listOf(
        HomeService(
            name = "Adopción",
            images = listOf(R.drawable.adopcion, R.drawable.adopcion2, R.drawable.adopcion3),
            description = "Encuentra a tu compañero perfecto."
        ),
        HomeService(
            name = "Veterinarios",
            images = listOf(R.drawable.veterinary, R.drawable.veterinary2),
            description = "Cuidado profesional para tu mascota."
        ),
        HomeService(
            name = "Tienda",
            images = listOf(R.drawable.pet_shop, R.drawable.pet_shop2),
            description = "Todo lo que necesitas para tu mascota."
        ),
        HomeService(
            name = "Paseadores",
            images = listOf(R.drawable.clinica, R.drawable.pet_hotel2),
            description = "Cuidamos a tu mascota como si fueras tú."
        )
    )
}

// -------------------------------------------------
// Componente: card con carrusel de imágenes
// conserva controles prev/next y dots (no lo quité)
// -------------------------------------------------
@Composable
fun ServiceWithGlassCard(service: HomeService) {
    var currentImageIndex by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Título + descripción breve
            Text(
                text = service.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = service.description,
                fontSize = 14.sp,
                color = Color(0xFF6D6D6D)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Carrusel (imagen, prev/next y dots)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                // Imagen principal
                Image(
                    painter = painterResource(id = service.images[currentImageIndex]),
                    contentDescription = "Imagen de ${service.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )

                // Dots indicadores
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(service.images.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (currentImageIndex == index) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (currentImageIndex == index) Color.White
                                    else Color.White.copy(alpha = 0.45f)
                                )
                        )
                    }
                }

                // Botón anterior
                IconButton(
                    onClick = {
                        currentImageIndex =
                            if (currentImageIndex == 0) service.images.lastIndex
                            else currentImageIndex - 1
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.28f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Anterior",
                        tint = Color.White
                    )
                }

                // Botón siguiente
                IconButton(
                    onClick = {
                        currentImageIndex = (currentImageIndex + 1) % service.images.size
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.28f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Siguiente",
                        tint = Color.White
                    )
                }
            } // box carrusel
        } // column card
    } // card
}
