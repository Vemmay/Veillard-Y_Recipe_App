package com.example.recipeapp

import com.example.recipeapp.network.SpoonacularApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeRepository(private val apiService: SpoonacularApiService) {

    fun searchRecipes(
        query: String?,
        cuisine: String?,
        diet: String?,
        maxCalories: Int?
        // create a cold asynchronous data stream
    ): Flow<List<Result>> = flow {
        val response = apiService.searchRecipes(query, cuisine, diet, maxCalories)
        emit(response.results)
    }
}
