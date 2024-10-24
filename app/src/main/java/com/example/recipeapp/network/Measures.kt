package com.example.recipeapp


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Measures(
    @Json(name = "metric")
    val metric: Metric,
    @Json(name = "us")
    val us: Us
)