package com.example.drinkinggame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SafeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safe_screen)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }
}