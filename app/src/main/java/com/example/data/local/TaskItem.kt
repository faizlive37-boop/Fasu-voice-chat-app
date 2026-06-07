package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val dayNumber: Int,
    val category: String,
    val isCompleted: Boolean = false
) {
    val phase: String
        get() = when {
            dayNumber <= 10 -> "Phase 1: Foundation Setup (Days 1-10)"
            dayNumber <= 20 -> "Phase 2: Launch & Viral Content (Days 11-20)"
            else -> "Phase 3: Community Scaling (Days 21-30)"
        }
}
