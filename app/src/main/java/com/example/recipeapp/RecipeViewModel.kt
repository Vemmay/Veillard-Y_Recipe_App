package com.example.recipeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.network.API_KEY
import com.example.recipeapp.network.AnalyzedInstruction
import com.example.recipeapp.network.ExtendedIngredient
import com.example.recipeapp.network.RetrofitInstance.retrofit
import com.example.recipeapp.network.SpoonacularApiService
import kotlinx.coroutines.launch

data class RecipeUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val selectedRecipe: Recipe? = null,
    val ingredients: List<ExtendedIngredient>? = null,
    val recipeInstruction: List<AnalyzedInstruction>? = null,
    val searchQuery: String = "",
    val selectedCuisine: String? = null,
    val selectedDiet: String? = null,
    val selectedMaxCalories: String? = null
)

data class Diet(val name: String, val isSelected: Boolean = false)
data class Cuisine(val name: String, val isSelected: Boolean = false)
data class MaxCalories(val name: String, val isSelected: Boolean = false)

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _uiState = MutableLiveData(RecipeUiState())
    val uiState: LiveData<RecipeUiState> = _uiState

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value?.copy(searchQuery = query)
    }

    fun updateCuisine(cuisine: String) {
        _uiState.value = _uiState.value?.copy(selectedCuisine = cuisine)
    }

    fun updateDiet(diet: String) {
        _uiState.value = _uiState.value?.copy(selectedDiet = diet)
    }

    fun updateMaxCalories(maxCalories: String) {
        _uiState.value = _uiState.value?.copy(selectedMaxCalories = maxCalories)
    }

    fun unselectRecipe() {
        _uiState.value = _uiState.value?.copy(selectedRecipe = null)
    }

    fun selectRecipe(recipe: Recipe) {
        _uiState.value = _uiState.value?.copy(selectedRecipe = recipe)
    }

    fun fetchRecipes(query: String, cuisine: String?, diet: String?, maxCalories: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, errorMessage = "")

            try {
                val response = recipeRepository.searchRecipes(API_KEY, query, cuisine, diet, maxCalories)
                _uiState.value = _uiState.value?.copy(recipes = response.recipes)
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(errorMessage = "Network error: ${e.message}")
                //log error
                e.printStackTrace()
            } finally {
                _uiState.value = _uiState.value?.copy(isLoading = false)
            }
        }
    }

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, errorMessage = "")
            try {
                val ingredientsResponse = recipeRepository.getRecipeDetails(recipeId, API_KEY).extendedIngredients
                val instructionsResponse = recipeRepository.getRecipeInstructions(recipeId, API_KEY)
                _uiState.value = _uiState.value?.copy(ingredients = ingredientsResponse, recipeInstruction = instructionsResponse)

            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(errorMessage = "Network error: ${e.message}")
                //log error
                e.printStackTrace()
            } finally {
                _uiState.value = _uiState.value?.copy(isLoading = false)
            }
        }
    }
}

// viewmodel factory
class ViewModelFactory(private val repo: RecipeRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// viewmodel repository
class RecipeRepository {
    private val apiService = retrofit.create(SpoonacularApiService::class.java)
    suspend fun searchRecipes(API_KEY: String, query: String, cuisine: String?, diet: String?, maxCalories: String?): ResponseTest {
        return apiService.searchRecipes(API_KEY, query, cuisine, diet, maxCalories)
    }

    suspend fun getRecipeDetails(id: Int, API_KEY: String): ShowRecipeByIdResponse {
        return apiService.getRecipeDetails(id, API_KEY)
    }

    suspend fun getRecipeInstructions(id: Int, API_KEY: String): List<AnalyzedInstruction> {
        return apiService.getRecipeInstructions(id, API_KEY)
    }
}