package com.jldevelops.neverhaveiever

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jldevelops.neverhaveiever.databinding.ActivityActiveGameBinding

var isHost = false
var currentRoom = ""
var playerNumber = 1

class ActiveGame : AppCompatActivity() {
    private lateinit var binding: ActivityActiveGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var playerName = ""
    private var playerIcon = 0
    private val database = FirebaseDatabase.getInstance()
    private val roomRef = database.getReference("Rooms").child(currentRoom)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        invisible()
        binding.roomcode.text = buildString {
        append("Room Code: ")
        append(roomRef.key)
    }
        if(isHost){
            startGameAsHost()

        }else{
            binding.startBtn.visibility = View.INVISIBLE
            binding.endBtn.visibility = View.INVISIBLE
            startGameAsPlayer()

        }
        binding.startBtn.setOnClickListener {
            binding.startBtn.visibility = View.GONE
            gameStart()

        }
        startRoomListener()
    }

    private fun startRoomListener() {
        roomRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val roomData = snapshot.value as? Map<*, *>
                    val playersData = roomData?.get("players") as? Map<*, *>
                    playersData?.let { players ->
                        for ((playerPosition, playerData) in players) {
                            playerName = ((playerData as? Map<*, *>)?.get("name") as? String).toString()
                            playerIcon = (((playerData as? Map<*, *>)?.get("icon") as? Long) ?: 0).toInt()

                            // Update the UI with the player's name and icon based on the player position
                            when (playerPosition) {
                                "player1" -> {
                                    binding.P1name.text = playerName
                                    binding.P1icon.setImageResource(playerIcon)
                                    visible(1)
                                }
                                "player2" -> {
                                    binding.P2name.text = playerName
                                    binding.P2icon.setImageResource(playerIcon)
                                    visible(2)
                                }
                                // Add other player cases here
                                "player3" -> {
                                    binding.P3name.text = playerName
                                    binding.P3icon.setImageResource(playerIcon)
                                    visible(3)
                                }
                                "player4" -> {
                                    binding.P4name.text = playerName
                                    binding.P4icon.setImageResource(playerIcon)
                                    visible(4)
                                }
                                "player5" -> {
                                    binding.P5name.text = playerName
                                    binding.P5icon.setImageResource(playerIcon)
                                    visible(5)
                                }
                                "player6" -> {
                                    binding.P6name.text = playerName
                                    binding.P6icon.setImageResource(playerIcon)
                                    visible(6)
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Main", "Start Room Listener Function Failed")
            }
        })
    }

    private fun startGameAsHost() {
        auth = FirebaseAuth.getInstance()
        val playerInfoRef = db.collection("Account Data").document(auth.uid.toString())
        val playerRef = database.getReference("Rooms/$currentRoom/players/player1")
        playerInfoRef.get().addOnSuccessListener { document->
            if (document != null){
                val currentPlayerId = auth.uid.toString()
                playerName = document.getString("Display Name").toString()
                binding.P1name.text = playerName
                playerRef.child("name").setValue(playerName)
                playerIcon = document.getLong("Icon")?.toInt() ?: 0
                binding.P1icon.setImageResource(playerIcon)
                playerRef.child("icon").setValue(playerIcon)
                playerRef.child("uid").setValue(currentPlayerId)
                playerRef.child("playerJoined").setValue(true)
            }
        }

    }

    private fun startGameAsPlayer() {
        when(playerNumber){
            1 -> {
                binding.P1name.text = playerName
                binding.P1icon.setImageResource(playerIcon)
            }
            2 -> {
                binding.P2name.text = playerName
                binding.P2icon.setImageResource(playerIcon)
            }
            3 -> {
                binding.P3name.text = playerName
                binding.P3icon.setImageResource(playerIcon)
            }
            4 -> {
                binding.P4name.text = playerName
                binding.P4icon.setImageResource(playerIcon)
            }
            5 -> {
                binding.P5name.text = playerName
                binding.P5icon.setImageResource(playerIcon)
            }
            6 -> {
                binding.P6name.text = playerName
                binding.P6icon.setImageResource(playerIcon)
            }

        }
        visible(playerNumber)
    }
    private fun gameStart(){

    }
    private fun changeQuestion(){

    }
    private fun startTimer(){

    }

    private fun checkDrunk() {
        var pCounter: Int
        val checkPCounter = db.collection("Rooms").document(currentRoom).collection("Players")
            .document("PlayersData")
        checkPCounter.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                when (playerNumber) {
                    1 -> {
                        pCounter = snapshot.get("Player 1 Counter").toString().toInt()
                        if (pCounter == 3) {
                            showCameraMiniGameDialog()
                        }
                    }
                    2 -> {
                        pCounter = snapshot.get("Player 2 Counter").toString().toInt()
                        if (pCounter == 3) {
                            showCameraMiniGameDialog()
                        }
                    }
                    3 -> {
                        pCounter = snapshot.get("Player 3 Counter").toString().toInt()
                        if (pCounter == 3) {
                            showCameraMiniGameDialog()
                        }
                    }
                    // Add other player cases here
                    4 -> {
                        pCounter = snapshot.get("Player 4 Counter").toString().toInt()
                        if (pCounter == 3) {
                            showCameraMiniGameDialog()
                        }
                    }
                    5 -> {
                        pCounter = snapshot.get("Player 5 Counter").toString().toInt()
                        if (pCounter == 5) {
                            showCameraMiniGameDialog()
                        }
                    }
                    6 -> {
                        pCounter = snapshot.get("Player 6 Counter").toString().toInt()
                        if (pCounter == 6) {
                            showCameraMiniGameDialog()
                        }
                    }
                }
            }
        }
    }

    private fun showCameraMiniGameDialog() {
        val cameraMiniGameDialogFragment = CameraMiniGameDialogFragment()
        cameraMiniGameDialogFragment.show(supportFragmentManager, "cameraMiniGameDialog")
    }


    private fun deleteRoom() {
        val intent = Intent(this, InitialScreen::class.java)
        startActivity(intent)
    }



    private fun invisible() {
        binding.P1name.visibility = View.INVISIBLE
        binding.P2name.visibility = View.INVISIBLE
        binding.P3name.visibility = View.INVISIBLE
        binding.P4name.visibility = View.INVISIBLE
        binding.P5name.visibility = View.INVISIBLE
        binding.P6name.visibility = View.INVISIBLE
        binding.P1icon.visibility = View.INVISIBLE
        binding.P2icon.visibility = View.INVISIBLE
        binding.P3icon.visibility = View.INVISIBLE
        binding.P4icon.visibility = View.INVISIBLE
        binding.P5icon.visibility = View.INVISIBLE
        binding.P6icon.visibility = View.INVISIBLE
    }

    private fun visible(x: Int) {
        when (x) {
            1 -> {
                binding.P1name.visibility = View.VISIBLE
                binding.P1icon.visibility = View.VISIBLE
                binding.startBtn.visibility = View.VISIBLE
                binding.endBtn.visibility = View.VISIBLE
            }
            2 -> {
                binding.P1name.visibility = View.VISIBLE
                binding.P1icon.visibility = View.VISIBLE
                binding.P2name.visibility = View.VISIBLE
                binding.P2icon.visibility = View.VISIBLE

            }
            3 -> {
                binding.P1name.visibility = View.VISIBLE
                binding.P1icon.visibility = View.VISIBLE
                binding.P2name.visibility = View.VISIBLE
                binding.P2icon.visibility = View.VISIBLE
                binding.P3name.visibility = View.VISIBLE
                binding.P3icon.visibility = View.VISIBLE

            }
            4 -> {
                binding.P1name.visibility = View.VISIBLE
                binding.P1icon.visibility = View.VISIBLE
                binding.P2name.visibility = View.VISIBLE
                binding.P2icon.visibility = View.VISIBLE
                binding.P3name.visibility = View.VISIBLE
                binding.P3icon.visibility = View.VISIBLE
                binding.P4name.visibility = View.VISIBLE
                binding.P4icon.visibility = View.VISIBLE

            }
            5 -> {
                binding.P1name.visibility = View.VISIBLE
                binding.P1icon.visibility = View.VISIBLE
                binding.P2name.visibility = View.VISIBLE
                binding.P2icon.visibility = View.VISIBLE
                binding.P3name.visibility = View.VISIBLE
                binding.P3icon.visibility = View.VISIBLE
                binding.P4name.visibility = View.VISIBLE
                binding.P4icon.visibility = View.VISIBLE
                binding.P5name.visibility = View.VISIBLE
                binding.P5icon.visibility = View.VISIBLE

            }
            6 -> {
                binding.P1name.visibility = View.VISIBLE
                binding.P1icon.visibility = View.VISIBLE
                binding.P2name.visibility = View.VISIBLE
                binding.P2icon.visibility = View.VISIBLE
                binding.P3name.visibility = View.VISIBLE
                binding.P3icon.visibility = View.VISIBLE
                binding.P4name.visibility = View.VISIBLE
                binding.P4icon.visibility = View.VISIBLE
                binding.P5name.visibility = View.VISIBLE
                binding.P5icon.visibility = View.VISIBLE
                binding.P6name.visibility = View.VISIBLE
                binding.P6icon.visibility = View.VISIBLE

            }
        }

    }


}
