package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE partnerId = :partnerId ORDER BY id ASC")
    fun getMessagesForPartner(partnerId: String): Flow<List<ChatMessage>>

    @Insert
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE partnerId = :partnerId")
    suspend fun clearChatHistory(partnerId: String)
}
