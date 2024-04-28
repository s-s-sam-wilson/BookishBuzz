package com.example.bookishbuzz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.BufferedReader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.squareup.picasso.Picasso

const val baseurl = "https://api.nytimes.com"

class HomeFragment : Fragment() {
    private lateinit var welcometxt: TextView
    private lateinit var genre0: TextView
    private lateinit var genre1: TextView
    private lateinit var genre2: TextView
    private lateinit var genre3: TextView
    private lateinit var auth : FirebaseAuth
    private lateinit var Genre: ArrayList<String>
    val url = "https://api.nytimes.com/svc/books/v3/lists/"
    val api_key = "lPV6ozUoSOl12EXtjFswaundr05cNjE5"
    private lateinit var SearchButton : ImageButton

    fun mapXmlTextToApiEncodedName(xmlText: String): String {
        return when (xmlText) {
            "Fiction" -> "combined-print-and-e-book-fiction"
            "Children" -> "childrens-middle-grade"
            "Culture" -> "culture"
            "Travel" -> "travel"
            "Education" -> "education"
            "Food" -> "food-and-fitness"
            "Health" -> "health"
            "Humor" -> "humor"
            "Science" -> "science"
            "Sports" -> "sports"
            "Business" -> "business-books"
            "Relationships" -> "relationships"
            "Manga" -> "manga"
            "Spirtuality" -> "religion-spirituality-and-faith"
            "Political" -> "hardcover-political-books"
            "Fashion" -> "fashion-manners-and-customs"
            else -> ""
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        welcometxt = view.findViewById(R.id.textView6)
        genre0 = view.findViewById(R.id.textView7)
        genre1 = view.findViewById(R.id.textView12)
        genre2 = view.findViewById(R.id.textView8)
        genre3 = view.findViewById(R.id.textView9)
        SearchButton = view.findViewById(R.id.imageButton)

        //auth
        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user?.uid.toString()


        //db
        val db = Firebase.firestore
        val docRef = db.collection("users").document(uid)
        docRef.get().addOnSuccessListener {document ->
            if (document!=null){
                val name = document.getString("name")
                if (name!=null){
                    welcometxt.text = "Welcome "+name
                }
                Genre = document.get("genre") as ArrayList<String>
                genre0.text = Genre.get(0)
                genre1.text = Genre.get(1)
                genre2.text = Genre.get(2)
                genre3.text = Genre.get(3)
                val genre1cards = listOf(R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6)
                val genre2cards = listOf(R.id.card11, R.id.card12, R.id.card13, R.id.card14, R.id.card15, R.id.card16)
                val genre3cards = listOf(R.id.card21, R.id.card22, R.id.card23, R.id.card24, R.id.card25, R.id.card26)
                val genre4cards = listOf(R.id.card31, R.id.card32, R.id.card33, R.id.card34, R.id.card35, R.id.card36)
                getBooks(mapXmlTextToApiEncodedName(Genre.get(0)), genre1cards);
                getBooks(mapXmlTextToApiEncodedName(Genre.get(1)), genre2cards);
                getBooks(mapXmlTextToApiEncodedName(Genre.get(2)), genre3cards);
                getBooks(mapXmlTextToApiEncodedName(Genre.get(3)), genre4cards);

            }

        }
        SearchButton.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun getBooks(s : String, l : List<Int> ) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseurl)
            .build()
            .create(NybookApi::class.java)
        val retrofitData = retrofitBuilder.getBooks(s)
        retrofitData.enqueue(object : Callback<com.example.bookishbuzz.Response?> {
            override fun onResponse(
                call: Call<com.example.bookishbuzz.Response?>,
                response: Response<com.example.bookishbuzz.Response?>
            ) {
                try {
                    val responsebody = response.body()!!
                    val result = responsebody?.results
                    val listbooks = result?.books!!
                    val bookiterator = listbooks.listIterator()
                    val carditerator = l.listIterator()
                    while (bookiterator.hasNext() && carditerator.hasNext()) {
                        val card = view?.findViewById<CardView>(carditerator.next())
                        val linear = card?.getChildAt(0) as LinearLayout
                        val textView = linear.getChildAt(1) as TextView
                        val cbook = bookiterator.next()
                        var btitle = cbook?.title.toString()
                        if (btitle.length>13){
                            btitle = btitle.substring(0,13)
                        }
                        textView.text = btitle
                        val imageUrl = cbook?.bookImage
                        val imgview = linear.getChildAt(0) as ImageView
                        Picasso.get().load(imageUrl).into(imgview)
                        card.setOnClickListener(){
                            /*intent.putExtra("bookobject", cbook)*/
                            val intent = Intent(activity, BookActivity::class.java)
                            intent.putExtra("ISBN", cbook?.primaryIsbn13);
                            startActivity(intent)
                        }

                    }
                }catch (e: Exception){

                }

            }

            override fun onFailure(call: Call<com.example.bookishbuzz.Response?>, t: Throwable) {
                Log.d("HomeFragment", "onFailure: ${t.message}")
            }
        })

    }

}