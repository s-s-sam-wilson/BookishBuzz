package com.example.bookishbuzz

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val baseurl1 = "https://www.googleapis.com/"

class BookActivity : AppCompatActivity() {
    private lateinit var ISBN13 : String
    private lateinit var Title : TextView
    private lateinit var BookImg : ImageView
    private lateinit var Description : TextView
    private lateinit var Author : TextView
    private lateinit var pDate : TextView
    private lateinit var Publisher : TextView
    private lateinit var Genre : TextView
    private lateinit var BackArrow : ImageButton
    private lateinit var SendButton : ImageButton
    private lateinit var auth : FirebaseAuth
    private lateinit var username : String
    private lateinit var CommentBox : TextInputEditText
    private lateinit var BuyButton : Button
    private lateinit var commentsArray: ArrayList<HashMap<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        ISBN13 = intent.getStringExtra("ISBN")!!
        Title = findViewById(R.id.textView4)
        BookImg = findViewById(R.id.imageView2)
        Description = findViewById(R.id.textView11)
        Author = findViewById(R.id.textView14)
        pDate = findViewById(R.id.textView17)
        Publisher = findViewById(R.id.textView18)
        Genre = findViewById(R.id.textView15)
        BackArrow = findViewById(R.id.imageButton2)
        CommentBox = findViewById(R.id.ReviewInput)
        SendButton = findViewById(R.id.imageButton3)

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user?.uid.toString()

        commentsArray = arrayListOf()
        val recyclerView: RecyclerView = findViewById(R.id.rv1)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = CommentAdapter(commentsArray)
        recyclerView.adapter = adapter


        val db = Firebase.firestore
        val docRef = db.collection("users").document(uid)
        docRef.get().addOnSuccessListener {document ->
            if (document!=null) {
                username = document.getString("name")!!
            }
        }
        val ref = db.collection("Review").document(ISBN13)
        ref.get().addOnSuccessListener {
            if (it.exists()){
                commentsArray = (it.get("comments") as? ArrayList<HashMap<String, Any>>)!!
            }else {
                commentsArray = arrayListOf()
            }
            val adapter = CommentAdapter(commentsArray)
            recyclerView.adapter = adapter
        }

        BuyButton = findViewById(R.id.button5)

        BackArrow.setOnClickListener{
            onBackPressed()
        }

        SendButton.setOnClickListener {
            val current = LocalDate.now()
            val ctime = LocalTime.now().toString()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formatted = current.format(formatter)
            if (CommentBox.text?.length!! > 0) {
                val document = hashMapOf(
                    "name" to username,
                    "date" to formatted,
                    "time" to ctime,
                    "review" to CommentBox.text.toString()
                )
                val reference = db.collection("Review").document(ISBN13)
                reference.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        reference.update("comments", FieldValue.arrayUnion(document))
                    } else {
                        val reviewData = hashMapOf(
                            "comments" to arrayListOf(document)
                        )
                        reference.set(reviewData)
                    }

                }
                commentsArray.add(document as HashMap<String, Any>)
                CommentBox.setText("")


                val adapter = CommentAdapter(commentsArray)
                recyclerView.adapter = adapter
            }
        }
        getGBooks()
    }

    private fun getGBooks(){
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseurl1)
            .build()
            .create(GoogleApi::class.java)
        val retrofitdata = retrofitBuilder.getGBooks("isbn:$ISBN13", "API_key");
        retrofitdata.enqueue(object : Callback<GResponse?> {
            override fun onResponse(call: Call<GResponse?>, response: Response<GResponse?>) {
                val responsebody = response.body()!!
                val items = responsebody.items
                val volinfo = items?.get(0)?.volumeInfo
                val accinfo = items?.get(0)?.accessInfo
                Title.text = volinfo?.title
                val imgurl = volinfo?.imageLinks?.thumbnail.toString()
                Picasso.get().load(imgurl).into(BookImg)
                Description.text = volinfo?.description
                Author.text = volinfo?.authors?.get(0)
                pDate.text = "Published Date: " + volinfo?.publishedDate
                Publisher.text = "Publisher: " + volinfo?.publisher
                Genre.text = volinfo?.categories?.joinToString(", ")
                val link = accinfo?.webReaderLink.toString()
                if (link!=null){
                    BuyButton.setOnClickListener(){
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(baseContext, "No links available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GResponse?>, t: Throwable) {
                Log.d("BookActivity", "onFailure: ${t.message}")
            }
        })

    }
}