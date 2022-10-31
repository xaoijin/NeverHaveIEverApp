package com.example.drinkinggame

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackgroundTintList
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
            clearSelect()
            questionsetselected = 1
            binding.s1.text = "Selected"
            binding.s1.backgroundTintList= ContextCompat.getColorStateList(applicationContext, R.color.DarkViolet)
        }
        binding.s2.setOnClickListener {
            clearSelect()
            questionsetselected = 2
            binding.s1.text = "Selected"
            binding.s1.backgroundTintList= ContextCompat.getColorStateList(applicationContext, R.color.DarkViolet)
        }
        binding.s3.setOnClickListener {
            clearSelect()
            questionsetselected = 3
            binding.s1.text = "Selected"
            binding.s1.backgroundTintList= ContextCompat.getColorStateList(applicationContext, R.color.DarkViolet)
        }
    }
    private fun clearSelect(){
        binding.s1.text = buildString {
        append("Select")
    }
        binding.s2.text = buildString {
        append("Select")
    }
        binding.s3.text = buildString {
        append("Select")
    }
        binding.s1.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.MediumPurple)
        binding.s2.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.MediumPurple)
        binding.s3.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.MediumPurple)
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