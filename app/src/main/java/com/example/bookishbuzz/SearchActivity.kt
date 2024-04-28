package com.example.bookishbuzz

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val baseurl11 = "https://www.googleapis.com/"
class SearchActivity : AppCompatActivity() {
    lateinit var SearchButton : ImageButton
    lateinit var SearchInput : TextInputEditText
    lateinit var books : List<ItemsItem?>
    lateinit var recyclerView : RecyclerView
    lateinit var BackButton : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        SearchInput = findViewById(R.id.textInputEditText)
        SearchButton = findViewById(R.id.imageButton4)
        books = listOf()
        recyclerView= findViewById(R.id.rv1)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = BookAdapter(books)
        recyclerView.adapter = adapter

        SearchButton.setOnClickListener {
            getGBooks(SearchInput.text.toString())
        }
        BackButton = findViewById(R.id.imageButton5)
        BackButton.setOnClickListener {
            onBackPressed()
        }
    }
    private fun getGBooks(term : String){
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseurl1)
            .build()
            .create(GoogleApi::class.java)
        val retrofitdata = retrofitBuilder.getGBooks("$term", "API_key");
        retrofitdata.enqueue(object : Callback<GResponse?> {
            override fun onResponse(call: Call<GResponse?>, response: Response<GResponse?>) {
                val responsebody = response.body()!!
                books = responsebody.items!!
                val adapter = BookAdapter(books)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<GResponse?>, t: Throwable) {
                Log.d("BookSearchActivity", "onFailure: ${t.message}")
            }
        })

    }
}