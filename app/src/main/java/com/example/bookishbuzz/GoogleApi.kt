package com.example.bookishbuzz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {
    @GET("/books/v1/volumes")
    fun getGBooks(
        @Query("q") isbnQuery: String,
        @Query("key") apiKey: String
    ): Call<GResponse>
}
