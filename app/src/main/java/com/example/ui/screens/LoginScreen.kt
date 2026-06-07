package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (method: String, nickname: String, avatarIndex: Int) -> Unit
) {
    val context = LocalContext.current
    var nicknameState by remember { mutableStateOf("") }
    var selectedAvatarIdx by remember { mutableStateOf(2) } // default crown avatar

    val avatars = listOf(
        "🎙️", "🎮", "👑", "🎧", "🎸", "🎤", "🧩", "🎲"
    )
    val avatarLabels = listOf(
        "Voice", "Gamer", "Crown", "DJ", "Band", "Singer", "Host", "Dice"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF070014), Color(0xFF14072B))
                )
            )
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Large 3D Neon logo sticker representing live audio broadcasting & gaming
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(colors = listOf(GlowPurple, Color.Transparent)))
                .border(2.dp, HotPink, CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🎙️",
                fontSize = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Catchy title & marketing tagline matching user instructions
        Text(
            text = "SilentX Voice App",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Khele, Baatein Karein aur Rishte Banayein.",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = SoftText,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
        )

        // Nickname Section Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            colors = CardDefaults.cardColors(containerColor = CabinCard),
            border = BorderStroke(1.dp, BorderNeon),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "1. Enter Live Nickname:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                OutlinedTextField(
                    value = nicknameState,
                    onValueChange = { nicknameState = it },
                    placeholder = { Text("e.g. RockerFaiz", color = SoftText.copy(alpha = 0.5f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlowPurple,
                        unfocusedBorderColor = BorderNeon
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("nickname_input_field")
                )
            }
        }

        // Avatar selector Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = CabinCard),
            border = BorderStroke(1.dp, BorderNeon),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "2. Select Gamer Badge / Sticker:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    avatars.forEachIndexed { idx, emoji ->
                        val isSelected = selectedAvatarIdx == idx
                        val borderGlow = if (isSelected) HotPink else Color.Transparent
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedAvatarIdx = idx }
                                .background(if (isSelected) GlowPurple.copy(alpha = 0.3f) else Color.Transparent)
                                .border(1.5.dp, borderGlow, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(text = emoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = avatarLabels[idx], fontSize = 8.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else SoftText)
                        }
                    }
                }
            }
        }

        // Action Sign-in buttons
        Button(
            onClick = {
                val inputName = nicknameState.trim()
                if (inputName.isNotEmpty()) {
                    onLoginSuccess("Google", inputName, selectedAvatarIdx)
                    Toast.makeText(context, "Welcome back, $inputName! 🎙️", Toast.LENGTH_SHORT).show()
                } else {
                    onLoginSuccess("Google", "Spender_Faiz", selectedAvatarIdx)
                    Toast.makeText(context, "Logged in as Spender_Faiz!", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = GlowPurple),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("google_login_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Continue", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Enter live voice lobby",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Security, contentDescription = "Safe", tint = VoiceGreen, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Secure direct standard sandbox sandbox authorization",
                fontSize = 10.sp,
                color = SoftText.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
