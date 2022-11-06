package com.example.drinkinggame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InitialScreen : AppCompatActivity() {
    private lateinit var jGame: Button
    private lateinit var cQuestions: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var bLogout: Button
    private lateinit var userUID: TextView
    private lateinit var cGame: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialscreen)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        auth = FirebaseAuth.getInstance()
        jGame = findViewById(R.id.joinGame)
        cQuestions = findViewById(R.id.cQuestionSet)
        bLogout = findViewById(R.id.log_out)
        userUID = findViewById(R.id.userUID)
        cGame = findViewById(R.id.createGame)
        jGame.setOnClickListener {
            val intent = Intent(this, ActiveGame::class.java)
            startActivity(intent)
        }

        cQuestions.setOnClickListener {

            val intent = Intent(this, QuestionSets::class.java)
            startActivity(intent)
        }
        cGame.setOnClickListener {
            val intent = Intent(this, CreateGame::class.java)
            startActivity(intent)
        }
        bLogout.setOnClickListener { logout() }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(user: FirebaseUser?){
        val currentUserUid = auth.currentUser?.uid.toString()
        userUID.text = currentUserUid
    }
    private fun logout(){
        Firebase.auth.signOut()
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }
}