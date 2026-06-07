package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AgencyCalculatorScreen(
    headcount: Int,
    onHeadcountChange: (Int) -> Unit,
    targetCoins: Int,
    onTargetCoinsChange: (Int) -> Unit,
    commissionRate: Double,
    onCommissionRateChange: (Double) -> Unit,
    platformCost: Double,
    onPlatformCostChange: (Double) -> Unit
) {
    // Formula calculations
    // Let's assume 1000 Coins = ₹10 INR in top-up value (₹0.01 per coin) for premium voice model
    val coinValueInrMultiplier = 0.01
    val grossRevenueInr = targetCoins * coinValueInrMultiplier
    
    // Hostesses get their fixed pay (e.g., 45% of total coin gifting they received)
    val hostEarningPercent = 45.0
    val totalHostsPayInr = grossRevenueInr * (hostEarningPercent / 100.0)
    
    // Agency head gets their commission (e.g., 15% of the gross)
    val totalAgencyPayInr = grossRevenueInr * (commissionRate / 100.0)
    
    // Platform server / Play Store costs (e.g., 10%)
    val platformCostInr = grossRevenueInr * (platformCost / 100.0)
    
    // Leftover net profit for FASU app owner (Faiz)! This directly addresses "mera profit ho"
    val netAppsProfitInr = grossRevenueInr - totalHostsPayInr - totalAgencyPayInr - platformCostInr

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GalacticBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Welcome Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CabinCard),
            border = BorderStroke(1.dp, BorderNeon),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Calculator",
                        tint = GoldVIP,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Agency & Host Commission Calculator",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "WePlay & Yalla Ludo scale via hostesses! Model your agency's monthly targets, gift flows, and calculate Net Owner Profits here.",
                    fontSize = 12.sp,
                    color = SoftText,
                    lineHeight = 16.sp
                )
            }
        }

        // Sliders & Custom controls
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CabinCard),
            border = BorderStroke(1.dp, BorderNeon),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Adjust Stream Variables",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Headcount Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Active Hosts Count:", fontSize = 12.sp, color = SoftText)
                    Text(text = "$headcount Hosts", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GlowPurple)
                }
                Slider(
                    value = headcount.toFloat(),
                    onValueChange = { onHeadcountChange(it.toInt()) },
                    valueRange = 5f..150f,
                    colors = SliderDefaults.colors(thumbColor = GlowPurple, activeTrackColor = GlowPurple, inactiveTrackColor = BorderNeon),
                    modifier = Modifier.testTag("headcount_slider")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Monthly Gifting Target Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Estimated Monthly Coins Gifting:", fontSize = 12.sp, color = SoftText)
                    Text(
                        text = "${targetCoins / 1000}k Coins",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = HotPink
                    )
                }
                Slider(
                    value = targetCoins.toFloat(),
                    onValueChange = { onTargetCoinsChange(it.toInt()) },
                    valueRange = 50000f..5000000f,
                    colors = SliderDefaults.colors(thumbColor = HotPink, activeTrackColor = HotPink, inactiveTrackColor = BorderNeon),
                    modifier = Modifier.testTag("coins_slider")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Commission Rate Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Agency Lead Promo Share:", fontSize = 12.sp, color = SoftText)
                    Text(text = "${String.format("%.1f", commissionRate)}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldVIP)
                }
                Slider(
                    value = commissionRate.toFloat(),
                    onValueChange = { onCommissionRateChange(it.toDouble()) },
                    valueRange = 5f..30f,
                    colors = SliderDefaults.colors(thumbColor = GoldVIP, activeTrackColor = GoldVIP, inactiveTrackColor = BorderNeon)
                )
            }
        }

        // Live Calculations Output Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CabinCard),
            border = BorderStroke(1.dp, BorderNeon),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = "Profit Stream", tint = VoiceGreen)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Estimated Monthly Cashflow Split",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Total Top-Up volume
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Gross User Recharge Volume:", fontSize = 13.sp, color = SoftText)
                    Text(
                        text = "₹${String.format("%,.0f", grossRevenueInr)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Divider(color = BorderNeon, modifier = Modifier.padding(vertical = 10.dp))

                // Host payouts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Hosts Live Wages ($hostEarningPercent%):", fontSize = 12.sp, color = SoftText)
                    Text(text = "₹${String.format("%,.0f", totalHostsPayInr)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Agency share
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Host Agency Bureau Margin ($commissionRate%):", fontSize = 12.sp, color = SoftText)
                    Text(text = "₹${String.format("%,.0f", totalAgencyPayInr)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldVIP)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Servers and platform
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Server Hosting / Google Store fee (10%):", fontSize = 12.sp, color = SoftText)
                    Text(text = "₹${String.format("%,.0f", platformCostInr)}", fontSize = 12.sp, color = SoftText)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Net Profit! Glowing Mint background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VoiceGreen.copy(alpha = 0.12f))
                        .border(1.dp, VoiceGreen, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Your Net App Direct Profit 💰",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = VoiceGreen
                            )
                            Text(
                                text = "Faiz's 100% Direct Profit Share",
                                fontSize = 10.sp,
                                color = SoftText
                            )
                        }

                        Text(
                            text = "₹${String.format("%,.0f", netAppsProfitInr)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = VoiceGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Warning / Info block about the direct transfer QR setup
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Notice",
                        tint = GoldVIP,
                        modifier = Modifier.size(16.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PRO-TIP: To maximize these direct profits, direct your online users to scan your payment QR card inside our Top-up screen instead of 3rd party gateways! This secures ₹0 fee dynamic deposits directly to your UPI.",
                        fontSize = 10.sp,
                        color = SoftText.copy(alpha = 0.8f),
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}
