package com.example.drinkinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.drinkinggame.databinding.ActivityQuestionListBinding
import com.example.drinkinggame.databinding.ActivityQuestionSetsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuestionList : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private lateinit var binding: ActivityQuestionListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        binding.e1.setOnClickListener {  }

    }
}