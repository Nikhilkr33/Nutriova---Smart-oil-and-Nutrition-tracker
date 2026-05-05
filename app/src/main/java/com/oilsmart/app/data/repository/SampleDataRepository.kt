package com.oilsmart.app.data.repository

import com.oilsmart.app.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Provides sample data for the OilSmart application.
 * In production, this would connect to Room DB and remote APIs.
 */
object SampleDataRepository {

    fun getUserProfile(): UserProfile = UserProfile(
        name = "Arjun Sharma",
        location = "Mumbai, Maharashtra",
        weight = 74,
        height = 178,
        age = 32,
        dailyOilLimitMl = 33,
        dietaryPreference = DietaryPreference.VEGETARIAN,
        totalPoints = 1250,
        currentStreak = 7,
        tier = RewardTier.GOLD,
        badgesEarned = 12
    )

    fun getRecipes(): List<Recipe> = listOf(
        Recipe(
            id = "r1",
            name = "Healthy Palak Paneer",
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800",
            oilAmountMl = 8,
            calories = 245,
            prepTimeMinutes = 25,
            difficulty = RecipeDifficulty.EASY,
            isVeg = true,
            isLowOil = true,
            cuisine = "North Indian",
            tags = listOf("Low Oil", "Veg", "High Protein"),
            ingredients = listOf(
                Ingredient("Fresh Spinach (Palak)", "500", "g"),
                Ingredient("Low-fat Paneer", "200", "g"),
                Ingredient("Cold Pressed Olive Oil", "1.5", "tsp"),
                Ingredient("Ginger-Garlic Paste", "1", "tbsp"),
                Ingredient("Indian Spices (Cumin, Turmeric)", "", "to taste")
            ),
            steps = listOf(
                CookingStep(1, "Blanch spinach leaves in boiling water for 2 minutes, then shock in cold water to retain the vibrant green color."),
                CookingStep(2, "Heat 1.5 tsp oil in a non-stick pan. Sauté cumin seeds and ginger-garlic paste until fragrant.", "Use a high-quality non-stick pan to reduce oil dependency by 60%."),
                CookingStep(3, "Add the spinach puree and cook for 5 minutes. Gently fold in the paneer cubes."),
                CookingStep(4, "Simmer on low heat. Avoid deep frying the paneer to keep the dish heart-healthy.", "Steam or soak paneer in warm water instead of frying for a soft texture.")
            ),
            rating = 4.8f,
            reviewCount = 342
        ),
        Recipe(
            id = "r2",
            name = "Moong Dal Chilla",
            imageUrl = "https://images.unsplash.com/photo-1567337710282-00832b415979?w=800",
            oilAmountMl = 5,
            calories = 180,
            prepTimeMinutes = 20,
            difficulty = RecipeDifficulty.EASY,
            isVeg = true,
            isLowOil = true,
            cuisine = "North Indian",
            tags = listOf("Low Oil", "Veg", "High Protein", "Breakfast"),
            ingredients = listOf(
                Ingredient("Moong Dal (soaked)", "1", "cup"),
                Ingredient("Green Chilli", "1", "piece"),
                Ingredient("Ginger", "1/2", "inch"),
                Ingredient("Cumin Seeds", "1", "tsp"),
                Ingredient("Salt", "", "to taste"),
                Ingredient("Oil", "1", "tsp")
            ),
            steps = listOf(
                CookingStep(1, "Soak moong dal for 4 hours, then grind to a smooth batter with minimal water."),
                CookingStep(2, "Add spices and mix well. Consistency should be like pancake batter."),
                CookingStep(3, "Heat a non-stick pan on medium. Add just 1/4 tsp oil and spread the batter.", "Using cast iron or ceramic pan requires zero oil."),
                CookingStep(4, "Cook 2 mins each side until golden. Serve with mint chutney.")
            ),
            rating = 4.6f,
            reviewCount = 218
        ),
        Recipe(
            id = "r3",
            name = "Oats Idli",
            imageUrl = "https://images.unsplash.com/photo-1645177628172-a94c1f96e6db?w=800",
            oilAmountMl = 3,
            calories = 120,
            prepTimeMinutes = 15,
            difficulty = RecipeDifficulty.EASY,
            isVeg = true,
            isLowOil = true,
            cuisine = "South Indian",
            tags = listOf("Low Oil", "Veg", "High Fiber", "Breakfast"),
            ingredients = listOf(
                Ingredient("Rolled Oats", "1", "cup"),
                Ingredient("Curd (low-fat)", "1/2", "cup"),
                Ingredient("Eno Fruit Salt", "1/2", "tsp"),
                Ingredient("Mustard Seeds", "1/4", "tsp"),
                Ingredient("Curry Leaves", "5-6", "leaves")
            ),
            steps = listOf(
                CookingStep(1, "Dry roast oats for 2 minutes, then grind to a coarse powder."),
                CookingStep(2, "Mix with curd and let rest for 5 minutes. Add eno and fold gently."),
                CookingStep(3, "Steam in idli moulds for 10-12 minutes.", "Use minimal oil to grease moulds — use a silicone brush."),
                CookingStep(4, "Serve hot with coconut chutney and sambar.")
            ),
            rating = 4.5f,
            reviewCount = 156
        ),
        Recipe(
            id = "r4",
            name = "Air-Fried Vegetable Pakora",
            imageUrl = "https://images.unsplash.com/photo-1606491956689-2ea866880c84?w=800",
            oilAmountMl = 7,
            calories = 180,
            prepTimeMinutes = 20,
            difficulty = RecipeDifficulty.EASY,
            isVeg = true,
            isLowOil = true,
            cuisine = "North Indian",
            tags = listOf("Low Oil", "Veg", "Snack", "Air-Fried"),
            ingredients = listOf(
                Ingredient("Mixed Vegetables", "2", "cups"),
                Ingredient("Chickpea Flour (Besan)", "1/2", "cup"),
                Ingredient("Spices", "", "to taste"),
                Ingredient("Oil (for air frying)", "1.5", "tsp")
            ),
            steps = listOf(
                CookingStep(1, "Cut vegetables into thin slices. Make a thick batter with besan and spices."),
                CookingStep(2, "Coat vegetables evenly in batter."),
                CookingStep(3, "Air fry at 200°C for 12-15 minutes, flipping halfway.", "Traditional deep frying uses 200ml oil; air frying uses just 7ml!"),
                CookingStep(4, "Serve with green chutney.")
            ),
            rating = 4.7f,
            reviewCount = 287
        ),
        Recipe(
            id = "r5",
            name = "Lemon Garlic Steamed Fish",
            imageUrl = "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=800",
            oilAmountMl = 4,
            calories = 210,
            prepTimeMinutes = 15,
            difficulty = RecipeDifficulty.EASY,
            isVeg = false,
            isLowOil = true,
            cuisine = "Coastal",
            tags = listOf("Low Oil", "Non-Veg", "High Protein", "Quick"),
            ingredients = listOf(
                Ingredient("Fish Fillet (Rohu/Pomfret)", "200", "g"),
                Ingredient("Lemon Juice", "2", "tbsp"),
                Ingredient("Garlic Cloves", "4", "cloves"),
                Ingredient("Olive Oil", "1", "tsp"),
                Ingredient("Fresh Herbs", "", "to taste")
            ),
            steps = listOf(
                CookingStep(1, "Marinate fish with lemon, garlic, and herbs for 10 minutes."),
                CookingStep(2, "Place on steaming tray, drizzle just 1 tsp oil on top."),
                CookingStep(3, "Steam for 10-12 minutes until fish flakes easily."),
                CookingStep(4, "Garnish with fresh coriander and serve with brown rice.")
            ),
            rating = 4.6f,
            reviewCount = 134
        ),
        Recipe(
            id = "r6",
            name = "Quinoa Vegetable Biryani",
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03246963f1a7?w=800",
            oilAmountMl = 10,
            calories = 290,
            prepTimeMinutes = 30,
            difficulty = RecipeDifficulty.MEDIUM,
            isVeg = true,
            isLowOil = false,
            cuisine = "Fusion",
            tags = listOf("Veg", "High Fiber", "Lunch", "Festive"),
            ingredients = listOf(
                Ingredient("Quinoa", "1", "cup"),
                Ingredient("Mixed Vegetables", "2", "cups"),
                Ingredient("Biryani Spices", "", "to taste"),
                Ingredient("Saffron", "a few", "strands"),
                Ingredient("Oil", "2", "tsp")
            ),
            steps = listOf(
                CookingStep(1, "Cook quinoa with saffron water for a beautiful aroma."),
                CookingStep(2, "Sauté vegetables with minimal oil and biryani spices."),
                CookingStep(3, "Layer quinoa and vegetables, dum cook for 15 minutes.", "Cover with wheat dough to seal in flavors without extra oil."),
                CookingStep(4, "Serve with raita and salad.")
            ),
            rating = 4.4f,
            reviewCount = 198
        ),
        Recipe(
            id = "r7",
            name = "Roasted Chickpea Salad",
            imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800",
            oilAmountMl = 5,
            calories = 150,
            prepTimeMinutes = 10,
            difficulty = RecipeDifficulty.EASY,
            isVeg = true,
            isLowOil = true,
            cuisine = "Mediterranean",
            tags = listOf("Low Oil", "Veg", "High Protein", "Quick", "Snack"),
            ingredients = listOf(
                Ingredient("Canned Chickpeas", "1", "can"),
                Ingredient("Mixed Greens", "2", "cups"),
                Ingredient("Lemon Juice", "2", "tbsp"),
                Ingredient("Olive Oil", "1", "tsp"),
                Ingredient("Paprika & Cumin", "", "to taste")
            ),
            steps = listOf(
                CookingStep(1, "Drain and dry chickpeas thoroughly on paper towels."),
                CookingStep(2, "Toss with just 1 tsp oil and spices, roast at 200°C for 20 mins."),
                CookingStep(3, "Mix greens with lemon dressing (no extra oil needed)."),
                CookingStep(4, "Top with roasted chickpeas and serve immediately.")
            ),
            rating = 4.3f,
            reviewCount = 89
        )
    )

