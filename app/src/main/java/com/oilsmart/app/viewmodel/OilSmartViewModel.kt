package com.oilsmart.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oilsmart.app.data.models.*
import com.oilsmart.app.data.repository.SampleDataRepository
import com.oilsmart.app.data.repository.SupabaseRepository
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import io.github.jan.supabase.auth.auth

class OilSmartViewModel : ViewModel() {

    val repository = SupabaseRepository()

    val authState = repository.authState

    private val _showLimitAlert = MutableStateFlow(false)
    val showLimitAlert: StateFlow<Boolean> = _showLimitAlert.asStateFlow()

    fun dismissLimitAlert() {
        _showLimitAlert.value = false
    }

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    // Remaining on SampleData for now since we didn't create Supabase tables for them yet
    private val _achievements = MutableStateFlow(SampleDataRepository.getAchievements())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _weeklyStreak = MutableStateFlow(SampleDataRepository.getWeeklyStreak())
    val weeklyStreak: StateFlow<WeeklyStreak> = _weeklyStreak.asStateFlow()

    private val _campaignTips = MutableStateFlow(SampleDataRepository.getCampaignTips())
    val campaignTips: StateFlow<List<CampaignTip>> = _campaignTips.asStateFlow()

    private val _healthMetrics = MutableStateFlow(SampleDataRepository.getHealthMetrics())
    val healthMetrics: StateFlow<List<HealthMetric>> = _healthMetrics.asStateFlow()

    private val _policyNudges = MutableStateFlow(SampleDataRepository.getPolicyNudges())
    val policyNudges: StateFlow<List<PolicyNudge>> = _policyNudges.asStateFlow()

    // Remote Data
    private val _oilLogs = MutableStateFlow<List<OilLog>>(emptyList())
    val oilLogs: StateFlow<List<OilLog>> = _oilLogs.asStateFlow()

    private val _dailySummaries = MutableStateFlow<List<DailyOilSummary>>(emptyList())
    val dailySummaries: StateFlow<List<DailyOilSummary>> = _dailySummaries.asStateFlow()

    private val _selectedFilter = MutableStateFlow(RecipeFilter.ALL)
    val selectedFilter: StateFlow<RecipeFilter> = _selectedFilter.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    init {
        viewModelScope.launch {
            authState.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> loadData()
                    else -> clearData()
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load Profile
            val profile = repository.getUserProfile()
            if (profile != null) {
                _userProfile.value = profile
            } else {
                // Create a default profile if none exists
                val newProfile = UserProfile()
                repository.updateUserProfile(newProfile)
                _userProfile.value = newProfile
            }

            // Load Recipes
            _recipes.value = repository.getRecipes()

            // Load Logs
            val logs = repository.getOilLogs()
            _oilLogs.value = logs
            updateDailySummaries(logs)
        }
    }

    private fun clearData() {
        _userProfile.value = UserProfile()
        _recipes.value = emptyList()
        _oilLogs.value = emptyList()
        _dailySummaries.value = emptyList()
    }

    private fun updateDailySummaries(logs: List<OilLog>) {
        _dailySummaries.value = logs.groupBy { it.date }.map { (date, dayLogs) ->
            DailyOilSummary(
                date = date,
                totalOilMl = dayLogs.sumOf { it.oilAmountMl.toDouble() }.toFloat(),
                limitMl = _userProfile.value.dailyOilLimitMl,
                logs = dayLogs
            )
        }
    }

    // Today's oil consumption
    val todayOilMl: Float get() {
        val today = LocalDate.now().toString()
        return _oilLogs.value.filter { it.date == today }.sumOf { it.oilAmountMl.toDouble() }.toFloat()
    }

    fun setFilter(filter: RecipeFilter) {
        _selectedFilter.value = filter
    }

