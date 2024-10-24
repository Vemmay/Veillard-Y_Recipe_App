package com.example.recipeapp


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetRecipeResponse(
    @Json(name = "number")
    val number: Int,
    @Json(name = "offset")
    val offset: Int,
    @Json(name = "results")
    val results: List<Result>,
    @Json(name = "totalResults")
    val totalResults: Int
)