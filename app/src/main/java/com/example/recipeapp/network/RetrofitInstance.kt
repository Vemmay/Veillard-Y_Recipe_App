package com.example.recipeapp.network

import com.example.recipeapp.ResponseTest
import com.example.recipeapp.ShowRecipeByIdResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// const val API_KEY = "f870272a56204b67b8feda691e249d2c"
const val API_KEY = "026b7efd80344817a6f4c5dba4797eda"
// Define Retrofit API interface
interface SpoonacularApiService {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String,
        @Query("cuisine") cuisine: String?,
        @Query("diet") diet: String?,
        @Query("maxCalories") maxCalories: String?
    ): ResponseTest

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): ShowRecipeByIdResponse

    @GET("recipes/{id}/analyzedInstructions")
    suspend fun getRecipeInstructions(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): List<AnalyzedInstruction>
}

object RetrofitInstance {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api = retrofit.create(SpoonacularApiService::class.java)
}