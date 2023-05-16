package com.jldevelops.neverhaveiever

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class FailScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fail_screen)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        isHost = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, InitialScreen::class.java)
            startActivity(intent)
        }, 5000)
    }
}