package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChatPartner
import com.example.ui.viewmodel.FasuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: FasuViewModel,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    val chatPartners = viewModel.chatPartners
    val selectedPartner by viewModel.selectedPartner.collectAsState()
    val messages by viewModel.activeChatMessagesFlow.collectAsState()

    var textInput by remember { mutableStateOf("") }

    // Scroll chat list to bottom reactively
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(GalacticBg)
    ) {
        // Left Column: Active Chat Partners List (Sidebar mini-list for easy switching)
        Column(
            modifier = Modifier
                .width(90.dp)
                .fillMaxHeight()
                .background(CabinCard)
                .border(0.5.dp, BorderNeon.copy(alpha = 0.2f))
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "FRIENDS",
                fontWeight = FontWeight.Black,
                color = SoftText,
                fontSize = 11.sp
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(chatPartners) { partner ->
                    val isSelected = selectedPartner.id == partner.id
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectChatPartner(partner)
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) GlowPurple else Color.Black.copy(alpha = 0.4f))
                                .border(
                                    2.dp,
                                    if (isSelected) GoldVIP else BorderNeon.copy(alpha = 0.3f),
                                    CircleShape
                                )
                        ) {
                            Text(partner.avatarEmoji, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = partner.name.split(" ")[0],
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else SoftText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }

        // Right Column: Direct Messaging Room Panel
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // Selected active partner header representation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CabinCard.copy(alpha = 0.8f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BorderNeon.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(selectedPartner.avatarEmoji, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedPartner.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GlowPurple.copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = selectedPartner.badge,
                                    color = GlowPurple,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = selectedPartner.subtitle,
                            color = SoftText,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Call invite symbol
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Invite to Room",
                    tint = VoiceGreen,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            // Instant join host's voice room
                            val matches = viewModel.voiceRooms.firstOrNull { it.hostName.startsWith(selectedPartner.name.split(" ")[0]) }
                            if (matches != null) {
                                viewModel.selectVoiceRoom(matches)
                            }
                        }
                )
            }

            // Messages feed list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Welcome helper info if list is just starting
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔐 Chat encrypted with Room Local Persistence",
                            fontSize = 10.sp,
                            color = SoftText.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(messages) { msg ->
                    val isMe = msg.isFromMe
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Column(
                            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
                            modifier = Modifier.widthIn(max = 240.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isMe) 16.dp else 4.dp,
                                            bottomEnd = if (isMe) 4.dp else 16.dp
                                        )
                                    )
                                    .background(
                                        if (isMe) GlowPurple else CabinCard
                                    )
                                    .border(
                                        0.5.dp,
                                        if (isMe) BorderNeon else BorderNeon.copy(alpha = 0.5f),
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isMe) 16.dp else 4.dp,
                                            bottomEnd = if (isMe) 4.dp else 16.dp
                                        )
                                    )
                                    .padding(horizontal = 12.dp, vertical = 9.dp)
                            ) {
                                Text(
                                    text = msg.text,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${msg.senderName} • ${msg.timeString}",
                                fontSize = 8.sp,
                                color = SoftText.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Direct messenger sending tools
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 12.dp)
                    .windowInsetsPadding(WindowInsets.ime),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Write chat to ${selectedPartner.name.split(" ")[0]}...", color = SoftText.copy(alpha = 0.7f), fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GlowPurple,
                        unfocusedBorderColor = BorderNeon.copy(alpha = 0.4f),
                        focusedContainerColor = CabinCard,
                        unfocusedContainerColor = CabinCard
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (textInput.trim().isNotEmpty()) {
                            viewModel.sendChatMessage(textInput)
                            textInput = ""
                            focusManager.clearFocus()
                        }
                    }),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_text_field")
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (textInput.trim().isNotEmpty()) {
                            viewModel.sendChatMessage(textInput)
                            textInput = ""
                            focusManager.clearFocus()
                        }
                    },
                    containerColor = GlowPurple,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Direct Msg",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
