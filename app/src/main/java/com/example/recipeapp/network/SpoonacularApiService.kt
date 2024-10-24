package com.example.recipeapp.network

import com.example.recipeapp.GetRecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "23085022dfd54a2fa179eb9a6622764e"
// Define Retrofit API interface
interface SpoonacularApiService {
    @GET("complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String?,
        @Query("cuisine") cuisine: String?,
        @Query("diet") diet: String?,
        @Query("maxCalories") maxCalories: Int?,
        @Query("apiKey") apiKey : String = API_KEY
    ): GetRecipeResponse
}
