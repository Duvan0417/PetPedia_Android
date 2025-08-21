package com.example.primerproyecto.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primerproyecto.R

// Paleta de colores mejorada
object PetPediaColors {
    val Primary = Color(0xFF6366F1)
    val PrimaryDark = Color(0xFF4F46E5)
    val Secondary = Color(0xFF8B5CF6)
    val Accent = Color(0xFF06B6D4)
    val Background = Color(0xFFF8FAFC)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF1F5F9)
    val OnSurface = Color(0xFF1E293B)
    val OnSurfaceVariant = Color(0xFF64748B)
    val Success = Color(0xFF10B981)
    val Warning = Color(0xFFF59E0B)
}

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PetPediaColors.Background)
            .verticalScroll(scrollState)
    ) {
        // Header con barra de búsqueda
        HeaderSection(searchText = searchText) { searchText = it }

        // Carrusel principal
        MainCarousel()

        // Botones de acción rápida
        QuickActionsSection()

        // Sección de bienvenida
        WelcomeSection()

        // Categorías rápidas
        QuickCategoriesSection()

        // Servicios principales
        ServicesSection()

        // Recomendaciones personalizadas
        RecommendationsSection()

        // Promociones especiales
        PromotionsSection()

        // Tips y consejos
        TipsSection()

        // Testimonios de usuarios
        TestimonialsSection()

        // Noticias y destacados
        NewsSection()

        // Espaciado final
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ===================== HEADER SECTION =====================
@Composable
private fun HeaderSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PetPediaColors.Primary,
                        PetPediaColors.PrimaryDark
                    )
                )
            )
            .padding(16.dp)
    ) {
        // Saludo y logo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "¡Hola! 👋",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Bienvenido a PetPedia",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Icono de perfil o notificaciones
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text("🐕", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de búsqueda mejorada
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(28.dp)),
            placeholder = {
                Text(
                    "Buscar servicios, productos...",
                    color = PetPediaColors.OnSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = PetPediaColors.Primary
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

// ===================== MAIN CAROUSEL =====================
@Composable
private fun MainCarousel() {
    var currentIndex by remember { mutableStateOf(0) }
    val images = listOf(
        R.drawable.dog_tips,
        R.drawable.dog_tips2,
        R.drawable.adopcion3
    )

    LaunchedEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                currentIndex = (currentIndex + 1) % images.size
                handler.postDelayed(this, 4000)
            }
        }
        handler.post(runnable)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(220.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box {
            Image(
                painter = painterResource(id = images[currentIndex]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Degradado mejorado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Contenido superpuesto
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Text(
                    text = "Cuidamos lo que más amas ❤️",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Servicios veterinarios de confianza",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Indicadores de carrusel
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(images.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentIndex) 12.dp else 8.dp)
                            .background(
                                if (index == currentIndex) Color.White else Color.White.copy(alpha = 0.5f),
                                CircleShape
                            )
                    )
                }
            }
        }
    }
}

// ===================== QUICK ACTIONS =====================
@Composable
private fun QuickActionsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = PetPediaColors.Primary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "📅 Agendar Cita",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                PetPediaColors.Primary
            )
        ) {
            Text(
                "🛍️ Explorar Tienda",
                fontWeight = FontWeight.SemiBold,
                color = PetPediaColors.Primary
            )
        }
    }
}

// ===================== WELCOME SECTION =====================
@Composable
private fun WelcomeSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = PetPediaColors.Surface
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tu mascota merece lo mejor",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PetPediaColors.OnSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Plataforma integral para el cuidado y felicidad de tus compañeros peludos.",
                    fontSize = 14.sp,
                    color = PetPediaColors.OnSurfaceVariant,
                    lineHeight = 20.sp
                )
            }

            Text("🐾", fontSize = 40.sp)
        }
    }
}

// ===================== QUICK CATEGORIES =====================
@Composable
private fun QuickCategoriesSection() {
    val categories = listOf(
        Triple("Veterinaria", "🏥", PetPediaColors.Success),
        Triple("Tienda", "🛍️", PetPediaColors.Primary),
        Triple("Adopciones", "🏠", PetPediaColors.Secondary),
        Triple("Entrenamiento", "🎾", PetPediaColors.Accent)
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Servicios principales",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PetPediaColors.OnSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(categories) { (name, emoji, color) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { }
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(8.dp, CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        color.copy(alpha = 0.8f),
                                        color
                                    ),
                                    radius = 120f
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Efecto de brillo interno
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.3f),
                                            Color.Transparent
                                        ),
                                        radius = 60f
                                    ),
                                    CircleShape
                                )
                        )
                        Text(
                            emoji,
                            fontSize = 28.sp,
                            modifier = Modifier.offset(y = (-2).dp) // Ligero efecto de elevación
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PetPediaColors.OnSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ===================== SERVICES SECTION =====================
data class ServiceItem(
    val title: String,
    val icon: Int,
    val description: String,
    val color: Color
)

@Composable
private fun ServicesSection() {
    val services = listOf(
        ServiceItem(
            "Veterinaria 24/7",
            R.drawable.veterinary,
            "Atención médica profesional las 24 horas del día.",
            PetPediaColors.Success
        ),
        ServiceItem(
            "Entrenamiento",
            R.drawable.pet_hotel2,
            "Entrenadores certificados con métodos positivos.",
            PetPediaColors.Primary
        ),
        ServiceItem(
            "Adopciones",
            R.drawable.adopcion,
            "Encuentra tu compañero perfecto y cambia una vida.",
            PetPediaColors.Secondary
        ),
        ServiceItem(
            "Productos Premium",
            R.drawable.collarp,
            "Alimentos, juguetes y accesorios de alta calidad.",
            PetPediaColors.Accent
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Nuestros servicios",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PetPediaColors.OnSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(services) { service ->
                ServiceCard(service)
            }
        }
    }
}