    fun getAchievements(): List<Achievement> = listOf(
        Achievement(
            id = "a1",
            title = "Oil Minimalist",
            description = "Keep daily oil under 20ml for 3 days",
            points = 100,
            iconEmoji = "🌿",
            isUnlocked = true,
            progress = 1f
        ),
        Achievement(
            id = "a2",
            title = "Healthy Chef",
            description = "Bookmark 5 low-oil recipes from the app",
            points = 50,
            iconEmoji = "👨‍🍳",
            isUnlocked = true,
            progress = 1f
        ),
        Achievement(
            id = "a3",
            title = "Consistency King",
            description = "Maintain a 14-day tracking streak",
            points = 500,
            iconEmoji = "👑",
            isUnlocked = false,
            progress = 0.5f
        ),
        Achievement(
            id = "a4",
            title = "Heart Hero",
            description = "Complete a full month within oil limits",
            points = 1000,
            iconEmoji = "❤️",
            isUnlocked = false,
            progress = 0.23f
        ),
        Achievement(
            id = "a5",
            title = "Community Champion",
            description = "Share 3 healthy recipes with friends",
            points = 200,
            iconEmoji = "🤝",
            isUnlocked = false,
            progress = 0.33f
        ),
        Achievement(
            id = "a6",
            title = "Atmanirbhar Cook",
            description = "Use only domestic Indian oils for 7 days",
            points = 300,
            iconEmoji = "🇮🇳",
            isUnlocked = false,
            progress = 0.0f
        )
    )

