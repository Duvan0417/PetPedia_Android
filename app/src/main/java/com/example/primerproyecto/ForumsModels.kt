package com.example.primerproyecto


data class Post(val id: Int, val title: String, val content: String, val author: String)
data class Comment(val id: Int, val author: String, val content: String)
data class Group(val id: Int, val name: String, val description: String, var joined: Boolean = false)
