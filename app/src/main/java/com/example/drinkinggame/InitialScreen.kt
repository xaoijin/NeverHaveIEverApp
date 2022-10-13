package com.example.drinkinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InitialScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

    }
}