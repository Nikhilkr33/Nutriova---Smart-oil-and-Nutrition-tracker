package com.oilsmart.app.ui.navigation

sealed class Screen(val route: String) {
    // Auth screens
    object SignIn : Screen("sign_in")
    object Onboarding : Screen("onboarding")

    // Main tabs
    object Home : Screen("home")
    object Recipes : Screen("recipes")
    object Tracker : Screen("tracker")
    object Rewards : Screen("rewards")
    object Profile : Screen("profile")

    // Detail screens
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object AddOilLog : Screen("add_oil_log")
    object Campaign : Screen("campaign")
    object PolicyDashboard : Screen("policy_dashboard")
    object Achievements : Screen("achievements")
    object ELearning : Screen("elearning")
    object Chatbot : Screen("chatbot")
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val iconUnselected: androidx.compose.ui.graphics.vector.ImageVector,
    val iconSelected: androidx.compose.ui.graphics.vector.ImageVector
)