    fun getWeeklyStreak(): WeeklyStreak = WeeklyStreak(
        days = listOf(
            StreakDay("Mon", 19, true, false),
            StreakDay("Tue", 20, true, false),
            StreakDay("Wed", 21, true, false),
            StreakDay("Thu", 22, true, false),
            StreakDay("Fri", 23, false, true),
            StreakDay("Sat", 24, false, false),
            StreakDay("Sun", 25, false, false)
        )
    )

    fun getCampaignTips(): List<CampaignTip> = listOf(
        CampaignTip(
            id = "t1",
            title = "Mann Ki Baat: PM's 10% Oil Reduction Call",
            description = "In the 119th episode of Mann Ki Baat (Feb 2025), PM Modi called for a 10% reduction in edible oil consumption as a strategic health and economic intervention for Atmanirbhar Bharat.",
            category = TipCategory.POLICY
        ),
        CampaignTip(
            id = "t2",
            title = "India's Oil Problem: The Numbers",
            description = "India's per capita edible oil consumption is 19.3 kg/year — 60% higher than the ICMR-recommended 12 kg. Over 56% of our 27.8 million tonnes consumption is imported, draining foreign exchange.",
            category = TipCategory.HEALTH
        ),
        CampaignTip(
            id = "t3",
            title = "Switch to Cold-Pressed Oils",
            description = "Cold-pressed mustard oil retains more natural antioxidants and requires less quantity for the same flavor. Try Indian varieties like cold-pressed sesame or groundnut oil.",
            category = TipCategory.COOKING
        ),
        CampaignTip(
            id = "t4",
            title = "Non-Stick Pan Tip",
            description = "A good non-stick or ceramic pan can reduce your oil usage by up to 60%. Preheat before adding oil and spread it using a silicone brush for even coverage.",
            category = TipCategory.COOKING
        ),
        CampaignTip(
            id = "t5",
            title = "South Indian Zero-Oil Cooking",
            description = "Traditional South Indian recipes like steamed idli, kootu, and poriyal use minimal oil. Explore these regional treasures for your daily cooking.",
            category = TipCategory.REGIONAL
        )
    )

