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
        val emailPsw = hashMapOf(
            "Email" to email,
            "Password" to psw
        )
        val cleanQuestionSetNames = hashMapOf(
            "QS1Name" to "Question Set 1",
            "QS2Name" to "Question Set 2",
            "QS3Name" to "Question Set 3"
        )
        val defaultQuestions = hashMapOf(
            "Question 1" to "Never have I ever urinated in public.....",
            "Question 2" to "Never have I ever stolen anything.....",
            "Question 3" to "Never have I ever lost a bet.....",
            "Question 4" to "Never have I ever broken a bone.....",
            "Question 5" to "Never have I ever used a fake ID....",
            "Question 6" to "Never have I ever broken up with someone.....",
            "Question 7" to "Never have I ever had a one-night stand......",
            "Question 8" to "Never have I ever tried psychedelics......",
            "Question 9" to "Never have I ever stood up a date......",
            "Question 10" to "Never have I ever ghosted someone......",
            "Question 11" to "Never have I ever had to take a walk of shame......",
            "Question 12" to "Never have I ever snooped through someone’s stuff.....",
            "Question 13" to "Never have I ever dined and dashed.......",
            "Question 14" to "Never have I ever fought in public......",
            "Question 15" to "Never have I ever lied about not doing something I was suppose to do....",
            "Question 16" to "Never have I ever pretended to speak a foreign language I didn't know",
            "Question 17" to "Never have I ever gone on a blind date.....",
            "Question 18" to "Never have I ever made a prank phone call....",
            "Question 19" to "Never have I ever lied to get something cheaper than it was.....",
            "Question 20" to "Never have I ever talked my way out of getting in trouble....."
        )
        val cleanQuestions = hashMapOf(
            "Question 1" to "Never have I ever ",
            "Question 2" to "Never have I ever ",
            "Question 3" to "Never have I ever ",
            "Question 4" to "Never have I ever ",
            "Question 5" to "Never have I ever ",
            "Question 6" to "Never have I ever ",
            "Question 7" to "Never have I ever ",
            "Question 8" to "Never have I ever ",
            "Question 9" to "Never have I ever ",
            "Question 10" to "Never have I ever ",
            "Question 11" to "Never have I ever ",
            "Question 12" to "Never have I ever ",
            "Question 13" to "Never have I ever ",
            "Question 14" to "Never have I ever ",
            "Question 15" to "Never have I ever ",
            "Question 16" to "Never have I ever ",
            "Question 17" to "Never have I ever ",
            "Question 18" to "Never have I ever ",
            "Question 19" to "Never have I ever ",
            "Question 20" to "Never have I ever "
        )
        Log.d("Main", "Email:$email")
        Log.d("Main", "Password: $psw" )
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,psw)
            .addOnCompleteListener {

                db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
                    "Question Sets"
                ).document("Set1").set(defaultQuestions)
                db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
                    "Question Sets"
                ).document("Set2").set(cleanQuestions)
                db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
                    "Question Sets"
                ).document("Set3").set(cleanQuestions)
                db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
                    "Question Set Name Edit"
                ).document("Names").set(cleanQuestionSetNames)
                db.collection("Account Data").document(auth.currentUser?.uid.toString()).set(emailPsw)
                val intent = Intent(this, InitialScreen::class.java)
                startActivity(intent)
                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")

            }

    }
}