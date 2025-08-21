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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primerproyecto.R

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // 🔹 Barra de búsqueda en la parte superior
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Buscar productos, servicios...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        // 🔹 Carrusel superior
        ImageCarousel(
            images = listOf(
                R.drawable.dog_tips,
                R.drawable.dog_tips2,
                R.drawable.adopcion3
            )
        )

        // 🔹 Botones de acción rápida
        QuickActions()

        // 🔹 Zona bienvenida
        WelcomeSection()

        // 🔹 Categorías rápidas
        QuickCategories()

        // 🔹 Servicios principales
        ServiceSection(
            items = listOf(
                ServiceItem("Veterinaria", R.drawable.veterinary, "Atención 24/7 para tu mascota."),
                ServiceItem("Entrenador", R.drawable.pet_hotel2, "Entrenamiento positivo y profesional."),
                ServiceItem("Adopciones", R.drawable.adopcion, "Encuentra tu mejor amigo."),
                ServiceItem("Productos", R.drawable.collarp, "Alimentos, juguetes y más.")
            )
        )

        // 🔹 Recomendaciones
        RecommendationsSection()

        // 🔹 Promociones
        PromotionsSection()

        // 🔹 Tips o Blog
        TipsSection()

        // 🔹 Testimonios
        TestimonialSection()

        // 🔹 Noticias o destacados
        HighlightSection()
    }
}


// ---------------- CARRUSEL ----------------
@Composable
fun ImageCarousel(images: List<Int>) {
    var currentIndex by remember { mutableStateOf(0) }

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
        shape = RoundedCornerShape(20.dp), // 🔹 bordes redondeados
        elevation = CardDefaults.cardElevation(8.dp), // 🔹 sombra/elevación
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // 🔹 no ocupa todo el ancho
            .height(200.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = images[currentIndex]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 🔹 Degradado oscuro en la parte baja
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xAA000000))
                        )
                    )
            )

            Text(
                text = "Cuidamos lo que más amas ❤️",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}



// ---------------- BOTONES DE ACCIÓN RÁPIDA ----------------
@Composable
fun QuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2))
        ) {
            Text("Agendar cita", color = Color.White)
        }
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A148C))
        ) {
            Text("Explorar tienda", color = Color.White)
        }
    }
}


// ---------------- BIENVENIDA ----------------
@Composable
fun WelcomeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Bienvenido a PetCare",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A148C)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "La plataforma integral para el cuidado y felicidad de tus mascotas.",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

// ---------------- CATEGORÍAS RÁPIDAS ----------------
@Composable
fun QuickCategories() {
    val categories = listOf(
        "Citas" to R.drawable.veterinary,
        "Tienda" to R.drawable.pet_shop,
        "Rescates" to R.drawable.adopcion3,
        "Entreno" to R.drawable.pet_hotel2
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(categories) { (name, icon) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = name,
                    modifier = Modifier
                        .size(70.dp) // mismo tamaño del círculo
                        .clip(CircleShape) // recorta la imagen circular
                        .clickable { },
                    contentScale = ContentScale.Crop // rellena todo el círculo
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


// ---------------- SERVICIOS ----------------
data class ServiceItem(val title: String, val icon: Int, val description: String)

@Composable
fun ServiceSection(items: List<ServiceItem>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Nuestros servicios",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(items) { service ->
                ServiceCard(service)
            }
        }
    }
}

@Composable
fun ServiceCard(service: ServiceItem) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(320.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7FF))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = service.icon),
                contentDescription = service.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp), // Imagen grande arriba
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = service.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A148C)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = service.description,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Ver más", color = Color.White)
            }
        }
    }
}

// ---------------- RECOMENDACIONES ----------------
@Composable
fun RecommendationsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Recomendados para ti",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val productos = listOf(
            R.drawable.collarp,
            R.drawable.pet_shop,
            R.drawable.adopcion
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(productos.size) { index ->
                Card(
                    modifier = Modifier
                        .width(220.dp)
                        .height(320.dp),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7FF)) // ✅ mismo color que servicios
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Imagen grande arriba (igual que en servicios)
                        Image(
                            painter = painterResource(id = productos[index]),
                            contentDescription = "Producto $index",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Nombre del producto
                        Text(
                            "Producto ${index + 1}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF4A148C) // ✅ mismo morado de servicios
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Precio
                        Text(
                            "Desde $20.000",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Botón de acción
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)), // ✅ igual que servicios
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Comprar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}



// ---------------- PROMOCIONES ----------------
@Composable
fun PromotionsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Promociones",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🎉 20% de descuento en alimentos premium", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Solo esta semana en la tienda PetCare.", color = Color.DarkGray)
            }
        }
    }
}

// ---------------- TIPS ----------------
@Composable
fun TipsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Tips para tu mascota",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(3) {
                Card(
                    modifier = Modifier
                        .width(220.dp)
                        .height(220.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), // fondo blanco
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column {
                        // 🔹 Imagen que ocupa todo el ancho
                        Image(
                            painter = painterResource(id = R.drawable.dog_tips), // cámbialo por la imagen que quieras
                            contentDescription = "Tip imagen",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp), // la mitad de la tarjeta
                            contentScale = ContentScale.Crop
                        )

                        // 🔹 Texto debajo de la imagen
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("🐾 Tip ${it + 1}", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Recuerda mantener al día sus vacunas y chequeos veterinarios.",
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- TESTIMONIOS ----------------
data class Testimonial(
    val name: String,
    val text: String,
    val photo: Int
)

@Composable
fun TestimonialSection() {
    val testimonials = listOf(
        Testimonial(
            "Andrea G.",
            "Gracias a PetCare adopté a mi perrita Luna y encontré un veterinario increíble. ¡Recomendado 100%!",
            R.drawable.logopet // 🔹 reemplaza con tus imágenes
        ),
        Testimonial(
            "Carlos M.",
            "La tienda de productos tiene todo lo que necesito, los envíos son rápidos y de calidad.",
            R.drawable.adopcion3
        ),
        Testimonial(
            "Valentina R.",
            "El servicio de entrenadores me ayudó muchísimo con mi cachorro. ¡Muy profesionales!",
            R.drawable.adopcion3
        ),
        Testimonial(
            "Juan P.",
            "La app es súper completa, pude agendar mi cita en minutos y todo salió perfecto.",
            R.drawable.adopcion3
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Lo que dicen nuestros usuarios",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(testimonials) { testimonial ->
                Card(
                    modifier = Modifier
                        .width(280.dp)
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7FF))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Foto de perfil
                        Image(
                            painter = painterResource(id = testimonial.photo),
                            contentDescription = testimonial.name,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Texto y nombre
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(
                                "“${testimonial.text}”",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "- ${testimonial.name}",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4A148C)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- DESTACADOS ----------------
@Composable
fun HighlightSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Destacados",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Campaña de Vacunación",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF4A148C)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Durante este mes ofrecemos vacunación gratuita para perros y gatos rescatados. ¡No faltes!",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}
