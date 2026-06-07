package com.example.data.repository

import com.example.data.local.TaskDao
import com.example.data.local.TaskItem
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasksFlow: Flow<List<TaskItem>> = taskDao.getAllTasksFlow()

    suspend fun getTasksDirect(): List<TaskItem> = taskDao.getAllTasksDirect()

    suspend fun insertTask(task: TaskItem) {
        taskDao.insert(task)
    }

    suspend fun updateTaskStatus(id: String, isCompleted: Boolean) {
        taskDao.updateTaskStatus(id, isCompleted)
    }

    suspend fun resetAllTasks() {
        taskDao.resetAllCompleted()
    }

    suspend fun ensureTasksPrepopulated() {
        val existing = taskDao.getAllTasksDirect()
        if (existing.isEmpty()) {
            val defaultTasks = listOf(
                TaskItem(
                    id = "t1",
                    title = "Brand Identity Match",
                    description = "Finalize Brand Name (FASU VOICE CHAT APP) and setup Tagline 'Khele, Baatein Karein aur Rishte Banayein'. Organize fonts & colors.",
                    dayNumber = 1,
                    category = "Brand Identity"
                ),
                TaskItem(
                    id = "t2",
                    title = "Aesthetic Logo & Assets",
                    description = "Deploy the eye-catching, youth-focused brand logo with vibrant violet and interactive neon design themes.",
                    dayNumber = 2,
                    category = "Brand Identity"
                ),
                TaskItem(
                    id = "t3",
                    title = "Agency Network Setup",
                    description = "Contact pilot agencies (10-15 host coordinators) to recruit top charm and high-quality vocal hosts for room engagement.",
                    dayNumber = 4,
                    category = "Agency System"
                ),
                TaskItem(
                    id = "t4",
                    title = "Host Incentive Matrix",
                    description = "Prepare transparent payout calculator matrices based on diamond/coin gifting percentages to motivate agencies.",
                    dayNumber = 6,
                    category = "Agency System"
                ),
                TaskItem(
                    id = "t5",
                    title = "Rings & Gifting Catalog",
                    description = "Design virtual rings levels: Bronze, Silver, Gold and Limited Valentine Series. Finalize top-up coin valuations.",
                    dayNumber = 8,
                    category = "CP & Gifting"
                ),
                TaskItem(
                    id = "t6",
                    title = "Virtual Wedding Ceremonies",
                    description = "Document structural guidelines for virtual room weddings. Setup custom badges, entries, and interactive gift storm effects.",
                    dayNumber = 11,
                    category = "CP & Gifting"
                ),
                TaskItem(
                    id = "t7",
                    title = "Couple Leaderboard Badges",
                    description = "Design the unique 'Golden Frame' entry animation awards for the top weekly couples listed on user profile pages.",
                    dayNumber = 14,
                    category = "CP & Gifting"
                ),
                TaskItem(
                    id = "t8",
                    title = "Referral Loop Launch",
                    description = "Program a referral engine: Invite 5 active buddies to claim a bonus Bronze Ring or premium dynamic entrance free trial.",
                    dayNumber = 17,
                    category = "Referral & Rewards"
                ),
                TaskItem(
                    id = "t9",
                    title = "Daily Check-in Streaks",
                    description = "Structure standard check-in streaks rewarding users with small coin drops each successive day they launch the app.",
                    dayNumber = 19,
                    category = "Referral & Rewards"
                ),
                TaskItem(
                    id = "t10",
                    title = "Instagram & TikTok Launch",
                    description = "Develop short reels showing funny Ludo dialogue clips and heartwarming couple matching sessions to drive organic growth.",
                    dayNumber = 21,
                    category = "Social Media"
                ),
                TaskItem(
                    id = "t11",
                    title = "ASO Metadata Finalization",
                    description = "Optimize Play Store / App Store keywords: 'Voice Chat Room', 'Play Ludo with Friends', 'CP Ring', 'Online Party Chat'.",
                    dayNumber = 24,
                    category = "Store Optimization"
                ),
                TaskItem(
                    id = "t12",
                    title = "Google & Facebook Ad Setup",
                    description = "Setup targeted digital ad assets catering exactly to 18-30 interest groups: Casual gaming, voice socializing, dating.",
                    dayNumber = 26,
                    category = "Paid Advertising"
                ),
                TaskItem(
                    id = "t13",
                    title = "Weekend Ludo Tournaments",
                    description = "Establish weekend-based entry pools where participants play casual games and compete for major coin percentages.",
                    dayNumber = 28,
                    category = "Tournaments"
                ),
                TaskItem(
                    id = "t14",
                    title = "Vocal Talent Contest Nights",
                    description = "Host grand virtual singing nights where users join mic slots and win by acquiring user-voted virtual gifting items.",
                    dayNumber = 30,
                    category = "Tournaments"
                )
            )
            taskDao.insertAll(defaultTasks)
        }
    }
}
