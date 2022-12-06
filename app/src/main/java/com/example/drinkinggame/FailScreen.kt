package com.example.drinkinggame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FailScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fail_screen)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }
}