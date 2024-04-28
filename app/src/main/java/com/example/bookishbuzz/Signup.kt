package com.example.bookishbuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signupbtn: Button
    private lateinit var emailinput: TextInputEditText
    private lateinit var pwdinput : TextInputEditText
    private lateinit var cpwsdinput: TextInputEditText
    private lateinit var nameinput: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")

        //email and pwd input
        emailinput = findViewById(R.id.emailtxt)
        pwdinput = findViewById(R.id.passwordtxt)
        cpwsdinput = findViewById(R.id.cnfpwdtxt)
        nameinput = findViewById(R.id.Nametxt2)
        //switch to login page
        var logintext = findViewById<TextView>(R.id.textView2)
        logintext.setOnClickListener(){
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        //firebase
        auth = Firebase.auth
        //db
        val db = Firebase.firestore
        //signup button
        signupbtn = findViewById(R.id.button)
        signupbtn.setOnClickListener(){
            if (nameinput.text.toString().isEmpty())
                Toast.makeText(baseContext, "Name is empty", Toast.LENGTH_SHORT).show()
            else if (emailinput.text.toString().isEmpty())
                Toast.makeText(baseContext, "Email is empty", Toast.LENGTH_SHORT).show()
            else if (!emailinput.text.toString().matches(emailRegex)){
                Toast.makeText(baseContext, "Invalid Email", Toast.LENGTH_SHORT).show()
            }else if (pwdinput.text.toString().isEmpty()){
                Toast.makeText(baseContext, "Password is empty", Toast.LENGTH_SHORT).show()
            }else if (pwdinput.text.toString().compareTo(cpwsdinput.text.toString()) != 0){
                Toast.makeText(baseContext, "Passwords didn't match", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(emailinput.text.toString(), pwdinput.text.toString()).addOnCompleteListener(this){ task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        Toast.makeText(
                            baseContext,
                            "Account Created.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val user = auth.currentUser
                        val uid = user?.uid.toString()
                        val document = hashMapOf(
                            "name" to nameinput.text.toString(),
                            "email" to emailinput.text.toString(),
                        )
                        db.collection("users").document(uid).set(document)
                        try {

                            var intent = Intent(this, PreferencePage::class.java)
                            startActivity(intent)
                        }catch (e : Exception){
                            Toast.makeText(
                                baseContext,
                                e.message.toString(),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    } else {
                        // If sign up fails
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                }
            }
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            var intent = Intent(this, BottomNav::class.java)
            startActivity(intent)
        }
    }
}