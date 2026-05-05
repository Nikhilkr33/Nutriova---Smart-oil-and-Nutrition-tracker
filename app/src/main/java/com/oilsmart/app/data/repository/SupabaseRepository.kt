package com.oilsmart.app.data.repository

import com.oilsmart.app.data.SupabaseClient
import com.oilsmart.app.data.models.OilLog
import com.oilsmart.app.data.models.Recipe
import com.oilsmart.app.data.models.UserProfile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

class SupabaseRepository {

    private val client = SupabaseClient.client

    // ============================
    // Authentication
    // ============================

    val authState = client.auth.sessionStatus

    suspend fun signInWithEmail(email: String, password: String): Result<Unit> {
        return try {
            client.auth.signInWith(io.github.jan.supabase.auth.providers.builtin.Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmail(email: String, password: String): Result<Unit> {
        return try {
            client.auth.signUpWith(io.github.jan.supabase.auth.providers.builtin.Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        try {
            client.auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ============================
    // Users
    // ============================
    
    suspend fun getUserProfile(): UserProfile? {
        val currentUser = client.auth.currentUserOrNull() ?: return null
        return try {
            client.postgrest["users"]
                .select { filter { eq("id", currentUser.id) } }
                .decodeSingleOrNull<UserProfile>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateUserProfile(profile: UserProfile): Boolean {
        return try {
            client.postgrest["users"]
                .upsert(profile)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ============================
    // Oil Logs
    // ============================
    
    suspend fun getOilLogs(): List<OilLog> {
        return try {
            client.postgrest["oil_logs"]
                .select()
                .decodeList<OilLog>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addOilLog(log: OilLog): Boolean {
        return try {
            // Need to set user_id for RLS, assuming log doesn't have it explicitly since it's missing in model
            // For now just insert as is, but we might need to handle user_id on backend or add to model
            client.postgrest["oil_logs"].insert(log)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ============================
    // Recipes
    // ============================
    
    suspend fun getRecipes(): List<Recipe> {
        return try {
            client.postgrest["recipes"]
                .select()
                .decodeList<Recipe>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
