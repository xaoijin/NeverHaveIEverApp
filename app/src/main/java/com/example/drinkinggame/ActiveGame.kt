package com.example.drinkinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.drinkinggame.databinding.ActivityActiveGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActiveGame : AppCompatActivity() {
    private lateinit var binding: ActivityActiveGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

    }
}