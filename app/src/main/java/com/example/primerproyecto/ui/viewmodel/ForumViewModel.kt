// Tu ForumViewModel.kt ACTUALIZADO
package com.example.primerproyecto.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerproyecto.data.model.SessionManager
import com.example.primerproyecto.data.Apiservice.RetrofitService
import com.example.primerproyecto.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForumViewModel(private val context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _posts = MutableStateFlow<List<Forum>>(emptyList())
    val posts: StateFlow<List<Forum>> = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val apiService = RetrofitService.apiService

    fun loadPosts() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                println("🔄 Cargando posts desde API...")
                val response = apiService.getAllPosts()

                println("📡 Response code: ${response.code()}")
                println("📡 Response isSuccessful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    println("📡 API Success: ${apiResponse?.success}")

                    if (apiResponse?.success == true) {
                        _posts.value = apiResponse.data ?: emptyList()
                        println("✅ Posts cargados: ${_posts.value.size}")

                        // ✅ USAR safeComments
                        _posts.value.forEach { post ->
                            println("📊 Post ${post.id}: ${post.safeComments.size} comentarios")
                        }
                    } else {
                        _errorMessage.value = apiResponse?.message ?: "Error al cargar publicaciones"
                        println("❌ Error en API: ${apiResponse?.message}")
                    }
                } else {
                    _errorMessage.value = "Error del servidor: ${response.code()}"
                    println("❌ Error del servidor: ${response.code()}")
                    try {
                        val errorBody = response.errorBody()?.string()
                        println("❌ Error body: $errorBody")
                    } catch (e: Exception) {
                        println("❌ No se pudo leer error body")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
                println("❌ Error de conexión: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                println("🏁 Loading terminado")
            }
        }
    }

    // ✅ ACTUALIZAR: Crear nuevo post con todos los parámetros
    fun createPost(title: String, content: String, description: String? = null, image: String? = null) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val authToken = sessionManager.getAuthToken()
                val userId = sessionManager.getUserId()
                val userName = sessionManager.getUserName()

                println("🔐 DEBUG: User ID: $userId, Name: $userName")
                println("🔐 DEBUG: Token: ${authToken?.take(20)}...")

                if (authToken.isNullOrEmpty()) {
                    _errorMessage.value = "Debes iniciar sesión para crear una publicación"
                    _isLoading.value = false
                    return@launch
                }

                // ✅ ENVIAR todos los parámetros
                val postRequest = ForumPostRequest(
                    title = title,
                    content = content,
                    description = description, // ✅ Ahora sí envía descripción
                    image = image              // ✅ Ahora sí envía imagen
                )

                println("🔄 Enviando POST a /forums")
                println("📝 Título: $title")
                println("📝 Contenido: $content")
                println("📝 Descripción: $description")
                println("🖼️ Imagen: $image")

                val response = apiService.createForumPost("Bearer $authToken", postRequest)

                println("📡 Response code: ${response.code()}")
                println("📡 Response isSuccessful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    println("✅ API Response success: ${apiResponse?.success}")
                    println("✅ API Response message: ${apiResponse?.message}")

                    if (apiResponse?.success == true) {
                        println("🎉 Post creado exitosamente!")
                        loadPosts() // Recargar la lista de posts
                    } else {
                        val errorMsg = apiResponse?.message ?: "Error al crear publicación"
                        println("❌ Error del API: $errorMsg")
                        _errorMessage.value = errorMsg
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("❌ Error del servidor - Código: ${response.code()}")
                    println("❌ Error body: $errorBody")

                    when (response.code()) {
                        401 -> _errorMessage.value = "Sesión expirada. Por favor inicia sesión nuevamente."
                        422 -> _errorMessage.value = "Error de validación: $errorBody"
                        500 -> _errorMessage.value = "Error interno del servidor"
                        else -> _errorMessage.value = "Error del servidor: ${errorBody ?: response.code()}"
                    }
                }
            } catch (e: Exception) {
                println("💥 Excepción en createPost: ${e.message}")
                _errorMessage.value = "Error de conexión: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Agregar comentario a un post
    fun addComment(forumId: Int, content: String) {
        viewModelScope.launch {
            try {
                val authToken = sessionManager.getAuthToken()
                if (authToken.isNullOrEmpty()) {
                    _errorMessage.value = "Debes iniciar sesión para comentar"
                    return@launch
                }

                println("💬 Agregando comentario al post $forumId")
                println("📝 Contenido: $content")

                val commentRequest = ForumCommentRequest(content = content)
                val response = apiService.addComment("Bearer $authToken", forumId, commentRequest)

                println("📡 Response code: ${response.code()}")
                println("📡 Response isSuccessful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        println("✅ Comentario agregado exitosamente")
                        loadPosts()
                    } else {
                        _errorMessage.value = apiResponse?.message ?: "Error al agregar comentario"
                        println("❌ Error al agregar comentario: ${apiResponse?.message}")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error del servidor: ${errorBody ?: response.code()}"
                    println("❌ Error del servidor al comentar: $errorBody")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al agregar comentario: ${e.message}"
                println("💥 Excepción en addComment: ${e.message}")
            }
        }
    }

    // Dar like a un post
    fun toggleLike(forumId: Int) {
        viewModelScope.launch {
            try {
                val authToken = sessionManager.getAuthToken()
                if (authToken.isNullOrEmpty()) {
                    _errorMessage.value = "Debes iniciar sesión para dar like"
                    return@launch
                }

                println("❤️ Dando like al post $forumId")

                val response = apiService.toggleLike("Bearer $authToken", forumId)

                println("📡 Response code: ${response.code()}")
                println("📡 Response isSuccessful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        println("✅ Like agregado exitosamente")
                        loadPosts()
                    } else {
                        _errorMessage.value = apiResponse?.message ?: "Error al dar like"
                        println("❌ Error al dar like: ${apiResponse?.message}")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error del servidor: ${errorBody ?: response.code()}"
                    println("❌ Error del servidor al dar like: $errorBody")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al dar like: ${e.message}"
                println("💥 Excepción en toggleLike: ${e.message}")
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun formatRelativeTime(createdAt: String): String {
        // ... (mantener tu código actual igual)
        return try {
            val formats = arrayOf(
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd"
            )

            var postDate: java.util.Date? = null
            for (format in formats) {
                try {
                    val dateFormat = java.text.SimpleDateFormat(format, java.util.Locale.getDefault())
                    postDate = dateFormat.parse(createdAt)
                    break
                } catch (e: Exception) {
                    continue
                }
            }

            if (postDate == null) return createdAt

            val now = java.util.Date()
            val diff = now.time - postDate.time

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val weeks = days / 7
            val months = days / 30
            val years = days / 365

            when {
                years > 0 -> "Hace $years año" + if (years > 1) "s" else ""
                months > 0 -> "Hace $months mes" + if (months > 1) "es" else ""
                weeks > 0 -> "Hace $weeks semana" + if (weeks > 1) "s" else ""
                days > 0 -> "Hace $days día" + if (days > 1) "s" else ""
                hours > 0 -> "Hace $hours hora" + if (hours > 1) "s" else ""
                minutes > 0 -> "Hace $minutes minuto" + if (minutes > 1) "s" else ""
                else -> "Ahora mismo"
            }
        } catch (e: Exception) {
            createdAt
        }
    }
}