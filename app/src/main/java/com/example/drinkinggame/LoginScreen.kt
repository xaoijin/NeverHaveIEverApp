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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginScreen : AppCompatActivity() {
    private lateinit var errorInfo: TextView
    private lateinit var lEmail: TextInputEditText
    private lateinit var lPassword: TextInputEditText
    private lateinit var bLogin: Button
    private lateinit var bRegister: Button
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
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

        auth = FirebaseAuth.getInstance()
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
        Log.d("Main", "Email:$email")
        Log.d("Main", "Password: $psw" )
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,psw)
            .addOnCompleteListener {
                val cleanQuestionSetNames = hashMapOf(
                    "Question Set 1 Name" to "",
                    "Question Set 2 Name" to "",
                    "Question Set 3 Name" to ""
                )
                val cleanQuestions = hashMapOf(
                    "Question 1" to "",
                    "Question 2" to "",
                    "Question 3" to "",
                    "Question 4" to "",
                    "Question 5" to "",
                    "Question 6" to "",
                    "Question 7" to "",
                    "Question 8" to "",
                    "Question 9" to "",
                    "Question 10" to "",
                    "Question 11" to "",
                    "Question 12" to "",
                    "Question 13" to "",
                    "Question 14" to "",
                    "Question 15" to "",
                    "Question 16" to "",
                    "Question 17" to "",
                    "Question 18" to "",
                    "Question 19" to "",
                    "Question 20" to ""
                )
                db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
                    "Question Sets"
                ).document("Set1").set(cleanQuestions)
                db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
                    "Question Sets"
                ).document("Set2").set(cleanQuestions)
                db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
                    "Question Sets"
                ).document("Set3").set(cleanQuestions)
                db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
                    "Question Set Name Edit"
                ).document("Names").set(cleanQuestionSetNames)
                val intent = Intent(this, InitialScreen::class.java)
                startActivity(intent)
                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")

            }

    }
}