package com.example.drinkinggame

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val someDelay = Thread{
            Thread.sleep(5000)
            val intent  = Intent(this, InitialScreen::class.java)
            startActivity(intent)
            finish()
        }
        someDelay.start()
    }
}