    fun getFilteredRecipes(): List<Recipe> {
        return when (_selectedFilter.value) {
            RecipeFilter.ALL -> _recipes.value
            RecipeFilter.VEG -> _recipes.value.filter { it.isVeg }
            RecipeFilter.LOW_OIL -> _recipes.value.filter { it.oilAmountMl < 10 }
            RecipeFilter.NORTH_INDIAN -> _recipes.value.filter { it.cuisine == "North Indian" }
            RecipeFilter.SOUTH_INDIAN -> _recipes.value.filter { it.cuisine == "South Indian" }
            RecipeFilter.QUICK -> _recipes.value.filter { it.prepTimeMinutes <= 15 }
        }
    }

    fun getRecipeById(id: String): Recipe? = _recipes.value.find { it.id == id }

    fun toggleBookmark(recipeId: String) {
        _recipes.value = _recipes.value.map { recipe ->
            if (recipe.id == recipeId) recipe.copy(isBookmarked = !recipe.isBookmarked)
            else recipe
        }
        // In a real app, we would also save this to Supabase
    }

    fun addOilLog(log: OilLog) {
        viewModelScope.launch {
            val success = repository.addOilLog(log)
            if (success) {
                // Refresh logs
                val logs = repository.getOilLogs()
                _oilLogs.value = logs
                updateDailySummaries(logs)
                
                // Check limit exceeded
                if (todayOilMl > _userProfile.value.dailyOilLimitMl) {
                    _showLimitAlert.value = true
                }
            }
        }
    }

    fun getRecommendedRecipes(): List<Recipe> =
        _recipes.value.filter { it.isLowOil }.take(4)

    fun getPopularRecipes(): List<Recipe> =
        _recipes.value.sortedByDescending { it.reviewCount }.take(6)

    // ============================
    // Authentication
    // ============================
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return repository.signInWithEmail(email, password)
    }

    suspend fun signUp(email: String, password: String): Result<Unit> {
        return repository.signUpWithEmail(email, password)
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }

    // Call this once if your Supabase database is empty to populate the recipes
    fun seedDatabase() {
        viewModelScope.launch {
            val sampleRecipes = SampleDataRepository.getRecipes()
            sampleRecipes.forEach {
                try {
                    com.oilsmart.app.data.SupabaseClient.client.postgrest["recipes"].insert(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            // Reload after seeding
            _recipes.value = repository.getRecipes()
        }
    }

    // ============================
    // Onboarding
    // ============================
    fun calculateAndSaveProfile(
        name: String,
        age: Int,
        weight: Int,
        hasDiabetes: Boolean,
        hasHeartProblem: Boolean,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            // Calculate limit
            var limitMl = (weight * 0.4).toInt() // Base: 0.4ml per kg
            if (hasHeartProblem) limitMl -= 10
            if (hasDiabetes) limitMl -= 5
            
            // Ensure a minimum floor to avoid dangerously low/negative limits
            if (limitMl < 10) limitMl = 10

            // Current user from auth (Supabase uses auth.currentUser)
            val currentUserId = com.oilsmart.app.data.SupabaseClient.client.auth.currentUserOrNull()?.id ?: "temp_user"

            val updatedProfile = UserProfile(
                id = currentUserId,
                name = name,
                age = age,
                weight = weight,
                hasDiabetes = hasDiabetes,
                hasHeartProblem = hasHeartProblem,
                dailyOilLimitMl = limitMl,
                // keep defaults for others
            )

            // Save to DB
            repository.updateUserProfile(updatedProfile)
            
            // Update local state
            _userProfile.value = updatedProfile

            onComplete()
        }
    }

    // ============================
    // Profile Editing
    // ============================
    fun updateProfileDetails(name: String, location: String, age: Int, weight: Int, height: Int) {
        viewModelScope.launch {
            val currentProfile = _userProfile.value
            val updatedProfile = currentProfile.copy(
                name = name,
                location = location,
                age = age,
                weight = weight,
                height = height
            )

            // Save to DB
            repository.updateUserProfile(updatedProfile)

            // Update local state
            _userProfile.value = updatedProfile
        }
    }
}
