package com.example.drinkinggame

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnAttach
import androidx.fragment.app.DialogFragment
import com.example.drinkinggame.databinding.ActivityMainBinding

class InitialScreen : AppCompatActivity() {
    private lateinit var cLogin: Button
    private lateinit var cRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        cLogin = findViewById(R.id.cLogin)
        cRegister = findViewById(R.id.cRegister)

        cLogin.setOnClickListener{
            loginDialog()
        }
        cRegister.setOnClickListener {
            registerDialog()
        }


    }
    private fun loginDialog(){
        LoginDialog().show(supportFragmentManager, "Login")

    }
    private fun registerDialog(){
        RegisterDialog().show(supportFragmentManager, "Register")
    }
}