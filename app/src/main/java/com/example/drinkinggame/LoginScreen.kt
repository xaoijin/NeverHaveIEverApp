package com.example.drinkinggame
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        errorInfo = findViewById(R.id.ErrorInfo)
        lEmail = findViewById(R.id.emailInput)
        lPassword = findViewById(R.id.passwordInput)
        bLogin = findViewById(R.id.log_in)
        bRegister = findViewById(R.id.register)

        errorInfo.visibility = View.INVISIBLE


        bLogin.setOnClickListener { }
        bRegister.setOnClickListener{
            registerAccount()
        }
    }
    private fun registerAccount(){
         val email = lEmail.text.toString()
        val psw = lPassword.text.toString()
        if (email.isEmpty() || psw.isEmpty()){
            Toast.makeText(this, "Please Enter Text Email or Password", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("Main", "Email:" + email)
        Log.d("Main", "Password: $psw" )
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,psw)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")
            }
    }





}