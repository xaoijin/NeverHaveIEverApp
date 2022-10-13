package com.example.drinkinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CameraMiniGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_mini_game)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }
}