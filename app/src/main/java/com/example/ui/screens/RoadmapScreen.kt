package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
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
import com.example.data.local.TaskItem
import com.example.ui.theme.*

@Composable
fun RoadmapScreen(
    tasks: List<TaskItem>,
    onToggleTask: (id: String, isCompleted: Boolean) -> Unit,
    onResetAll: () -> Unit
) {
    // Calculate progress stats
    val totalTasks = tasks.size
    val finishedTasks = tasks.count { it.isCompleted }
    val progressFraction = if (totalTasks > 0) finishedTasks.toFloat() / totalTasks else 0.0f
    val progressPercentage = (progressFraction * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GalacticBg)
            .padding(16.dp)
    ) {
        // Upper stats banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CabinCard),
            border = BorderStroke(1.dp, BorderNeon),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Promotional Plan Progress",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "$finishedTasks of $totalTasks marketing gates achieved",
                            fontSize = 11.sp,
                            color = SoftText
                        )
                    }

                    // Reset action button
                    IconButton(
                        onClick = onResetAll,
                        modifier = Modifier
                            .background(BorderNeon.copy(alpha = 0.2f), CircleShape)
                            .border(0.5.dp, BorderNeon, CircleShape)
                            .testTag("reset_all_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Checklist",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Beautiful standard linear progress bar
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = HotPink,
                    trackColor = BorderNeon.copy(alpha = 0.25f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "READY FOR VIRAL TRAFFIC",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (progressPercentage == 100) VoiceGreen else SoftText.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "$progressPercentage% COMPLETE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = HotPink
                    )
                }
            }
        }

        // Checklist body
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Group our persisted target marketing milestones by category
            val groupedByPhase = tasks.groupBy { it.category }

            groupedByPhase.forEach { (phase, phaseTasks) ->
                item {
                    Text(
                        text = phase,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldVIP,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                    )
                }

                items(phaseTasks, key = { it.id }) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleTask(task.id, !task.isCompleted) }
                            .testTag("task_item_${task.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (task.isCompleted) CabinCard.copy(alpha = 0.45f) else CabinCard
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (task.isCompleted) BorderNeon.copy(alpha = 0.5f) else BorderNeon
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Checked icon status
                            Icon(
                                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = if (task.isCompleted) "Done" else "Pending",
                                tint = if (task.isCompleted) VoiceGreen else SoftText.copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (task.isCompleted) Color.White.copy(alpha = 0.6f) else Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = task.description,
                                    fontSize = 10.sp,
                                    color = if (task.isCompleted) SoftText.copy(alpha = 0.6f) else SoftText,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
