package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class ConfigMessage(
    var type: MessageType?,
    var content: String?,
    var sender: String?,
    var receiver: String
    ) : java.io.Serializable

enum class MessageType {
    REFRESH_REQUEST
}
