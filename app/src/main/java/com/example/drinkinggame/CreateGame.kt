package com.example.drinkinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.drinkinggame.databinding.ActivityCreateGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
var timer = 0
var maxPlayer = 0
var chosenSet = 0
var roomCode = 0
var host = ""
class CreateGame : AppCompatActivity() {
    private lateinit var binding: ActivityCreateGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        auth = FirebaseAuth.getInstance()
        timer = binding.timer.text.toString().toInt()
        maxPlayer = binding.players.text.toString().toInt()
        roomCode = binding.roomcode.text.toString().toInt()
        host = auth.currentUser?.uid.toString()
    }

}