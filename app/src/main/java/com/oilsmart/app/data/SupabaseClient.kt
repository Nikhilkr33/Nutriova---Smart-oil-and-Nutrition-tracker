package com.oilsmart.app.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    // TODO: Replace with your actual Supabase Project URL and Anon Key
    private const val SUPABASE_URL = "https://ssabgjfpcoevxwnbbodk.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNzYWJnamZwY29ldnh3bmJib2RrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzc4MDUzMTgsImV4cCI6MjA5MzM4MTMxOH0.RLqEgERLxIop8agv-T5KLpYo42twCtY0hIyjzxZl9dw"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
