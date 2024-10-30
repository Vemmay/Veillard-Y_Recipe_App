package com.example.recipeapp


import com.example.recipeapp.network.ExtendedIngredient
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowRecipeByIdResponse(
    @Json(name = "extendedIngredients") val extendedIngredients: List<ExtendedIngredient>?,
)