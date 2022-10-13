package com.example.drinkinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActiveGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_game)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }
}