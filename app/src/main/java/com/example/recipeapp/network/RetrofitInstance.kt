package com.example.recipeapp.network

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitInstance {
    private const val BASE_URL = "https://api.spoonacular.com/recipes/"

    // Create a Moshi instance
    private val moshi = Moshi.Builder().build()

    // Create a Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val api: SpoonacularApiService by lazy {
        retrofit.create(SpoonacularApiService::class.java)
    }
}