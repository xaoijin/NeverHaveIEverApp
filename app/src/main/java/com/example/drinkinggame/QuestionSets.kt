package com.example.drinkinggame

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkinggame.databinding.ActivityQuestionSetsBinding
import com.google.firebase.auth.FirebaseUser


var questionsetselected = 0
var questionsetedit = 0
var questionsetrename = 0

class QuestionSets : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionSetsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionSetsBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            questionsetrename = 1
            rename()
        }
        binding.r2.setOnClickListener {
            questionsetrename = 2
            rename()
        }
        binding.r3.setOnClickListener {
            questionsetrename = 3
            rename()
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

    private fun rename(){
        val input = EditText(this)
        input.hint = "Enter Here"
        val dialogBuilder = AlertDialog.Builder(this)
        when (questionsetrename){
            1 -> {
                dialogBuilder.setMessage("What would you like to name this Question Set?")
                    .setView(input)
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, id ->
                        binding.Qs1.text = input.text.toString()
                    })
                    // negative button text and action
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            }
            2 -> {
                dialogBuilder.setMessage("What would you like to name this Question Set?")
                    .setView(input)
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, id ->
                        binding.Qs2.text = input.text.toString()
                    })
                    // negative button text and action
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            }
            3 -> {
                dialogBuilder.setMessage("What would you like to name this Question Set?")
                    .setView(input)
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, id ->
                        binding.Qs3.text = input.text.toString()
                    })
                    // negative button text and action
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            }
        }
        val alert = dialogBuilder.create()
        // show alert dialog
        alert.show()
    }
}