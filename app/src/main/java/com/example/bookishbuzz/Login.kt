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
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var signinbtn : Button
    private lateinit var emailtxt : TextInputEditText
    private lateinit var pwdtxt : TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var registertext = findViewById<TextView>(R.id.textView2)
        registertext.setOnClickListener(){
            var intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        signinbtn = findViewById(R.id.button)
        emailtxt = findViewById(R.id.emailtxt)
        pwdtxt = findViewById(R.id.passwordtxt)

        signinbtn.setOnClickListener(){
            if(emailtxt.text.toString().isEmpty()){
                Toast.makeText(baseContext, "Email is empty", Toast.LENGTH_SHORT).show();
            }else if (!emailtxt.text.toString().matches(emailRegex)){
                Toast.makeText(baseContext, "Invalid Email", Toast.LENGTH_SHORT).show();
            }else if (pwdtxt.text.toString().isEmpty()){
                Toast.makeText(baseContext, "Password is empty", Toast.LENGTH_SHORT).show();
            }else{
                auth.signInWithEmailAndPassword(emailtxt.text.toString(), pwdtxt.text.toString())
                    .addOnCompleteListener(){task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            Toast.makeText(
                                baseContext,
                                "Account Logged in.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val user = auth.currentUser
                            var intent = Intent(this, PreferencePage::class.java)
                            startActivity(intent)
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

        auth = Firebase.auth

    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                var intent = Intent(this, PreferencePage::class.java)
                startActivity(intent)
            }
        }catch (e : Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}