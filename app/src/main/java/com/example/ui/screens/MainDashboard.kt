package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.FasuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboard(
    viewModel: FasuViewModel,
    onLogoutClick: () -> Unit
) {
    // Selected Screen State:
    // 0: Main homepage list view (the Flutter equivalent Home screen)
    // 1: Voice Room inside cabin
    // 2: Chat Room direct messenger
    // 3: Personal Profile wallet suite
    var activeViewIdx by remember { mutableStateOf(0) }

    val userName by viewModel.userName.collectAsState()
    val userAvatarIdx by viewModel.userAvatarIndex.collectAsState()
    val userCoins by viewModel.userCoins.collectAsState()

    val avatars = listOf("🎙️", "🎮", "👑", "🎧", "🎸", "🎤", "🧩", "🎲")
    val userEmoji = avatars.getOrElse(userAvatarIdx) { "🎙️" }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CabinCard,
                    titleContentColor = Color.White
                ),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (activeViewIdx != 0) {
                                IconButton(
                                    onClick = { activeViewIdx = 0 },
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "SilentX Voice App",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    modifier = Modifier.testTag("app_bar_title")
                                )
                                Text(
                                    text = when (activeViewIdx) {
                                        1 -> "Active Vocal Cabin Party"
                                        2 -> "Secure Direct Messenger"
                                        3 -> "Wallet Coins & Social ID"
                                        else -> "Silent Social Network Hub"
                                    },
                                    fontSize = 10.sp,
                                    color = SoftText
                                )
                            }
                        }

                        // Compact Wallet & Info Representer
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GalacticBg)
                                .border(0.5.dp, BorderNeon, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clickable { activeViewIdx = 3 }
                        ) {
                            Text(userEmoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "🪙 $userCoins",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldVIP
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onLogoutClick,
                        modifier = Modifier.testTag("logout_app_bar_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = HotPink
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Elegant M3 Navigation Bar for quick access switching
            NavigationBar(
                containerColor = CabinCard,
                tonalElevation = 8.dp,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                NavigationBarItem(
                    selected = activeViewIdx == 0,
                    onClick = { activeViewIdx = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = SoftText,
                        unselectedTextColor = SoftText,
                        indicatorColor = GlowPurple
                    ),
                    modifier = Modifier.testTag("bottom_nav_home")
                )

                NavigationBarItem(
                    selected = activeViewIdx == 1,
                    onClick = { activeViewIdx = 1 },
                    icon = { Icon(Icons.Default.Mic, contentDescription = "Voice Room") },
                    label = { Text("Voice Room", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = SoftText,
                        unselectedTextColor = SoftText,
                        indicatorColor = GlowPurple
                    ),
                    modifier = Modifier.testTag("bottom_nav_voice")
                )

                NavigationBarItem(
                    selected = activeViewIdx == 2,
                    onClick = { activeViewIdx = 2 },
                    icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                    label = { Text("Chat Messages", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = SoftText,
                        unselectedTextColor = SoftText,
                        indicatorColor = GlowPurple
                    ),
                    modifier = Modifier.testTag("bottom_nav_chat")
                )

                NavigationBarItem(
                    selected = activeViewIdx == 3,
                    onClick = { activeViewIdx = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = SoftText,
                        unselectedTextColor = SoftText,
                        indicatorColor = GlowPurple
                    ),
                    modifier = Modifier.testTag("bottom_nav_profile")
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(GalacticBg)
        ) {
            AnimatedContent(
                targetState = activeViewIdx,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "dashboard_screen_switcher"
            ) { currentIdx ->
                when (currentIdx) {
                    0 -> HomeScreenList(
                        userName = userName,
                        userEmoji = userEmoji,
                        userCoins = userCoins,
                        onSelectOption = { optionIndex ->
                            activeViewIdx = optionIndex
                        }
                    )
                    1 -> VoiceRoomScreen(viewModel = viewModel)
                    2 -> ChatScreen(viewModel = viewModel)
                    3 -> MyProfileScreen(viewModel = viewModel, onLogoutClick = onLogoutClick)
                }
            }
        }
    }
}

@Composable
fun HomeScreenList(
    userName: String,
    userEmoji: String,
    userCoins: Int,
    onSelectOption: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcoming Starry Header Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(GlowPurple, BorderNeon)
                        ),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CabinCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Namaste, $userName! $userEmoji",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Welcome to India's live social party app.",
                                color = SoftText,
                                fontSize = 11.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(VoiceGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "LIVE NOW", color = VoiceGreen, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Section header
        item {
            Text(
                text = "EXPLORE CHANNELS",
                color = SoftText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 6.dp, top = 2.dp)
            )
        }

        // ListTile 1: Voice Room
        item {
            SilentXTileItem(
                title = "Voice Room",
                subtitle = "Active Live Vocal Cabin rooms, mic seats, and soundboard effects.",
                icon = Icons.Default.Mic,
                iconColor = VoiceGreen,
                badge = "12 LIVE",
                onClick = { onSelectOption(1) },
                tag = "home_tile_voice"
            )
        }

        // ListTile 2: Chat
        item {
            SilentXTileItem(
                title = "Chat Messages",
                subtitle = "Encrypted direct inbox chats and simulated group companion channels.",
                icon = Icons.Default.Chat,
                iconColor = GlowPurple,
                badge = "3 NEW",
                onClick = { onSelectOption(2) },
                tag = "home_tile_chat"
            )
        }

        // ListTile 3: Profile
        item {
            SilentXTileItem(
                title = "Gamer Profile & Wallet",
                subtitle = "Track personal level badges, bios, and top-up Indian UPI packages.",
                icon = Icons.Default.Person,
                iconColor = GoldVIP,
                badge = "LEVEL 24",
                onClick = { onSelectOption(3) },
                tag = "home_tile_profile"
            )
        }
    }
}

@Composable
fun SilentXTileItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    badge: String,
    onClick: () -> Unit,
    tag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, BorderNeon.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .testTag(tag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CabinCard.copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f))
                    .border(1.dp, iconColor.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            color = iconColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    color = SoftText,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
