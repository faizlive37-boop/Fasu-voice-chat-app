package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.CoinPackage
import com.example.ui.viewmodel.FasuViewModel
import com.example.ui.viewmodel.VoiceRoom
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VoiceRoomScreen(
    viewModel: FasuViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val activeRoom by viewModel.activeVoiceRoom.collectAsState()
    val userCoins by viewModel.userCoins.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userAvatarIdx by viewModel.userAvatarIndex.collectAsState()

    val avatars = listOf("🎙️", "🎮", "👑", "🎧", "🎸", "🎤", "🧩", "🎲")
    val userEmoji = avatars.getOrElse(userAvatarIdx) { "🎙️" }

    // Live cabin states
    var mySeatIndex by remember { mutableStateOf<Int?>(null) } // null means user in audience
    var isMuted by remember { mutableStateOf(false) }
    var soundTextEffect by remember { mutableStateOf("") }
    var activeGiftStormText by remember { mutableStateOf("") }
    
    // Voice level pulsing animations
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_audio")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_anim"
    )

    // Chat states
    var textMessage by remember { mutableStateOf("") }
    val roomMessages = remember {
        mutableStateListOf(
            "System" to "🛡️ SilentX Guard: Audio streams are secured under high fidelity filters.",
            "Aanya ✨" to "Hey silent lovers! Welcome to my music studio, grab a mic slot! 🎙️🌸",
            "RockerFaiz" to "Yo yo! Live vibe is absolutely fire! 🔥🎮"
        )
    }

    val listState = rememberLazyListState()

    LaunchedEffect(roomMessages.size) {
        if (roomMessages.size > 0) {
            listState.animateScrollToItem(roomMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(CabinCard, GalacticBg)
                )
            )
    ) {
        // Upper cabin status banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .border(1.dp, BorderNeon.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = CabinCard.copy(alpha = 0.8f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GlowPurple)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Active Room Mic",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = activeRoom.title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Host: ${activeRoom.hostName}",
                                    color = SoftText,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(VoiceGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = activeRoom.status,
                                        color = VoiceGreen,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Listeners count bubble
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Listeners",
                            tint = VoiceGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${activeRoom.listeners}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tags flow
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    activeRoom.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(BorderNeon.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = "#$tag", color = GlowPurple, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldVIP.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(text = "⭐ High Quality HQ", color = GoldVIP, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Live Gifting Notification Banner
        AnimatedVisibility(
            visible = activeGiftStormText.isNotEmpty(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(HotPink, GlowPurple)
                        )
                    )
                    .border(1.dp, GoldVIP, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🎁 " + activeGiftStormText,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Animated Sound effect banner
        AnimatedVisibility(
            visible = soundTextEffect.isNotEmpty(),
            enter = scaleIn(animationSpec = spring()) + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = soundTextEffect,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldVIP,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Interactive 8-Seat Vocal Grid
        Text(
            text = "Voice Seats (Tap any slot to Join)",
            fontSize = 11.sp,
            color = SoftText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
        )

        // Grid spacing
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(CabinCard.copy(alpha = 0.6f))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // First Row (Seats 1-4)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in 1..4) {
                        VoiceSeatItem(
                            seatNum = i,
                            isHost = i == 1,
                            userLabel = if (i == 1) activeRoom.hostName else if (mySeatIndex == i) userName else null,
                            avatarEmoji = if (i == 1) "🎙️" else if (mySeatIndex == i) userEmoji else null,
                            isPulsing = i == 1 || (mySeatIndex == i && !isMuted),
                            pulseScale = pulseScale,
                            onClick = {
                                if (i == 1) {
                                    Toast.makeText(context, "Seat 1 belongs to Host! Grab other empty seats.", Toast.LENGTH_SHORT).show()
                                } else {
                                    if (mySeatIndex == i) {
                                        // Leave seat
                                        mySeatIndex = null
                                        roomMessages.add("System" to "$userName left vocal seat $i.")
                                    } else {
                                        // Switch or sit on slot
                                        mySeatIndex = i
                                        roomMessages.add("System" to "$userName hopped on vocal seat $i!")
                                    }
                                }
                            }
                        )
                    }
                }

                // Second Row (Seats 5-8)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in 5..8) {
                        VoiceSeatItem(
                            seatNum = i,
                            isHost = false,
                            userLabel = if (mySeatIndex == i) userName else null,
                            avatarEmoji = if (mySeatIndex == i) userEmoji else null,
                            isPulsing = mySeatIndex == i && !isMuted,
                            pulseScale = pulseScale,
                            onClick = {
                                if (mySeatIndex == i) {
                                    mySeatIndex = null
                                    roomMessages.add("System" to "$userName left vocal seat $i.")
                                } else {
                                    mySeatIndex = i
                                    roomMessages.add("System" to "$userName hopped on vocal seat $i!")
                                }
                            }
                        )
                    }
                }
            }
        }

        // soundboard trigger bar & Micro controllers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Mic Controllers
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (mySeatIndex == null) {
                            Toast.makeText(context, "Join a voice seat first to toggle microphone!", Toast.LENGTH_SHORT).show()
                        } else {
                            isMuted = !isMuted
                            val stateString = if (isMuted) "muted 🔇" else "unmuted 🎙️"
                            Toast.makeText(context, "Microphone is now $stateString", Toast.LENGTH_SHORT).show()
                            roomMessages.add("System" to "$userName is $stateString.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMuted) HotPink.copy(alpha = 0.8f) else VoiceGreen.copy(alpha = 0.8f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("action_mute_mic")
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute Unmute Mic",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (isMuted) "Muted" else "Talking", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                // Sound Effect Player triggers
                IconButton(
                    onClick = {
                        soundTextEffect = "👏 CLAP! CLAP! CLAP! 👏"
                        roomMessages.add(userName to "triggered soundboard effect: Applause! 👏")
                        coroutineScope.launch {
                            delay(1800)
                            if (soundTextEffect.startsWith("👏")) soundTextEffect = ""
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GlowPurple.copy(alpha = 0.3f))
                ) {
                    Text("👏", fontSize = 16.sp)
                }

                IconButton(
                    onClick = {
                        soundTextEffect = "😂 ROFL! HAHAHA! 😂"
                        roomMessages.add(userName to "shared laughter effect! 😂")
                        coroutineScope.launch {
                            delay(1800)
                            if (soundTextEffect.startsWith("😂")) soundTextEffect = ""
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GlowPurple.copy(alpha = 0.3f))
                ) {
                    Text("😂", fontSize = 16.sp)
                }

                IconButton(
                    onClick = {
                        soundTextEffect = "🎉 BOOM! FIREWORKS! 🎉"
                        roomMessages.add(userName to "launched Cabin Celebrations! 🎆")
                        coroutineScope.launch {
                            delay(1800)
                            if (soundTextEffect.startsWith("🎉")) soundTextEffect = ""
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GlowPurple.copy(alpha = 0.3f))
                ) {
                    Text("🎆", fontSize = 16.sp)
                }
            }

            // Coin counter representation
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(0.5.dp, GoldVIP.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🪙", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$userCoins",
                        color = GoldVIP,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Fast virtual gifts hot drawer
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp),
            colors = CardDefaults.cardColors(containerColor = CabinCard.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Quick Gifts (Send to Host):",
                    fontSize = 9.sp,
                    color = SoftText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val gifts = listOf(
                        Triple("💍", "Bronze Ring", 10),
                        Triple("💎", "Silver Frame", 100),
                        Triple("👑", "VIP Tag", 500),
                        Triple("🌌", "Galaxy Gift", 2000)
                    )
                    gifts.forEach { (emoji, label,cost) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .border(0.5.dp, BorderNeon.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .clickable {
                                    if (viewModel.spendCoinsForGifting(cost)) {
                                        val storm = "$userName gifted $emoji $label to ${activeRoom.hostName}!"
                                        activeGiftStormText = storm
                                        roomMessages.add(userName to "sent $emoji $label, setting up cozy vibes in the cabin! ✨")
                                        coroutineScope.launch {
                                            delay(3000)
                                            if (activeGiftStormText == storm) activeGiftStormText = ""
                                        }
                                        Toast.makeText(context, "$label sent successfully!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Insufficient Coins (Costs $cost 🪙). Top Up in Profile Wallet!", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(emoji, fontSize = 15.sp)
                                Text(label, fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🪙", fontSize = 7.sp)
                                    Text("$cost", fontSize = 8.sp, color = GoldVIP, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Live Chat Messages Column
        Text(
            text = "Cabin Live Chat",
            fontSize = 11.sp,
            color = SoftText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(roomMessages) { (sender, msg) ->
                val isSys = sender == "System"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    if (isSys) {
                        Text(
                            text = "[!] ",
                            color = VoiceGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = msg,
                            color = VoiceGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = "$sender: ",
                            color = if (sender == userName) GlowPurple else GoldVIP,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = msg,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Message input textfield
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .windowInsetsPadding(WindowInsets.ime),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textMessage,
                onValueChange = { textMessage = it },
                placeholder = { Text("Write message to live cabin...", color = SoftText.copy(alpha = 0.7f), fontSize = 13.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = GlowPurple,
                    unfocusedBorderColor = BorderNeon.copy(alpha = 0.5f),
                    focusedContainerColor = CabinCard,
                    unfocusedContainerColor = CabinCard
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (textMessage.trim().isNotEmpty()) {
                        roomMessages.add(userName to textMessage)
                        textMessage = ""
                        focusManager.clearFocus()
                    }
                }),
                modifier = Modifier
                    .weight(1f)
                    .testTag("cabin_message_input")
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = {
                    if (textMessage.trim().isNotEmpty()) {
                        roomMessages.add(userName to textMessage)
                        textMessage = ""
                        focusManager.clearFocus()
                    }
                },
                containerColor = GlowPurple,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(46.dp)
                    .testTag("cabin_message_send_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun VoiceSeatItem(
    seatNum: Int,
    isHost: Boolean,
    userLabel: String?,
    avatarEmoji: String?,
    isPulsing: Boolean,
    pulseScale: Float,
    onClick: () -> Unit
) {
    val scale = if (isPulsing) pulseScale else 1.0f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(6.dp)
        ) {
            // Ripple waves for active speakers
            if (isPulsing) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(
                            if (isHost) VoiceGreen.copy(alpha = 0.15f) else GlowPurple.copy(alpha = 0.15f)
                        )
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (userLabel != null) {
                            if (isHost) VoiceGreen.copy(alpha = 0.3f) else CabinCard
                        } else {
                            Color.Black.copy(alpha = 0.3f)
                        }
                    )
                    .border(
                        1.dp,
                        if (isHost) GoldVIP else if (userLabel != null) GlowPurple else BorderNeon.copy(alpha = 0.3f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (userLabel != null) {
                    Text(
                        text = avatarEmoji ?: "🎙️",
                        fontSize = 18.sp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Empty",
                        tint = BorderNeon.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Little indicator badge (Host vs mic slot)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(if (isHost) GoldVIP else GlowPurple)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = if (isHost) "H" else "$seatNum",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = userLabel ?: "Seat $seatNum",
            color = if (userLabel != null) Color.White else SoftText.copy(alpha = 0.6f),
            fontSize = 9.sp,
            fontWeight = if (userLabel != null) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