@Composable
private fun ServiceCard(service: ServiceItem) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(340.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .clickable { },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = PetPediaColors.Surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen con overlay colorido
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = service.icon),
                    contentDescription = service.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    service.color.copy(alpha = 0.3f)
                                )
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = service.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PetPediaColors.OnSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = service.description,
                    fontSize = 14.sp,
                    color = PetPediaColors.OnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = service.color
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Ver más",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ===================== RECOMMENDATIONS =====================
@Composable
private fun RecommendationsSection() {
    val products = listOf(
        Triple("Collar Inteligente", "$45.000", R.drawable.collarp),
        Triple("Kit de Aseo Premium", "$32.000", R.drawable.pet_shop),
        Triple("Casa para Mascotas", "$120.000", R.drawable.adopcion)
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recomendados para ti",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PetPediaColors.OnSurface
            )

            TextButton(onClick = { }) {
                Text(
                    "Ver todos",
                    color = PetPediaColors.Primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
        ) {
            items(products.size) { index ->
                ProductCard(
                    name = products[index].first,
                    price = products[index].second,
                    imageRes = products[index].third
                )
            }
        }
    }
}

@Composable
private fun ProductCard(name: String, price: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(260.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clickable { },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = PetPediaColors.Surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Etiqueta de descuento (opcional)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            PetPediaColors.Warning,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "-20%",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PetPediaColors.OnSurface,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = PetPediaColors.Warning,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "4.8",
                        fontSize = 12.sp,
                        color = PetPediaColors.OnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PetPediaColors.Primary
                    )

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                PetPediaColors.Primary,
                                CircleShape
                            )
                    ) {
                        Text("🛒", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

// ===================== PROMOTIONS =====================
@Composable
private fun PromotionsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = PetPediaColors.Primary
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "🎉 Oferta Especial",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "20% de descuento en alimentos premium esta semana",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Aprovechar oferta",
                        color = PetPediaColors.Primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text("🎁", fontSize = 60.sp)
        }
    }
}

// ===================== TIPS SECTION =====================
@Composable
private fun TipsSection() {
    val tips = listOf(
        "Mantén las vacunas al día" to "Las vacunas son esenciales para prevenir enfermedades graves.",
        "Ejercicio diario" to "El ejercicio regular mantiene a tu mascota saludable y feliz.",
        "Alimentación balanceada" to "Una dieta equilibrada es clave para la longevidad de tu mascota."
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Tips para tu mascota 💡",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PetPediaColors.OnSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(tips.size) { index ->
                TipCard(
                    title = tips[index].first,
                    description = tips[index].second,
                    index = index + 1
                )
            }
        }
    }
}

@Composable
private fun TipCard(title: String, description: String, index: Int) {
    Card(
        modifier = Modifier
            .width(240.dp)
            .height(200.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .clickable { },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = PetPediaColors.Surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            PetPediaColors.Primary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$index",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Tip #$index",
                    color = PetPediaColors.Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PetPediaColors.OnSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                color = PetPediaColors.OnSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

// ===================== TESTIMONIALS =====================
data class Testimonial(
    val name: String,
    val text: String,
    val rating: Float = 5f
)

@Composable
private fun TestimonialsSection() {
    val testimonials = listOf(
        Testimonial(
            "Andrea G.",
            "Gracias a PetPedia adopté a mi perrita Luna y encontré un veterinario increíble."
        ),
        Testimonial(
            "Carlos M.",
            "La tienda tiene todo lo que necesito, envíos rápidos y productos de calidad."
        ),
        Testimonial(
            "Valentina R.",
            "El servicio de entrenadores me ayudó muchísimo con mi cachorro travieso."
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Lo que dicen nuestros usuarios ⭐",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PetPediaColors.OnSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(testimonials) { testimonial ->
                TestimonialCard(testimonial)
            }
        }
    }
}

@Composable
private fun TestimonialCard(testimonial: Testimonial) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = PetPediaColors.Surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            PetPediaColors.Primary.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        testimonial.name.first().toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PetPediaColors.Primary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        testimonial.name,
                        fontWeight = FontWeight.SemiBold,
                        color = PetPediaColors.OnSurface
                    )
                    Row {
                        repeat(5) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = PetPediaColors.Warning,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "\"${testimonial.text}\"",
                fontSize = 14.sp,
                color = PetPediaColors.OnSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

// ===================== NEWS SECTION =====================
@Composable
private fun NewsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = PetPediaColors.Secondary
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💉", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        "Campaña de Vacunación Gratuita",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Este mes • Evento especial",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Durante todo el mes ofrecemos vacunación gratuita para perros y gatos rescatados. ¡Una oportunidad única para cuidar a quienes más lo necesitan!",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Más información",
                        color = PetPediaColors.Secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedButton(
                    onClick = { },
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Compartir",
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}