package com.example.drinkinggame


import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class LoginScreen : AppCompatActivity() {
    private lateinit var errorInfo: TextView
    private lateinit var lEmail: TextInputEditText
    private lateinit var lPassword: TextInputEditText
    private lateinit var bLogin: Button
    private lateinit var bRegister: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val linearLayout = findViewById<LinearLayout>(R.id.loginlayout)
        val animationDrawable = linearLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(500)
        animationDrawable.setExitFadeDuration(4500)
        animationDrawable.start()


        errorInfo = findViewById(R.id.ErrorInfo)
        lEmail = findViewById(R.id.emailInput)
        lPassword = findViewById(R.id.passwordInput)
        bLogin = findViewById(R.id.log_in)
        bRegister = findViewById(R.id.register)

        errorInfo.visibility = View.INVISIBLE


        bLogin.setOnClickListener {
            loginAccount()
        }
        bRegister.setOnClickListener{
            registerAccount()
        }
    }
    private fun loginAccount(){
        val email = lEmail.text.toString()
        val psw = lPassword.text.toString()
        if (email.isEmpty() || psw.isEmpty()){
            Toast.makeText(this, "Please Enter Email or Password", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("Main", "Email:$email")
        Log.d("Main", "Password: $psw" )
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,psw)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main", "Successfully Login: ${it.result?.user?.uid}")
                val intent = Intent(this, InitialScreen::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                errorInfo.visibility = View.VISIBLE
                Log.d("Main", "Failed to Login User: ${it.message}")
            }
    }
    private fun registerAccount(){
        val email = lEmail.text.toString()
        val psw = lPassword.text.toString()
        if (email.isEmpty() || psw.isEmpty()){
            Toast.makeText(this, "Please Enter Email or Password", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("Main", "Email:" + email)
        Log.d("Main", "Password: $psw" )
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,psw)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
                Toast.makeText(this, "Account Successfully Created", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Email Address is already taken", Toast.LENGTH_SHORT).show()
            }
    }





}