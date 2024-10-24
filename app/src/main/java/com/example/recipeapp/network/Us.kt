package com.example.recipeapp


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Us(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "unitLong")
    val unitLong: String,
    @Json(name = "unitShort")
    val unitShort: String
)