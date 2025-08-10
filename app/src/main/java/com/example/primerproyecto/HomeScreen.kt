package com.example.primerproyecto

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primerproyecto.R

// Data class para los servicios
data class Service(
    val name: String,
    @DrawableRes val images: List<Int>,
    val description: String
)

/**
 * Pantalla principal de inicio que muestra los servicios disponibles
 * @param searchQuery: String de búsqueda (para futuras implementaciones de filtrado)
 */
@Composable
fun HomeScreen(searchQuery: String) {
    val services = getServicesData()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "Nuestros Servicios",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(services) { service ->
            ServiceWithGlassCard(service)
        }
    }
}

/**
 * Card individual para mostrar cada servicio con carrusel de imágenes
 * @param service: Objeto Service con la información del servicio
 */
@Composable
fun ServiceWithGlassCard(service: Service) {
    var currentImageIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título del servicio
        Text(
            text = service.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Descripción del servicio
        Text(
            text = service.description,
            fontSize = 16.sp,
            color = Color(0xFF6D6D6D),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Card con imagen y controles de carrusel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentAlignment = Alignment.Center
        ) {
            // Imagen principal
            Image(
                painter = painterResource(id = service.images[currentImageIndex]),
                contentDescription = "Imagen de ${service.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            )

            // Indicadores de página (dots)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(service.images.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentImageIndex == index) Color.White
                                else Color.White.copy(alpha = 0.4f)
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
                    .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Imagen anterior",
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
                    .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Imagen siguiente",
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * Función que retorna la lista de servicios disponibles
 * Separada para facilitar el mantenimiento y posibles cambios futuros
 */
private fun getServicesData(): List<Service> {
    return listOf(
        Service(
            name = "Adopción",
            images = listOf(R.drawable.adopcion, R.drawable.adopcion2, R.drawable.adopcion3),
            description = "Encuentra a tu compañero perfecto"
        ),
        Service(
            name = "Veterinarios",
            images = listOf(R.drawable.veterinary, R.drawable.veterinary2),
            description = "Cuidado profesional para tu mascota"
        ),
        Service(
            name = "Tienda",
            images = listOf(R.drawable.pet_shop, R.drawable.pet_shop2),
            description = "Todo lo que necesitas para tu mascota"
        ),
        Service(
            name = "Paseadores",
            images = listOf(R.drawable.clinica, R.drawable.pet_hotel2),
            description = "Cuidamos a tu mascota como si fueras tú"
        ),
        Service(
            name = "Consejos",
            images = listOf(R.drawable.dog_tips, R.drawable.dog_tips2),
            description = "Las mejores recomendaciones para tu mascota"
        )
    )
}