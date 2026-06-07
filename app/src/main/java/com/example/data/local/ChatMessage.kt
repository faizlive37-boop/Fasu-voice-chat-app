package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderName: String,
    val text: String,
    val timeString: String,
    val isFromMe: Boolean,
    val partnerId: String // Group chats or direct dm partner
)