    fun getOilLogs(): List<OilLog> = listOf(
        OilLog(id = 1, date = "2025-04-29", mealName = "Oats Idli", oilAmountMl = 3f, mealType = MealType.BREAKFAST, recipeId = "r3"),
        OilLog(id = 2, date = "2025-04-29", mealName = "Dal Tadka", oilAmountMl = 10f, mealType = MealType.LUNCH),
        OilLog(id = 3, date = "2025-04-29", mealName = "Palak Paneer", oilAmountMl = 8f, mealType = MealType.DINNER, recipeId = "r1"),
        OilLog(id = 4, date = "2025-04-28", mealName = "Moong Chilla", oilAmountMl = 5f, mealType = MealType.BREAKFAST, recipeId = "r2"),
        OilLog(id = 5, date = "2025-04-28", mealName = "Rajma Rice", oilAmountMl = 12f, mealType = MealType.LUNCH),
        OilLog(id = 6, date = "2025-04-28", mealName = "Stir Fry Veggies", oilAmountMl = 6f, mealType = MealType.DINNER)
    )

    fun getDailyOilSummaries(): List<DailyOilSummary> {
        val logs = getOilLogs()
        return logs.groupBy { it.date }.map { (date, dayLogs) ->
            DailyOilSummary(
                date = date,
                totalOilMl = dayLogs.sumOf { it.oilAmountMl.toDouble() }.toFloat(),
                limitMl = 33,
                logs = dayLogs
            )
        }
    }

    fun getHealthMetrics(): List<HealthMetric> = listOf(
        HealthMetric("Today's Oil", "21", "ml", 4f, ConsumptionStatus.GOOD),
        HealthMetric("Weekly Avg", "24.5", "ml/day", 2.5f, ConsumptionStatus.GOOD),
        HealthMetric("Monthly Saving", "270", "ml", 8f, ConsumptionStatus.EXCELLENT),
        HealthMetric("Target", "33", "ml/day", 0f, ConsumptionStatus.EXCELLENT)
    )

    fun getPolicyNudges(): List<PolicyNudge> = listOf(
        PolicyNudge("n1", "GST Benefit on Mustard Oil", "Domestic cold-pressed mustard oil has reduced GST, making it cost-effective for households.", NudgeType.GST_BENEFIT),
        PolicyNudge("n2", "Earn Points on Streaks", "Maintaining a 7-day streak earns you 50 bonus points redeemable for health consultations.", NudgeType.REWARD),
        PolicyNudge("n3", "OilSmart Certified Restaurants", "Look for the OilSmart Green Label when ordering from partnered restaurants.", NudgeType.CERTIFICATION)
    )
}
