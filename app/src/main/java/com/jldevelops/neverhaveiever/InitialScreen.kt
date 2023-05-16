package com.jldevelops.neverhaveiever

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var playOnce = 0
class InitialScreen : AppCompatActivity() {
    private var db = Firebase.firestore
    private lateinit var jGame: Button
    private lateinit var cQuestions: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var bLogout: Button
    private lateinit var cGame: AppCompatButton
    private lateinit var cName: AppCompatButton
    private lateinit var cIcon: AppCompatButton
    private lateinit var displayName: TextView
    private lateinit var userIcon: ImageView
    private lateinit var codeError: TextView
    private lateinit var gameCodeET: EditText
    private lateinit var muteSound: AppCompatButton
    private lateinit var playSound: AppCompatButton
    private val database = FirebaseDatabase.getInstance()
    private var mMediaPlayer: MediaPlayer? = null
    var savedIcon: Int = 0
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
        codeError = findViewById(R.id.codeError)
        gameCodeET = findViewById(R.id.gameCode)
        muteSound = findViewById(R.id.mute)
        playSound = findViewById(R.id.playS)
        codeError.visibility = View.INVISIBLE



        updateUI()
        cIcon.setOnClickListener {

            val intent = Intent(this, AvatarIcons::class.java)
            startActivity(intent)
            muteSound()
        }
        cName.setOnClickListener {
            changeName()
        }
        jGame.setOnClickListener {
            joinGame()
            muteSound()
        }

        cQuestions.setOnClickListener {

            val intent = Intent(this, QuestionSets::class.java)
            startActivity(intent)
            muteSound()
        }
        cGame.setOnClickListener {
            val intent = Intent(this, CreateGame::class.java)
            startActivity(intent)
            muteSound()
        }
        bLogout.setOnClickListener {
            logout()
        }
        if (currentRoom.isNotEmpty()) {
            val deletePrevRoom = db.collection("Rooms").document(currentRoom)
            deletePrevRoom.addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    deletePrevRoom.delete()
                }
            }
        }
        playSound()
        muteSound.setOnClickListener {
            if(mMediaPlayer!=null && mMediaPlayer!!.isPlaying)
            {
                mMediaPlayer!!.stop()
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
        }
        playSound.setOnClickListener { playSound() }
    }

    override fun onResume() {
        super.onResume()
        playSound()
    }
    private fun playSound() {
        if(mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.lobbymusic)
            mMediaPlayer!!.isLooping = true
        }
        if(!mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.start()
        }
    }

    private fun muteSound(){
        if(mMediaPlayer!=null && mMediaPlayer!!.isPlaying)
        {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }


    private fun joinGame() {
        if (gameCodeET.text.isEmpty()) {
            Toast.makeText(applicationContext, "Please Enter a Code!", Toast.LENGTH_SHORT).show()
        } else {
            val roomCode = gameCodeET.text.toString()
            val roomRef = database.getReference("Rooms/$roomCode")

            roomRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val maxPlayers = snapshot.child("Max Players").getValue(Int::class.java)
                        val playersRef = snapshot.child("players")
                        val playersData = playersRef.getValue(object : GenericTypeIndicator<Map<String, Any>>() {})


                        if (playersData != null) {
                            val currentPlayerId = auth.uid.toString()
                            val currentPlayerName = displayName.text.toString()

                            // Find an empty player slot based on the max number of players
                            val emptyPlayerSlot = (1..maxPlayers!!).find {
                                val playerInfo = playersData["player$it"] as? Map<*, *>
                                playerInfo?.get("playerJoined") == false
                            }

                            if (emptyPlayerSlot != null) {
                                // Update the player's name, icon, and playerJoined status in the Realtime Database
                                val playerRef = roomRef.child("players").child("player$emptyPlayerSlot")
                                playerRef.child("uid").setValue(currentPlayerId)
                                playerRef.child("name").setValue(currentPlayerName)
                                playerRef.child("icon").setValue(savedIcon) // Set the initial icon value to 0 or any default value
                                playerRef.child("playerJoined").setValue(true)
                                //update playerNumber for active game activity usage
                                playerNumber = emptyPlayerSlot
                                // Navigate to the ActiveGame activity
                                currentRoom = roomCode
                                val intent = Intent(applicationContext, ActiveGame::class.java)
                                startActivity(intent)
                                muteSound()
                            } else {
                                // Room is already full
                                Toast.makeText(applicationContext, "Room is Full!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Room does not exist
                        Toast.makeText(applicationContext, "Room Does Not Exist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    Toast.makeText(applicationContext, "Failed to join the room.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun updateUI() {
        auth = FirebaseAuth.getInstance()
        val userProfile = db.collection("Account Data").document(auth.currentUser?.uid.toString())
        userProfile.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Main", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("Main", "Current data: ${snapshot.data}")
                displayName.text = snapshot.getString("Display Name").toString()
                savedIcon = snapshot.getLong("Icon")?.toInt()?: R.drawable.brandy
                userIcon.setImageResource(savedIcon)
            } else {
                Log.d("Main", "Current data: null")
            }
        }
    }

    private fun updateDisplayName() {
        auth = FirebaseAuth.getInstance()
        val setDisplayName =
            db.collection("Account Data").document(auth.currentUser?.uid.toString())
        setDisplayName.update("Display Name", displayName.text.toString())
    }

    private fun changeName() {
        val input = EditText(this)
        input.hint = "Type Here"
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Change Your Display Name")
            .setView(input)
            .setCancelable(true)
            .setPositiveButton("Confirm") { _, _ ->
                displayName.text = input.text.toString()
                updateDisplayName()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        // show alert dialog
        alert.show()
    }

    private fun logout() {
        Firebase.auth.signOut()
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
        muteSound()
    }
}