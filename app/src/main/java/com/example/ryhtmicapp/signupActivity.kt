package com.example.ryhtmicapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ryhtmicapp.databinding.ActivityLoginActovityBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class signupActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginActovityBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginActovityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

            binding.btnsignup.setOnClickListener {
                signUpuser()
            }
                binding.Signin2.setOnClickListener{
                    val intent= Intent(this, loginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

    }


    private fun signUpuser()
    {

        val email:String= binding.emailsignup.text.toString()
        val password:String = binding.passwordsignup.text.toString()

        val confirmpassword :String= binding.confirmpasswordsignup.text.toString()

        if(email.isBlank() ||password.isBlank() ||confirmpassword.isBlank())
        {
            Toast.makeText(baseContext, "Email and password cannot be blank",
                Toast.LENGTH_SHORT).show()
            return
        }
        if(password!=confirmpassword)
        {
            Toast.makeText(baseContext, "Email and Confirm password ddo not match",
                Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) {
        if (it.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information

            Toast.makeText(this, "Created Succesfully.", Toast.LENGTH_SHORT).show()
            updateUI()



        } else {
            // If sign in fails, display a message to the user.

            Toast.makeText(this, "Enter 6 digit Password", Toast.LENGTH_SHORT).show()

        }
    }

    }
    private fun updateUI(user: FirebaseUser? =auth.currentUser)
    {
        startActivity(Intent(this,otpverify::class.java))
    }



}