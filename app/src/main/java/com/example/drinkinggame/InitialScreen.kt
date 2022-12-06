package com.example.drinkinggame

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.media.MediaPlayer


class InitialScreen : AppCompatActivity() {
    private var db = Firebase.firestore
    private var mMediaPlayer:MediaPlayer? = null
    private lateinit var jGame: Button
    private lateinit var cQuestions: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var bLogout: Button
    private lateinit var cGame: AppCompatButton
    private lateinit var cName: AppCompatButton
    private lateinit var cIcon: AppCompatButton
    private lateinit var displayName: TextView
    private lateinit var userIcon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialscreen)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        auth = FirebaseAuth.getInstance()
        jGame = findViewById(R.id.joinGame)
        cQuestions = findViewById(R.id.cQuestionSet)
        bLogout = findViewById(R.id.log_out)
        cGame = findViewById(R.id.createGame)
        cName = findViewById(R.id.changeNameB)
        cIcon = findViewById(R.id.changeIconB)
        displayName = findViewById(R.id.displayname)
        userIcon = findViewById(R.id.iconimginitial)

        updateUI()
        //activates sound
        playSound()

        cIcon.setOnClickListener {
            stopSound()
            val intent = Intent(this,AvatarIcons::class.java)
            startActivity(intent)
        }
        cName.setOnClickListener { changeName() }
        jGame.setOnClickListener {
            stopSound()
            val intent = Intent(this, ActiveGame::class.java)
            startActivity(intent)
        }

        cQuestions.setOnClickListener {
            stopSound()
            val intent = Intent(this, QuestionSets::class.java)
            startActivity(intent)
        }
        cGame.setOnClickListener {
            stopSound()
            val intent = Intent(this, CreateGame::class.java)
            startActivity(intent)
        }
        bLogout.setOnClickListener { logout() }

    }
    //plays sound
    private fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.lobbymusic)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }
    //stops sound
    private fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
    private fun updateUI(){
        auth = FirebaseAuth.getInstance()
        var savedIcon = ""
        val userProfile = db.collection("Account Data").document(auth.currentUser?.uid.toString())
        userProfile.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Main", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("Main", "Current data: ${snapshot.data}")
                displayName.text = snapshot.getString("Display Name").toString()
                savedIcon = snapshot.getString("Icon").toString()
                userIcon.setImageResource(resources.getIdentifier(savedIcon, "drawable", packageName))
            }else {
                Log.d("Main", "Current data: null")
            }
        }
    }
    private fun updateDisplayName(){
        auth = FirebaseAuth.getInstance()
        val setDisplayName = db.collection("Account Data").document(auth.currentUser?.uid.toString())
        setDisplayName.update("Display Name", displayName.text.toString())
    }
    private fun changeName(){
        val input = EditText(this)
        input.hint = "Type Here"
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Change Your Display Name")
            .setView(input)
            .setCancelable(true)
            .setPositiveButton("Confirm",DialogInterface.OnClickListener{dialog, id ->
                displayName.text = input.text.toString()
                updateDisplayName()
            }).setNegativeButton("Cancel", DialogInterface.OnClickListener{dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        // show alert dialog
        alert.show()
    }

    private fun logout(){
        Firebase.auth.signOut()
        stopSound()
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }
}