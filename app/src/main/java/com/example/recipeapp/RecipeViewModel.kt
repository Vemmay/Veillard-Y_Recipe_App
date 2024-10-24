package com.example.recipeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {
    fun searchRecipes(
        query: String?,
        cuisine: String?,
        diet: String?,
        maxCalories: Int?
        // create a cold asynchronous data stream
    ): Flow<List<Result>> {
        return repository.searchRecipes(query, cuisine, diet, maxCalories)
    }
}

class RecipeViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            // Instantiate the RecipeViewModel and pass the repository as an argument
            return RecipeViewModel(repository) as T
        }
        // Throw an exception if the ViewModel class is not recognized
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}