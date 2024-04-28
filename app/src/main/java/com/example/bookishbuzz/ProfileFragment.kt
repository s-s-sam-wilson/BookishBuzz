package com.example.bookishbuzz

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user?.uid.toString()


        //db
        val db = Firebase.firestore
        val docRef = db.collection("users").document(uid)
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        val text = view.findViewById<TextView>(R.id.textView23)
        val text2 = view.findViewById<TextView>(R.id.textView19)
        docRef.get().addOnSuccessListener {document ->
            if (document!=null){
                text2.text = document.getString("name")
                text.text = document.getString("email")
            }
        }
        val button = view.findViewById<Button>(R.id.button4)
        button.setOnClickListener(){
            auth.signOut()
            val intent = Intent(activity, Signup::class.java)
            startActivity(intent)
        }
        return view
    }
}