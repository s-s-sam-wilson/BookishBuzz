package com.example.bookishbuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PreferencePage : AppCompatActivity() {
    private lateinit var nextbt : Button
    private lateinit var cpgrp2 : ChipGroup
    private lateinit var auth: FirebaseAuth
    private val selectedGenre = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_page)

        auth = Firebase.auth
        val db = Firebase.firestore

        nextbt = findViewById(R.id.button3)
        cpgrp2 = findViewById(R.id.chipGroup2)

        val selectedGenre = mutableListOf<String>()



        nextbt.setOnClickListener{
            val user = auth.currentUser
            val uid = user?.uid.toString()
                for (i in 0 until cpgrp2.childCount) {
                    val chip = cpgrp2.getChildAt(i) as? Chip
                    chip?.let {
                        // Check if the Chip is checked
                        if (it.isChecked) {
                            // Add the text of the checked Chip to the selectedGenre list
                            selectedGenre.add(it.text.toString())
                        }
                    }
                }
            if(selectedGenre.size < 4){
                Toast.makeText(baseContext,"Select atleast 4 genre", Toast.LENGTH_SHORT).show()
            }else{
                val prefdoc = mapOf(
                    "genre" to selectedGenre,
                )
                db.collection("users").document(uid).set(prefdoc, SetOptions.merge())
                val intent = Intent(this, BottomNav::class.java)
                startActivity(intent)
            }


        }


    }
}