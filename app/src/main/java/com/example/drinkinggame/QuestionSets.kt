package com.example.drinkinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class QuestionSets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_sets)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }
}