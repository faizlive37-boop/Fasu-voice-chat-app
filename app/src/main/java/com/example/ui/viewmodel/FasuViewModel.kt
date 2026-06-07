package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessage
import com.example.data.local.TaskItem
import com.example.data.repository.TaskRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CoinPackage(
    val id: String,
    val coins: Int,
    val priceInInr: Int,
    val description: String,
    val bonusGift: String
)

data class VoiceRoom(
    val id: String,
    val title: String,
    val hostName: String,
    val status: String,
    val listeners: Int,
    val tags: List<String>
)

data class ChatPartner(
    val id: String,
    val name: String,
    val subtitle: String,
    val avatarEmoji: String,
    val badge: String
)

class FasuViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val taskDao = database.taskDao()
    private val chatDao = database.chatDao()
    private val repository = TaskRepository(taskDao)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow("Artist_Faiz")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userAvatarIndex = MutableStateFlow(2) // Crown sticker default
    val userAvatarIndex: StateFlow<Int> = _userAvatarIndex.asStateFlow()

    private val _loginType = MutableStateFlow("Google")
    val loginType: StateFlow<String> = _loginType.asStateFlow()

    // Interactive user coin count (can be used to send virtual gifts)
    private val _userCoins = MutableStateFlow(3200)
    val userCoins: StateFlow<Int> = _userCoins.asStateFlow()

    // Owner payout UPI ID - Customizable
    private val _ownerUpiId = MutableStateFlow("faizlive37@okhdfcbank")
    val ownerUpiId: StateFlow<String> = _ownerUpiId.asStateFlow()

    // Pre-filled coin packages
    val coinPackages = listOf(
        CoinPackage("1", 500, 10, "Bronze Entry + Special Ring", "Bronze Ring"),
        CoinPackage("2", 5000, 99, "Silver Entry + Glowing Custom Badge", "Custom Avatar Frame"),
        CoinPackage("3", 25000, 499, "Gold Host Special + VIP Wedding Ring", "Special Golden Frame"),
        CoinPackage("4", 100000, 2499, "VIP Sapphire Whale Crown + Custom Entry Effect", "Royal Crown Animation")
    )

    private val _selectedPackage = MutableStateFlow(coinPackages[0])
    val selectedPackage: StateFlow<CoinPackage> = _selectedPackage.asStateFlow()

    // Voice Room dynamic states
    val voiceRooms = listOf(
        VoiceRoom("room_1", "Silence & Music Vibing Room", "Aanya ✨", "Muted, Chill Beats", 142, listOf("Acoustic", "Lofi")),
        VoiceRoom("room_2", "Ludo & Chai Adda", "RockerFaiz 🎧", "Ludo match live!", 389, listOf("Gaming", "Fun")),
        VoiceRoom("room_3", "Sania Unplugged 🎀", "Sania 🎀", "Singing old melodies", 955, listOf("Singing", "Live")),
        VoiceRoom("room_4", "Night Starry Talks", "Silent_Aman 🌠", "Sharing deep poetry", 84, listOf("Poetry", "LateNight"))
    )

    private val _activeVoiceRoom = MutableStateFlow(voiceRooms[0])
    val activeVoiceRoom: StateFlow<VoiceRoom> = _activeVoiceRoom.asStateFlow()

    // Chat partner states
    val chatPartners = listOf(
        ChatPartner("aanya", "Aanya ✨", "Active in Music Studio Cabin", "🎙️", "VIP"),
        ChatPartner("faiz", "RockerFaiz 🎧", "Let's play Ludo!", "🎮", "Host"),
        ChatPartner("sania", "Sania 🎀", "Online - Singing live soon!", "🎤", "Creator"),
        ChatPartner("admin", "SilentX Admin 🤖", "System Guard active", "🛡️", "Official")
    )

    private val _selectedPartner = MutableStateFlow(chatPartners[0])
    val selectedPartner: StateFlow<ChatPartner> = _selectedPartner.asStateFlow()

    // FlatMapLatest ensures chat room reactively updates when we change chat partner
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val activeChatMessagesFlow = _selectedPartner.flatMapLatest { partner ->
        chatDao.getMessagesForPartner(partner.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Observe tasks reactively from repository
    val tasksFlow: StateFlow<List<TaskItem>> = repository.allTasksFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Seed default roadmap tasks from repository if database is empty
        viewModelScope.launch {
            repository.ensureTasksPrepopulated()
            // Seed a welcome message for each chat partner if messages are empty
            seedInitialChatMessages()
        }
    }

    private suspend fun seedInitialChatMessages() {
        chatPartners.forEach { partner ->
            chatDao.getMessagesForPartner(partner.id).collect { msgs ->
                if (msgs.isEmpty()) {
                    val welcomeMsg = ChatMessage(
                        senderName = partner.name,
                        text = "Hey there! Welcome to the SilentX Voice App. Join my Voice cabin or send a chat! 🎙️✨",
                        timeString = "12:00 PM",
                        isFromMe = false,
                        partnerId = partner.id
                    )
                    chatDao.insertMessage(welcomeMsg)
                }
            }
        }
    }

    fun loginWithSocial(type: String, nickname: String, avatarIdx: Int) {
        _loginType.value = type
        if (nickname.trim().isNotEmpty()) {
            _userName.value = nickname
        }
        _userAvatarIndex.value = avatarIdx
        _isLoggedIn.value = true
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun selectCoinPackage(pkg: CoinPackage) {
        _selectedPackage.value = pkg
    }

    fun purchaseCoins(p: CoinPackage) {
        _userCoins.value += p.coins
    }

    fun updateOwnerUpiId(newUpiAddress: String) {
        _ownerUpiId.value = newUpiAddress
    }

    fun selectVoiceRoom(room: VoiceRoom) {
        _activeVoiceRoom.value = room
    }

    fun selectChatPartner(partner: ChatPartner) {
        _selectedPartner.value = partner
    }

    fun sendChatMessage(text: String) {
        if (text.trim().isEmpty()) return
        val currentPartner = _selectedPartner.value
        val me = _userName.value

        viewModelScope.launch {
            // 1. Save user's message to Room
            val myMessage = ChatMessage(
                senderName = me,
                text = text,
                timeString = "Now",
                isFromMe = true,
                partnerId = currentPartner.id
            )
            chatDao.insertMessage(myMessage)

            // 2. Trigger smart simulated response after slight delay
            delay(1000)
            val replyText = getSmartSimulatedReply(currentPartner.id, text)
            val partnerMessage = ChatMessage(
                senderName = currentPartner.name,
                text = replyText,
                timeString = "Now",
                isFromMe = false,
                partnerId = currentPartner.id
            )
            chatDao.insertMessage(partnerMessage)
        }
    }

    fun spendCoinsForGifting(amount: Int): Boolean {
        return if (_userCoins.value >= amount) {
            _userCoins.value -= amount
            true
        } else {
            false
        }
    }

    fun topupCoinsDirect(amount: Int) {
        _userCoins.value += amount
    }

    private fun getSmartSimulatedReply(partnerId: String, userQuery: String): String {
        val normalized = userQuery.lowercase()
        return when (partnerId) {
            "aanya" -> when {
                normalized.contains("hello") || normalized.contains("hi") -> "Hey lovely! Welcome to my music studio, join my voice slot. 🎙️"
                normalized.contains("song") || normalized.contains("sing") -> "Sure! I'll perform a live acoustic lofi covers for you tonight. Stay tuned! 🌸"
                normalized.contains("gift") || normalized.contains("ring") -> "Aww, thank you for the wonderful gifting! You are so sweet! 💖"
                else -> "SilentX Voice Rooms are so active today! Let's talk soon on the host mic! 🎧✨"
            }
            "faiz" -> when {
                normalized.contains("ludo") || normalized.contains("play") -> "Ludo room is open! Come join slot 3, let's roll the dice! 🎲"
                normalized.contains("hi") || normalized.contains("hello") -> "Arre bhai! Welcome to SilentX, tell me how's your active level going?"
                else -> "Sahi h! Don't forget to check out our gold couple ring packages on profile wallet page! 💍"
            }
            "sania" -> when {
                normalized.contains("live") || normalized.contains("vocal") -> "Starting my unplugged set in 10 minutes, set up your audio boosters! 🎹"
                else -> "Aww glad to connect here! Grab a mic seat so we can discuss and chat together. 🌈"
            }
            "admin" -> when {
                normalized.contains("upi") || normalized.contains("payout") -> "You can adjust the recipient GPay UPI id inside our Wallet Top Up screen securely!"
                else -> "SilentX Guard: Secure, dynamic, real-time audio rooms. Report any violations inside chat partner panel."
            }
            else -> "Thank you! Join the live voice cabin and let's interact! 🌟"
        }
    }

    fun toggleTaskCompletion(id: String, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateTaskStatus(id, isCompleted)
        }
    }

    fun resetRoadmap() {
        viewModelScope.launch {
            repository.resetAllTasks()
        }
    }
}
