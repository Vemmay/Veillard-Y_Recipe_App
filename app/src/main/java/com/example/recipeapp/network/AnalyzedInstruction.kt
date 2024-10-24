package com.example.recipeapp


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnalyzedInstruction(
    @Json(name = "name")
    val name: String,
    @Json(name = "steps")
    val steps: List<Step>
)