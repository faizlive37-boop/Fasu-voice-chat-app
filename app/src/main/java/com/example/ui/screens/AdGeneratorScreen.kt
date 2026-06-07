package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class AdScriptTemplate(
    val category: String,
    val headline: String,
    val format: String,
    val description: String,
    val scriptText: String
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AdGeneratorScreen(
    currentCategory: String,
    onCategoryChange: (String) -> Unit,
    tagline: String,
    onTaglineChange: (String) -> Unit,
    appName: String = "FASU VOICE CHAT APP"
) {
    val context = LocalContext.current
    val textMeasurer = rememberTextMeasurer()

    // Setup viral script templates
    val templates = remember(tagline, appName) {
        listOf(
            AdScriptTemplate(
                category = "CP Gifting",
                headline = "Couples & Magic Rings Craze 💍",
                format = "15-30s Reels / TikTok Short Video Dialog",
                description = "Perfect target strategy to prompt 'Virtual Weddings' and Ring Gifting triggers.",
                scriptText = """
🚀 [SCENE 1: Splitscreen. In-game dynamic avatars of a boy and girl showing cute romantic emojis]
VOICEOVER: "Tired of playing lonely ludo? Meet your virtual partner on $appName!"

🎬 [SCENE 2: The screen shifts to a highly stylized Voice Chat cabin filled with gifting animations of glowing pink rings flying around]
BOY HOST: "Wanna take our gaming buddy status to the next level?"
GIRL HOST: "Only if you send me the Special Golden CP Ring!"
BOY HOST: *launches top-up & sends Ring* (gorgeous wedding music triggers!)

📣 [SCENE 3: App logo appears with QR scan code and quick install buttons]
VOICEOVER: "Join virtual weddings, build your special couple album, and enjoy talent nights! Download $appName today!"
TAGLINE: "$tagline"
                """.trimIndent()
            ),
            AdScriptTemplate(
                category = "Ludo Duel",
                headline = "Ludo Gameplay with Live Voice Cabin 🎲",
                format = "20-40s YouTube Short Funny Dialogue",
                description = "Focuses on casual gaming combined with high-energy verbal arguments / friendly chat.",
                scriptText = """
😄 [SCENE 1: Close-up of intense Ludo board. Player A's token is about to win]
PLAYER A: "Yess! Just one 6 required to win this tournament and claim 1,000 Coins!"
PLAYER B (Directly on Voice Mic, laughing): "Not so fast brother. God of Ludo is with me today!"

🔥 [SCENE 2: Player B rolls the dice, lands a 6 and cuts Player A's token! Dynamic audio of coin crash and funny screaming sound effects]
PLAYER A: "Arey yaar! How is this luck even possible?!"
PLAYER B: "Haha! Come back to Room 102 next weekend and challenge again!"

📣 [SCENE 3: Screen fades into dynamic purple brand banner]
VOICEOVER: "Play high-stakes Ludo, join exciting weekends lobbies, and talk in premium quality real-time audio rooms. Download $appName now!"
TAGLINE: "$tagline"
                """.trimIndent()
            ),
            AdScriptTemplate(
                category = "Voice Chat",
                headline = "Vocal Talent Night & Singing Contests 🎤",
                format = "15-30s Organic Engagement Video Clip",
                description = "Emphasizes pure voice streaming, virtual gifting support, and premium sound cabin levels.",
                scriptText = """
🎵 [SCENE 1: Ambient micro-wave audio bar bouncing on-screen. A clean vocal is singing a popular song in direct high definition audio]
SINGER: *Singing classic tune on mic slot 1*
AUDIENCE MEMBERS: *Showcasing high volume dynamic coin gifts exploding into floating heart bubbles on-screen*

🎤 [SCENE 2: Host Cabin MC speaks up]
MC: "And with that golden song, faizlive is currently leading the Top Vocalist chart segment! Who will win the VIP golden avatar crown?"

📣 [SCENE 3: Grand neon poster layout transitions in]
VOICEOVER: "Showcase your voice, host target agencies, and receive direct cash payout gifting! Enter $appName now."
TAGLINE: "$tagline"
                """.trimIndent()
            ),
            AdScriptTemplate(
                category = "Agency Invite",
                headline = "Host & Agency Network Program 👑",
                format = "Digital Instagram Slider / Recruitment Post Copy",
                description = "A powerful pitch tool to onboard professional agencies and hosts.",
                scriptText = """
💰 [HEADLINE: Earn up to ₹50,000/Month directly inside your Voice Cabin on $appName!]

🌟 WHAT WE BRING:
- Direct 100% Top-Up QR payout splits!
- Beautiful VIP Profile frames, custom ring alerts, and dynamic entry animations.
- Automated Agency dashboard with secure host tracking.

👉 HOW TO SIGNUP:
1. Download $appName and enter Lounge profile section.
2. Direct-scan the Top-Up QR to secure your Host License setup.
3. Bring 10+ charming hosts and receive starting agency gifting commission bonus from Day 1!

TAGLINE: "$tagline"
                """.trimIndent()
            )
        )
    }

    val selectedTemplate = remember(templates, currentCategory) {
        templates.find { it.category == currentCategory } ?: templates[0]
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GalacticBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Tagline Editing Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CabinCard),
            border = BorderStroke(1.dp, BorderNeon),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Create, contentDescription = "Edit Tagline", tint = HotPink)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Customize App Meta & Tagline",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = tagline,
                    onValueChange = onTaglineChange,
                    label = { Text("App Tagline / Brand Hook Message", color = SoftText) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlowPurple,
                        unfocusedBorderColor = BorderNeon
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tagline_text_field")
                )
            }
        }

        // Horizontal Category Tab selection
        Text(
            text = "Select Campaign Targeting Format:",
            color = SoftText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("CP Gifting", "Ludo Duel", "Voice Chat", "Agency Invite").forEach { cat ->
                val isSelected = currentCategory == cat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) GlowPurple else CabinCard)
                        .border(1.dp, if (isSelected) Color.White else BorderNeon, RoundedCornerShape(12.dp))
                        .clickable { onCategoryChange(cat) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else SoftText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Real-Time Visual Poster Banner Drawing Canvas Mockup
        Text(
            text = "Live Marketing Poster Preview (Auto-Generated Banner):",
            color = SoftText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Draw poster banner dynamically via Jetpack Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(2.dp, GlowPurple, RoundedCornerShape(18.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Gradient background flow representing the party apps
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(CabinCard, Color(0xFF230F3E), Color(0xFF0F0022)),
                        start = Offset(0f, 0f),
                        end = Offset(w, h)
                    )
                )

                // Draw neon cosmic circles
                drawCircle(
                    color = HotPink.copy(alpha = 0.35f),
                    radius = h * 0.5f,
                    center = Offset(w * 0.85f, h * 0.3f)
                )
                drawCircle(
                    color = GlowPurple.copy(alpha = 0.25f),
                    radius = h * 0.6f,
                    center = Offset(w * 0.15f, h * 0.8f)
                )

                // Neon borders & framing lines
                drawRoundRect(
                    color = GlowPurple.copy(alpha = 0.5f),
                    topLeft = Offset(4f, 4f),
                    size = Size(w - 8f, h - 8f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f),
                    style = Stroke(width = 2f)
                )
                
                // Draw a retro grid representing dynamic social matching
                for (i in 1..8) {
                    val x = w * (i / 9f)
                    drawLine(
                        color = Color(0xFFBF5AF2).copy(alpha = 0.08f),
                        start = Offset(x, 0f),
                        end = Offset(x, h),
                        strokeWidth = 1f
                    )
                }

                // Add text details in canvas
                drawText(
                    textMeasurer = textMeasurer,
                    text = appName,
                    topLeft = Offset(w * 0.08f, h * 0.14f),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = "CAMPAIGN: ${selectedTemplate.headline}",
                    topLeft = Offset(w * 0.08f, h * 0.38f),
                    style = TextStyle(
                        color = HotPink,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = tagline,
                    topLeft = Offset(w * 0.08f, h * 0.54f),
                    style = TextStyle(
                        color = SoftText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Italic
                    )
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = "LUDO • AGENCY PARTY • CP RINGS",
                    topLeft = Offset(w * 0.08f, h * 0.74f),
                    style = TextStyle(
                        color = VoiceGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }

            // Small badge in top-right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color(0xFFFF2D55), RoundedCornerShape(8.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("HOT PROMO", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ad Copy Script Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CabinCard),
            border = BorderStroke(1.dp, BorderNeon),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Viral Video Script",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = selectedTemplate.format,
                            fontSize = 11.sp,
                            color = HotPink,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Copy Action
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Ad Script", selectedTemplate.scriptText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Script Copied! Play on TikTok/Instagram Reels 📢", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .background(GlowPurple.copy(alpha = 0.2f), CircleShape)
                            .border(1.dp, GlowPurple.copy(alpha = 0.4f), CircleShape)
                            .testTag("copy_script_button")
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                
                Text(
                    text = selectedTemplate.description,
                    fontSize = 11.sp,
                    color = SoftText,
                    lineHeight = 15.sp,
                    fontStyle = FontStyle.Italic
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Displays script body inside styled block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GalacticBg)
                        .border(1.dp, BorderNeon, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = selectedTemplate.scriptText,
                        fontSize = 11.sp,
                        color = Color.White,
                        lineHeight = 16.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Promo Tagline", tagline)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Tagline Copied!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BorderNeon)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Taglines / Meta Targets", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
