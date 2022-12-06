package com.example.drinkinggame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SafeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safe_screen)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        Handler().postDelayed(Runnable {
            val i = Intent(this, ActiveGame::class.java)
            startActivity(i)
        }, 5000)
    }
}