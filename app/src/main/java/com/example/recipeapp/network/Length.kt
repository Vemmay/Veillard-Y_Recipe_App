package com.example.recipeapp.network


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Length(
    @Json(name = "number")
    val number: Int,
    @Json(name = "unit")
    val unit: String
)