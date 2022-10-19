package com.example.drinkinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.drinkinggame.databinding.ActivityQuestionSetsBinding

var questionsetselected = 0
var questionsetedit = 0
class QuestionSets : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionSetsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionSetsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_question_sets)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        binding.e1.setOnClickListener {
            questionsetedit = 1
            val intent = Intent(this, EditQuestionSet::class.java)
            startActivity(intent)
        }
        binding.e2.setOnClickListener {
            questionsetedit = 2
            val intent = Intent(this, EditQuestionSet::class.java)
            startActivity(intent)
        }
        binding.e3.setOnClickListener {
            questionsetedit = 3
            val intent = Intent(this, EditQuestionSet::class.java)
            startActivity(intent)
        }
        binding.r1.setOnClickListener {

        }
        binding.r2.setOnClickListener {

        }
        binding.r3.setOnClickListener {

        }

        binding.s1.setOnClickListener {
            questionsetselected = 1
        }
        binding.s2.setOnClickListener {
            questionsetselected = 2
        }
        binding.s3.setOnClickListener {
            questionsetselected = 3
        }
    }
}