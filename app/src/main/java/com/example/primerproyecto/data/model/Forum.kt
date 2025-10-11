package com.example.primerproyecto.data.model

import com.google.gson.annotations.SerializedName

data class Forum(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("content")
    val content: String,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("likes_count")
    val likesCount: Int = 0,

    @SerializedName("comments_count")
    val commentsCount: Int = 0,

    // ✅ SOLUCIÓN: Usar un tipo genérico y convertir después
    @SerializedName("comments")
    private val commentsRaw: Any? = null,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("creation_date")
    val creationDate: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String? = null
) {
    // ✅ Propiedad computada para manejar comments de forma segura
    val safeComments: List<ForumComment>
        get() = parseComments(commentsRaw)

    private fun parseComments(rawData: Any?): List<ForumComment> {
        return try {
            when (rawData) {
                is List<*> -> {
                    // Ya es una lista de objetos
                    @Suppress("UNCHECKED_CAST")
                    rawData as? List<ForumComment> ?: emptyList()
                }
                is String -> {
                    // Es un string JSON, necesitamos convertirlo
                    if (rawData.isBlank()) {
                        emptyList()
                    } else {
                        try {
                            // Intenta parsear el JSON string
                            val gson = com.google.gson.Gson()
                            val arrayType = com.google.gson.reflect.TypeToken.getParameterized(
                                Array::class.java,
                                ForumComment::class.java
                            ).type
                            gson.fromJson<Array<ForumComment>>(rawData, arrayType).toList()
                        } catch (e: Exception) {
                            println("❌ Error parsing comments JSON: ${e.message}")
                            emptyList()
                        }
                    }
                }
                else -> emptyList()
            }
        } catch (e: Exception) {
            println("❌ Error general parsing comments: ${e.message}")
            emptyList()
        }
    }
}

// ✅ ACTUALIZA ForumComment para que coincida con Laravel
data class ForumComment(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("user_name")  // ✅ Cambiar de 'user' a 'user_name'
    val userName: String? = null,

    @SerializedName("content")
    val content: String,

    // ✅ Mantener 'user' por si acaso, pero hacerlo opcional
    @SerializedName("user")
    val user: User? = null,

    @SerializedName("created_at")
    val createdAt: String

    // ❌ REMOVER: 'post_id' y 'updated_at' no existen en tu estructura Laravel
    // @SerializedName("post_id") val postId: Int, // ❌ NO EXISTE en tu estructura
    // @SerializedName("updated_at") val updatedAt: String? = null // ❌ NO EXISTE
)

// Request models (están bien)
data class ForumPostRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("content")
    val content: String,

    @SerializedName("image")
    val image: String? = null
)

data class ForumCommentRequest(
    @SerializedName("content")
    val content: String
)

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null
)