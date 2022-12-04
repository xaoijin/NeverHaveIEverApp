package com.example.drinkinggame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CameraMiniGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_mini_game)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }
}