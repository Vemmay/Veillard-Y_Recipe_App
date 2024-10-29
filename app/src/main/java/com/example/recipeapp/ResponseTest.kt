package com.example.recipeapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ResponseTest (
    @Json(name = "results")
    val recipes: List<Recipe>,
)

@JsonClass(generateAdapter = true)
data class Recipe (
    @Json(name = "id")
    val id: Int,
    @Json(name = "image")
    val image: String,
    @Json(name = "title")
    val title: String
)
