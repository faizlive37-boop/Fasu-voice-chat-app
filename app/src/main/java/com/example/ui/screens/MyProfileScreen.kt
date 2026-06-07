package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.CoinPackage
import com.example.ui.viewmodel.FasuViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    viewModel: FasuViewModel,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val userName by viewModel.userName.collectAsState()
    val userAvatarIdx by viewModel.userAvatarIndex.collectAsState()
    val loginType by viewModel.loginType.collectAsState()
    val userCoins by viewModel.userCoins.collectAsState()
    val ownerUpiId by viewModel.ownerUpiId.collectAsState()
    val selectedPkg by viewModel.selectedPackage.collectAsState()

    val avatars = listOf("🎙️", "🎮", "👑", "🎧", "🎸", "🎤", "🧩", "🎲")
    val userEmoji = avatars.getOrElse(userAvatarIdx) { "🎙️" }

    var personalBio by remember { mutableStateOf("SilentX is love! Singing, playing casual games & bonding daily. 🌸🎧") }
    var isEditingBio by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GalacticBg)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // High Fidelity Gamer Social Card representing SilentX digital identity
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(GlowPurple, BorderNeon)),
                        RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CabinCard)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar circle with active neon circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.4f))
                            .border(3.dp, GlowPurple, CircleShape)
                    ) {
                        Text(userEmoji, fontSize = 42.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = userName,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GoldVIP.copy(alpha = 0.15f))
                                .border(0.5.dp, GoldVIP, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🏆 Level 24 VIP",
                                color = GoldVIP,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(VoiceGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Social Host",
                                color = VoiceGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bio display & Custom editor
                    if (isEditingBio) {
                        OutlinedTextField(
                            value = personalBio,
                            onValueChange = { personalBio = it },
                            singleLine = false,
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = GlowPurple,
                                unfocusedBorderColor = BorderNeon
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TextButton(
                            onClick = { isEditingBio = false }
                        ) {
                            Text("Save Bio Changes", color = GlowPurple, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Text(
                            text = personalBio,
                            color = SoftText,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isEditingBio = true }
                                .padding(horizontal = 8.dp)
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = BorderNeon.copy(alpha = 0.3f)
                    )

                    // Wallet Balance Node
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Current Coins", color = SoftText, fontSize = 11.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🪙", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$userCoins",
                                    color = GoldVIP,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Login Method", color = SoftText, fontSize = 11.sp)
                            Text(
                                text = loginType,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Coins Top-Up Suite (Paytm, GPay, PhonePe)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CabinCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💎 Coin Wallet Store (Customizable UPI)",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Customize the receiver UPI address below to manage where host deposits go!",
                        color = SoftText,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Configurable Recipient UPI
                    OutlinedTextField(
                        value = ownerUpiId,
                        onValueChange = { viewModel.updateOwnerUpiId(it) },
                        label = { Text("Enter Your Receiver GPay/PhonePe UPI ID", color = SoftText) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = VoiceGreen,
                            unfocusedBorderColor = BorderNeon
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp)
                            .testTag("owner_upi_id_input"),
                        leadingIcon = {
                            Icon(Icons.Default.QrCodeScanner, contentDescription = "UPI", tint = VoiceGreen)
                        }
                    )

                    // 4 Interactive Bundles
                    Text(
                        text = "Choose Coin Package to Buy:",
                        color = SoftText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        viewModel.coinPackages.forEach { pkg ->
                            val isSelected = selectedPkg.id == pkg.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) GlowPurple.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.03f))
                                    .border(
                                        1.dp,
                                        if (isSelected) GlowPurple else BorderNeon.copy(alpha = 0.2f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.selectCoinPackage(pkg) }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🪙", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "${pkg.coins} Coins Package",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Gift perk: ${pkg.bonusGift}",
                                            color = SoftText,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) GlowPurple else Color.Black.copy(alpha = 0.5f))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "₹ ${pkg.priceInInr}",
                                        color = if (isSelected) Color.White else VoiceGreen,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulated UPI QR Code or instant addition for testing
                    // Generating standard UPI Payment URI
                    val upiPaymentUri = "upi://pay?pa=$ownerUpiId&pn=SilentXApp&am=${selectedPkg.priceInInr}&cu=INR"

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.3f))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "QR Mock",
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Instant Top-Up QR generated at: $ownerUpiId",
                            color = SoftText,
                            fontSize = 9.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Amount: ₹ ${selectedPkg.priceInInr} for ${selectedPkg.coins} Coins",
                            color = GoldVIP,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(upiPaymentUri)
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("UPI URL", upiPaymentUri)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Link Copied! No local UPI apps installed in preview emulator.", Toast.LENGTH_LONG).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GlowPurple),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Pay Now (UPI)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Button(
                                onClick = {
                                    viewModel.purchaseCoins(selectedPkg)
                                    Toast.makeText(context, "Test Coin Purchase Approved! Added +${selectedPkg.coins} Coins 🪙", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = VoiceGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Simulate Credit", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Global security disclosure & exit Representatiton
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CabinCard.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Shield",
                        tint = GlowPurple,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("SilentX Guardian Secure Platform", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Encryption standards verified under host licensing keys.", color = SoftText, fontSize = 9.sp)
                    }
                }
            }
        }

        item {
            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(containerColor = HotPink.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("action_logout_profile_btn")
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout from Fasu profile", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
