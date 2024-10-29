package com.example.recipeapp


import com.example.recipeapp.network.Equipment
import com.example.recipeapp.network.Ingredient
import com.example.recipeapp.network.Length
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Step(
    @Json(name = "equipment")
    val equipment: List<Equipment>,
    @Json(name = "ingredients")
    val ingredients: List<Ingredient>,
    @Json(name = "length")
    val length: Length,
    @Json(name = "number")
    val number: Int,
    @Json(name = "step")
    val step: String
)