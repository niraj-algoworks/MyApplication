package com.example.myapplication

import kotlinx.serialization.Serializable


@Serializable
data class MyMessage(val timestamp: Long, val author: String, val content: String)