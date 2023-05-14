package com.jldevelops.neverhaveiever

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var selectedIcon: Int = 0

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
        //Selects the Icon and Deselects the previous one.
        wineglass.setOnClickListener{
            wineglass.setImageResource(R.drawable.checkmark)
            shotglass.setImageResource(R.drawable.shotglass)
            martini.setImageResource(R.drawable.martini)
            brandy.setImageResource(R.drawable.brandy)
            hurricane.setImageResource(R.drawable.hurricane)
            beermug.setImageResource(R.drawable.beermug)
            coffee.setImageResource(R.drawable.coffee)
            vodka.setImageResource(R.drawable.vodka)
            weizen.setImageResource(R.drawable.weizen)
            selectedIcon = R.drawable.wineglass
            userIcon.update("Icon", selectedIcon)
        }
        shotglass.setOnClickListener{
            wineglass.setImageResource(R.drawable.wineglass)
            shotglass.setImageResource(R.drawable.checkmark)

            martini.setImageResource(R.drawable.martini)
            brandy.setImageResource(R.drawable.brandy)
            hurricane.setImageResource(R.drawable.hurricane)
            beermug.setImageResource(R.drawable.beermug)
            coffee.setImageResource(R.drawable.coffee)
            vodka.setImageResource(R.drawable.vodka)
            weizen.setImageResource(R.drawable.weizen)
            selectedIcon = R.drawable.shotglass
            userIcon.update("Icon", selectedIcon)
        }
        martini.setOnClickListener{
            wineglass.setImageResource(R.drawable.wineglass)
            shotglass.setImageResource(R.drawable.shotglass)
            martini.setImageResource(R.drawable.checkmark)
            brandy.setImageResource(R.drawable.brandy)
            hurricane.setImageResource(R.drawable.hurricane)
            beermug.setImageResource(R.drawable.beermug)
            coffee.setImageResource(R.drawable.coffee)
            vodka.setImageResource(R.drawable.vodka)
            weizen.setImageResource(R.drawable.weizen)

            selectedIcon = R.drawable.martini
            userIcon.update("Icon", selectedIcon)
        }
        brandy.setOnClickListener{
            wineglass.setImageResource(R.drawable.wineglass)
            shotglass.setImageResource(R.drawable.shotglass)
            martini.setImageResource(R.drawable.martini)
            brandy.setImageResource(R.drawable.checkmark)
            hurricane.setImageResource(R.drawable.hurricane)
            beermug.setImageResource(R.drawable.beermug)
            coffee.setImageResource(R.drawable.coffee)
            vodka.setImageResource(R.drawable.vodka)
            weizen.setImageResource(R.drawable.weizen)

            selectedIcon = R.drawable.brandy
            userIcon.update("Icon", selectedIcon)
        }
        hurricane.setOnClickListener{
            wineglass.setImageResource(R.drawable.wineglass)
            shotglass.setImageResource(R.drawable.shotglass)
            martini.setImageResource(R.drawable.martini)
            brandy.setImageResource(R.drawable.brandy)
            hurricane.setImageResource(R.drawable.checkmark)
            beermug.setImageResource(R.drawable.beermug)
            coffee.setImageResource(R.drawable.coffee)
            vodka.setImageResource(R.drawable.vodka)
            weizen.setImageResource(R.drawable.weizen)

            selectedIcon = R.drawable.hurricane
            userIcon.update("Icon", selectedIcon)
        }
        beermug.setOnClickListener{
            wineglass.setImageResource(R.drawable.wineglass)
            shotglass.setImageResource(R.drawable.shotglass)
            martini.setImageResource(R.drawable.martini)
            brandy.setImageResource(R.drawable.brandy)
            hurricane.setImageResource(R.drawable.hurricane)
            beermug.setImageResource(R.drawable.checkmark)
            coffee.setImageResource(R.drawable.coffee)
            vodka.setImageResource(R.drawable.vodka)
            weizen.setImageResource(R.drawable.weizen)

            selectedIcon = R.drawable.beermug
            userIcon.update("Icon", selectedIcon)
        }
        coffee.setOnClickListener{
            wineglass.setImageResource(R.drawable.wineglass)
            shotglass.setImageResource(R.drawable.shotglass)
            martini.setImageResource(R.drawable.martini)
            brandy.setImageResource(R.drawable.brandy)
            hurricane.setImageResource(R.drawable.hurricane)
            beermug.setImageResource(R.drawable.beermug)
            coffee.setImageResource(R.drawable.checkmark)
            vodka.setImageResource(R.drawable.vodka)
            weizen.setImageResource(R.drawable.weizen)

            selectedIcon = R.drawable.coffee
            userIcon.update("Icon", selectedIcon)
        }
        vodka.setOnClickListener{
            wineglass.setImageResource(R.drawable.wineglass)
            shotglass.setImageResource(R.drawable.shotglass)
            martini.setImageResource(R.drawable.martini)
            brandy.setImageResource(R.drawable.brandy)
            hurricane.setImageResource(R.drawable.hurricane)
            beermug.setImageResource(R.drawable.beermug)
            coffee.setImageResource(R.drawable.coffee)
            vodka.setImageResource(R.drawable.checkmark)
            weizen.setImageResource(R.drawable.weizen)

            selectedIcon = R.drawable.vodka
            userIcon.update("Icon", selectedIcon)
        }
        weizen.setOnClickListener{
            wineglass.setImageResource(R.drawable.wineglass)
            shotglass.setImageResource(R.drawable.shotglass)
            martini.setImageResource(R.drawable.martini)
            brandy.setImageResource(R.drawable.brandy)
            hurricane.setImageResource(R.drawable.hurricane)
            beermug.setImageResource(R.drawable.beermug)
            coffee.setImageResource(R.drawable.coffee)
            vodka.setImageResource(R.drawable.vodka)
            weizen.setImageResource(R.drawable.checkmark)

            selectedIcon = R.drawable.weizen
            userIcon.update("Icon", selectedIcon)
        }
        select.setOnClickListener {
            val intent = Intent(this, InitialScreen::class.java)
            startActivity(intent)
        }


    }

}
