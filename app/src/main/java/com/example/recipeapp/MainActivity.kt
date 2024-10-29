package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.ui.theme.RecipeAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeAppTheme {
                val viewModel: RecipeViewModel by viewModels {
                    ViewModelFactory(RecipeRepository())
                }
                RecipeSearchScreen(viewModel)
            }
        }
    }
}


@Composable
fun RecipeSearchScreen(viewModel: RecipeViewModel) {
    val uiState by viewModel.uiState.observeAsState(RecipeUiState())
    if (uiState.selectedRecipe == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = uiState.searchQuery,
                onValueChange = { newQuery ->
                    viewModel.updateSearchQuery(newQuery)
                },
                label = { Text("Search Recipes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                items(cuisineOptions) { cuisine ->
                    FilterChip(
                        selected = cuisine.isSelected,
                        onClick = { viewModel.updateCuisine(cuisine.name) },
                        label = { Text(cuisine.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                items(dietOptions) { diet ->
                    FilterChip(
                        selected = diet.isSelected,
                        onClick = { viewModel.updateDiet(diet.name) },
                        label = { Text(diet.name) }
                    )
                }
            }
            Row {
                TextField(
                    value = uiState.selectedMaxCalories ?: "",
                    onValueChange = { newMaxCalories ->
                        viewModel.updateMaxCalories(newMaxCalories)
                    },
                    label = { Text("Max Calories") },
                    modifier = Modifier.weight(0.05f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.fetchRecipes(
                            query = uiState.searchQuery,
                            cuisine = uiState.selectedCuisine,
                            diet = uiState.selectedDiet,
                            maxCalories = uiState.selectedMaxCalories
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .align(Alignment.CenterVertically)
                ) {
                    Text("Search")
                }
            }


            // Display the list of recipes or loading/error states
            when {
                uiState.isLoading -> {
                    Text(text = "Loading...", modifier = Modifier.padding(16.dp))
                }

                uiState.errorMessage.isNotEmpty() -> {
                    Text(text = uiState.errorMessage, modifier = Modifier.padding(16.dp))
                }
            }

            LazyColumn {
                items(uiState.recipes) { recipe ->
                    RecipeItem(recipe = recipe) {
                        viewModel.selectRecipe(it)
                    }
                }
            }
        }

    } else {
        val selectedRecipe = uiState.selectedRecipe!!
        LaunchedEffect(key1 = uiState.selectedRecipe) {
            uiState.selectedRecipe?.let { recipe ->
                viewModel.getRecipeDetails(recipe.id)
            }
            uiState.selectedRecipe?.let { recipe ->
                viewModel.getRecipeInstructions(recipe.id)
            }
        }
        RecipeDetailScreen(
            recipe = selectedRecipe,
            viewModel = viewModel,
            onBack = { viewModel.unselectRecipe() }
        )
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onClick: (Recipe) -> Unit) {
    Column(modifier = Modifier
        .padding(16.dp)
        .clickable { onClick(recipe) }) {
        Text(text = recipe.title, style = MaterialTheme.typography.titleSmall)
        Image(
            painter = rememberAsyncImagePainter(recipe.image),
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        )
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
    }
}

@Composable
fun RecipeDetailScreen(recipe: Recipe, viewModel: RecipeViewModel, onBack: () -> Unit) {
    // Display the recipe details
    Column(modifier = Modifier.padding(16.dp)) {
        viewModel.getRecipeDetails(recipe.id)
        val recipeInstructions = viewModel.uiState.value?.recipeInstruction
        val ingredients = viewModel.uiState.value?.ingredients

        if (recipeInstructions == null) {
            Text(text = "Loading recipe details...")
            return@Column
        }

        Button(onClick = onBack) {
            Text("Back")
        }
        Text(text = recipe.title, style = MaterialTheme.typography.titleLarge)
        Image(
            painter = rememberAsyncImagePainter(recipe.image),
            contentDescription = "Recipe image for ${recipe.title}",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Row{
            LazyColumn {
                items(ingredients ?: emptyList()) { ingredient ->
                    Text(
                        text = ingredient.name,
                        modifier = Modifier.padding(8.dp), // Add padding for each ingredient item
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Instructions:", style = MaterialTheme.typography.titleMedium)
                recipeInstructions.forEachIndexed { index, instruction ->
                    Text(
                        text = "Step ${index + 1}: ${instruction.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}