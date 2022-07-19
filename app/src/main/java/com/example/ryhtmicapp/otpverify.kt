package com.example.ryhtmicapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ryhtmicapp.databinding.ActivityOtpverifyBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class otpverify : AppCompatActivity() {
    var number : String =""

    // create instance of firebase auth
    private lateinit var auth: FirebaseAuth
    private var storedVerificationId:String?=""

   private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var binding:ActivityOtpverifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOtpverifyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        // start verification on click of the button
      binding.getotp.setOnClickListener {
          var otp:String=binding.etph!!.text.toString()
          var otp2:String= "+91$otp"
          startphonenumberverfication(otp2)
          Toast.makeText(this, "Code Sent", Toast.LENGTH_SHORT).show()
          Toast.makeText(this, "please wait for 20 sec ", Toast.LENGTH_SHORT).show()
      }
        binding.vfotp.setOnClickListener {
            binding.probar.visibility=View.VISIBLE
            verifyphonenumberwithotp(storedVerificationId,binding.etotp!!.text.toString())
        }
        auth=Firebase.auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
               Log.d(TAG,"onVerificationCompleted:$credential")
                signInWithCredential(credential)
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("fail" , "onVerificationFailed  $e")
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("sent","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token

                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase

            }
        }

    }

    /*override fun onStart() {
        super.onStart()
        val currentuser =auth.currentUser
        updateUI(currentuser)
    }*/
    // this method sends the verification code
    // and starts the callback of verification
    // which is implemented above in onCreate
    private fun startphonenumberverfication(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("started" , "Auth started")
    }
    private fun verifyphonenumberwithotp(verificationId:String?,code: String) {
        // below line is used for getting
        // credentials from our verification id and code.

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential)
    }
    private fun signInWithCredential(credential: PhoneAuthCredential)
    {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(TAG,"signinwithcredentail success")
                    val user=it.result?.user
                    // Sign in success, update UI with the signed-in user's information

                    Toast.makeText(this, "Welcome."+user, Toast.LENGTH_SHORT).show()
                    updateUI()
                    binding.probar.visibility=View.GONE

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG,"signinwithcredentail failure",it.exception)
                    Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show()
                    if(it.exception is FirebaseAuthInvalidCredentialsException)
                    {
                        //verfication code invalid
                        Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
    }
    private fun updateUI(user: FirebaseUser? =auth.currentUser)
    {
        startActivity(Intent(this,upload_activity::class.java))
    }
    companion object{
        private const val TAG="otpverify"
    }

}


