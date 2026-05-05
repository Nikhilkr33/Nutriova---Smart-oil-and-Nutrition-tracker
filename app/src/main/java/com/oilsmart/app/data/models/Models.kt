package com.oilsmart.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

// ───────────────────────────────────────────
//  User Profile
// ───────────────────────────────────────────
@Serializable
data class UserProfile(
    val id: String = "user_1",
    val name: String = "Arjun Sharma",
    val location: String = "Mumbai, Maharashtra",
    val weight: Int = 74,
    val height: Int = 178,
    val age: Int = 32,
    @SerialName("daily_oil_limit_ml") val dailyOilLimitMl: Int = 33,
    @SerialName("dietary_preference") val dietaryPreference: DietaryPreference = DietaryPreference.VEGETARIAN,
    @SerialName("profile_image_url") val profileImageUrl: String = "",
    @SerialName("total_points") val totalPoints: Int = 1250,
    @SerialName("current_streak") val currentStreak: Int = 7,
    val tier: RewardTier = RewardTier.GOLD,
    @SerialName("badges_earned") val badgesEarned: Int = 12,
    @SerialName("has_diabetes") val hasDiabetes: Boolean = false,
    @SerialName("has_heart_problem") val hasHeartProblem: Boolean = false
)

@Serializable
enum class DietaryPreference(val label: String) {
    VEGETARIAN("Vegetarian"),
    NON_VEGETARIAN("Non-Vegetarian"),
    VEGAN("Vegan"),
    EGGETARIAN("Eggetarian")
}

@Serializable
enum class RewardTier(val label: String, val minPoints: Int) {
    BRONZE("Bronze Tier", 0),
    SILVER("Silver Tier", 500),
    GOLD("Gold Tier", 1000),
    PLATINUM("Platinum Tier", 5000)
}

// ───────────────────────────────────────────
//  Oil Consumption Tracking
// ───────────────────────────────────────────
@Serializable
@Entity(tableName = "oil_logs")
data class OilLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String = LocalDate.now().toString(), // ISO-8601
    @SerialName("meal_name") val mealName: String = "",
    @SerialName("oil_amount_ml") val oilAmountMl: Float = 0f,
    @SerialName("meal_type") val mealType: MealType = MealType.LUNCH,
    @SerialName("recipe_id") val recipeId: String? = null,
    val notes: String = ""
)

@Serializable
enum class MealType(val label: String, val emoji: String) {
    BREAKFAST("Breakfast", "🌅"),
    LUNCH("Lunch", "☀️"),
    DINNER("Dinner", "🌙"),
    SNACK("Snack", "🍎")
}

@Serializable
data class DailyOilSummary(
    val date: String,
    val totalOilMl: Float,
    val limitMl: Int,
    val logs: List<OilLog> = emptyList()
) {
    val percentageUsed: Float get() = (totalOilMl / limitMl * 100).coerceIn(0f, 100f)
    val status: ConsumptionStatus get() = when {
        percentageUsed <= 70f -> ConsumptionStatus.EXCELLENT
        percentageUsed <= 90f -> ConsumptionStatus.GOOD
        percentageUsed <= 100f -> ConsumptionStatus.WARNING
        else -> ConsumptionStatus.OVER_LIMIT
    }
}

@Serializable
enum class ConsumptionStatus(val label: String) {
    EXCELLENT("Excellent"),
    GOOD("On Track"),
    WARNING("Near Limit"),
    OVER_LIMIT("Over Limit")
}

// ───────────────────────────────────────────
//  Recipes
// ───────────────────────────────────────────
@Serializable
data class Recipe(
    val id: String,
    val name: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("oil_amount_ml") val oilAmountMl: Int,
    val calories: Int,
    @SerialName("prep_time_minutes") val prepTimeMinutes: Int,
    val difficulty: RecipeDifficulty,
    @SerialName("is_veg") val isVeg: Boolean,
    @SerialName("is_low_oil") val isLowOil: Boolean,
    val cuisine: String,
    val tags: List<String> = emptyList(),
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<CookingStep> = emptyList(),
    @SerialName("is_bookmarked") val isBookmarked: Boolean = false,
    val rating: Float = 4.5f,
    @SerialName("review_count") val reviewCount: Int = 0
)

@Serializable
data class Ingredient(
    val name: String,
    val quantity: String,
    val unit: String = ""
)

@Serializable
data class CookingStep(
    @SerialName("step_number") val stepNumber: Int,
    val description: String,
    val tip: String? = null
)

@Serializable
enum class RecipeDifficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}

@Serializable
enum class RecipeFilter(val label: String) {
    ALL("All"),
    VEG("Veg"),
    LOW_OIL("Low Oil (<10ml)"),
    NORTH_INDIAN("North Indian"),
    SOUTH_INDIAN("South Indian"),
    QUICK("Quick (<15 min)")
}

// ───────────────────────────────────────────
//  Rewards & Achievements
// ───────────────────────────────────────────
@Serializable
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val points: Int,
    @SerialName("icon_emoji") val iconEmoji: String,
    @SerialName("is_unlocked") val isUnlocked: Boolean,
    val progress: Float = 0f // 0.0 to 1.0
)

@Serializable
data class WeeklyStreak(
    val days: List<StreakDay>
) {
    val currentStreak: Int get() = days.count { it.isCompleted }
}

@Serializable
data class StreakDay(
    val dayLabel: String,
    val dayNumber: Int,
    val isCompleted: Boolean,
    val isToday: Boolean
)

// ───────────────────────────────────────────
//  Campaign & Awareness
// ───────────────────────────────────────────
@Serializable
data class CampaignTip(
    val id: String,
    val title: String,
    val description: String,
    val category: TipCategory,
    @SerialName("image_url") val imageUrl: String = "",
    @SerialName("is_read") val isRead: Boolean = false
)

@Serializable
enum class TipCategory(val label: String) {
    COOKING("Cooking Tips"),
    HEALTH("Health Facts"),
    NUTRITION("Nutrition"),
    POLICY("Policy Update"),
    REGIONAL("Regional Cuisine")
}

// ───────────────────────────────────────────
//  Health Dashboard
// ───────────────────────────────────────────
@Serializable
data class HealthMetric(
    val title: String,
    val value: String,
    val unit: String,
    val trend: Float, // positive = improvement
    val status: ConsumptionStatus
)

@Serializable
data class PolicyNudge(
    val id: String,
    val title: String,
    val description: String,
    val type: NudgeType,
    @SerialName("is_active") val isActive: Boolean = true
)

@Serializable
enum class NudgeType(val label: String) {
    GST_BENEFIT("GST Benefit"),
    REWARD("Reward"),
    CERTIFICATION("Certification"),
    RESTAURANT_LABEL("Restaurant Label")
}
