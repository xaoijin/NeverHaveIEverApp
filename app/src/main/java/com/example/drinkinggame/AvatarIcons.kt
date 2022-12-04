package com.example.drinkinggame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var selectedIcon = ""

class AvatarIcons : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_icons)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val wineglass = findViewById<ImageView>(R.id.wineglass)
        val shotglass = findViewById<ImageView>(R.id.shotglass)
        val martini = findViewById<ImageView>(R.id.martini)
        val brandy = findViewById<ImageView>(R.id.brandy)
        val hurricane = findViewById<ImageView>(R.id.hurricane)
        val beermug = findViewById<ImageView>(R.id.beermug)
        val coffee = findViewById<ImageView>(R.id.coffee)
        val vodka = findViewById<ImageView>(R.id.vodka)
        val weizen = findViewById<ImageView>(R.id.weizen)
        val select = findViewById<Button>(R.id.select)
        auth = FirebaseAuth.getInstance()
        val userIcon = db.collection("Account Data")
            .document(auth.currentUser?.uid.toString())

        wineglass.setOnClickListener {
            wineglass.setImageResource(R.drawable.checkmark)
            selectedIcon = "wineglass"
            userIcon.update("Icon", selectedIcon)
        }
        shotglass.setOnClickListener {
            shotglass.setImageResource(R.drawable.checkmark)

            selectedIcon = "shotglass"
            userIcon.update("Icon", selectedIcon)
        }
        martini.setOnClickListener {
            martini.setImageResource(R.drawable.checkmark)
            selectedIcon = "martini"
            userIcon.update("Icon", selectedIcon)
        }
        brandy.setOnClickListener {
            brandy.setImageResource(R.drawable.checkmark)
            selectedIcon = "brandy"
            userIcon.update("Icon", selectedIcon)
        }
        hurricane.setOnClickListener {
            hurricane.setImageResource(R.drawable.checkmark)
            selectedIcon = "hurricane"
            userIcon.update("Icon", selectedIcon)
        }
        beermug.setOnClickListener {
            beermug.setImageResource(R.drawable.checkmark)
            selectedIcon = "beermug"
            userIcon.update("Icon", selectedIcon)
        }
        coffee.setOnClickListener {
            coffee.setImageResource(R.drawable.checkmark)
            selectedIcon = "coffee"
            userIcon.update("Icon", selectedIcon)
        }
        vodka.setOnClickListener {
            vodka.setImageResource(R.drawable.checkmark)
            selectedIcon = "vodka"
            userIcon.update("Icon", selectedIcon)
        }
        weizen.setOnClickListener {
            weizen.setImageResource(R.drawable.checkmark)
            selectedIcon = "weizen"
            userIcon.update("Icon", selectedIcon)
        }
        select.setOnClickListener {
            val intent = Intent(this, InitialScreen::class.java)
            startActivity(intent)
        }


    }

}
