package com.oilsmart.app.ui.navigation
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.oilsmart.app.ui.screens.*
import com.oilsmart.app.ui.theme.*
import com.oilsmart.app.viewmodel.OilSmartViewModel

@Composable
fun OilSmartNavGraph() {
    val navController = rememberNavController()
    val viewModel: OilSmartViewModel = viewModel()

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home, "Home", Icons.Outlined.Home, Icons.Filled.Home),
        BottomNavItem(Screen.Recipes, "Recipes", Icons.Outlined.MenuBook, Icons.Filled.MenuBook),
        BottomNavItem(Screen.Tracker, "Track", Icons.Outlined.AddCircle, Icons.Filled.AddCircle),
        BottomNavItem(Screen.Rewards, "Rewards", Icons.Outlined.EmojiEvents, Icons.Filled.EmojiEvents),
        BottomNavItem(Screen.Profile, "Profile", Icons.Outlined.Person, Icons.Filled.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom bar is only shown on main tab screens (not on SignIn)
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Recipes.route,
        Screen.Tracker.route,
        Screen.Rewards.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.iconSelected else item.iconUnselected,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    item.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = GreenPrimary,
                                selectedTextColor = GreenPrimary,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = GreenSurface
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.SignIn.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn() + slideInHorizontally { it / 3 } },
            exitTransition = { fadeOut() + slideOutHorizontally { -it / 3 } },
            popEnterTransition = { fadeIn() + slideInHorizontally { -it / 3 } },
            popExitTransition = { fadeOut() + slideOutHorizontally { it / 3 } }
        ) {
            // ── Auth ──
            composable(Screen.SignIn.route) {
                SignInScreen(
                    viewModel = viewModel,
                    onSignInSuccess = { isSignUp ->
                        if (isSignUp) {
                            navController.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.SignIn.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.SignIn.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
            
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    viewModel = viewModel,
                    onOnboardingComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            // ── Main Tabs ──
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToRecipes = { navController.navigate(Screen.Recipes.route) },
                    onNavigateToTracker = { navController.navigate(Screen.Tracker.route) },
                    onNavigateToCampaign = { navController.navigate(Screen.Campaign.route) },
                    onNavigateToRecipeDetail = { id ->
                        navController.navigate(Screen.RecipeDetail.createRoute(id))
                    },
                    onNavigateToChatbot = { navController.navigate(Screen.Chatbot.route) }
                )
            }
            composable(Screen.Recipes.route) {
                RecipesScreen(
                    viewModel = viewModel,
                    onRecipeClick = { id ->
                        navController.navigate(Screen.RecipeDetail.createRoute(id))
                    }
                )
            }
            composable(Screen.Tracker.route) {
                TrackerScreen(viewModel = viewModel)
            }
            composable(Screen.Rewards.route) {
                RewardsScreen(viewModel = viewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onSignOut = {
                        // Clear entire back stack and go back to SignIn
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            // ── Detail Screens ──
            composable(Screen.RecipeDetail.route) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                RecipeDetailScreen(
                    viewModel = viewModel,
                    recipeId = recipeId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Campaign.route) {
                CampaignScreen(viewModel = viewModel)
            }
            composable(Screen.Chatbot.route) {
                ChatbotScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
