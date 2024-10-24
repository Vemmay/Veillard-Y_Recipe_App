package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.recipeapp.ui.theme.RecipeAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import coil.compose.rememberImagePainter
import com.example.recipeapp.Result
import com.example.recipeapp.RecipeViewModel
import com.example.recipeapp.network.RetrofitInstance


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeAppTheme {
                val viewModel: ViewModel = ViewModel(factory = RecipeViewModelFactory(RecipeRepository(RetrofitInstance.api))).also {

                    // Show the search screen
                    RecipeSearchScreen(viewModel = it)
                }
            }
        }
    }
}

private fun createRecipeViewModel(): ViewModel {
    val repository = RecipeRepository(RetrofitInstance.api)
    val factory = RecipeViewModelFactory(repository)
    return ViewModel(factory = factory)
}

override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    setContent {
        RecipeAppTheme {
            val viewModel = createRecipeViewModel()
            RecipeSearchScreen(viewModel = viewModel)
        }
    }
}


@Composable
fun RecipeSearchScreen(viewModel: RecipeViewModel) {
    var query by remember { mutableStateOf("") }

    Column {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Recipes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                viewModel.searchRecipes(query = query, cuisine = null, diet = null, maxCalories = null)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Search")
        }

        RecipeListScreen(viewModel)
    }
}

@Composable
fun RecipeListScreen(viewModel: RecipeViewModel) {
    val recipeFlow = viewModel.searchRecipes(query = "pasta", cuisine = null, diet = null, maxCalories = null)
    val recipeList by recipeFlow.collectAsState(initial = emptyList())

    LazyColumn {
        items(recipeList) { recipe ->
            RecipeItem(recipe)
        }
    }
}

@Composable
fun RecipeItem(recipe: Result) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = recipe.title, style = MaterialTheme.typography.titleSmall)
        Image(
            painter = rememberImagePainter(recipe.image),
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun RecipeDetailScreen(recipe: Result) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = recipe.title, style = MaterialTheme.typography.titleLarge)
        Image(
            painter = rememberImagePainter(recipe.image),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
        TODO("Add recipe details here")
    }
}

