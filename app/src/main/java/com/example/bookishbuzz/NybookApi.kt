package com.example.bookishbuzz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NybookApi {
    @GET("/svc/books/v3/lists/{name}.json?api-key=API_key")
    fun getBooks(
        @Path("name") name: String
    ): Call<Response>
}
