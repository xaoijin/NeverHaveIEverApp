package com.example.drinkinggame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class AvatarIcons : AppCompatActivity() {

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
        val empty = findViewById<ImageView>(R.id.empty)
        val select = findViewById<Button>(R.id.select)

        wineglass.setOnClickListener{
            empty.setImageResource(R.drawable.wineglass)

        }
        shotglass.setOnClickListener{
            empty.setImageResource(R.drawable.shotglass)

        }
        martini.setOnClickListener{
            empty.setImageResource(R.drawable.martini)

        }
        brandy.setOnClickListener{
            empty.setImageResource(R.drawable.brandy)

        }
        hurricane.setOnClickListener{
            empty.setImageResource(R.drawable.hurricane)

        }
        beermug.setOnClickListener{
            empty.setImageResource(R.drawable.beermug)

        }
        coffee.setOnClickListener{
            empty.setImageResource(R.drawable.coffee)

        }
        vodka.setOnClickListener{
            empty.setImageResource(R.drawable.vodka)

        }
        weizen.setOnClickListener{
            empty.setImageResource(R.drawable.weizen)

        }
        select.setOnClickListener {
            val intent  = Intent(this, CreateGame::class.java)
            startActivity(intent)
        }



        }

    }
