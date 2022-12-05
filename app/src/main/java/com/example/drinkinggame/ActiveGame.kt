package com.example.drinkinggame

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkinggame.databinding.ActivityActiveGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var JoinRoomCode = ""
var isHost = false
var CurrentRoom = ""
class ActiveGame : AppCompatActivity() {
    private lateinit var binding: ActivityActiveGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var playerName = ""
    private var playerIcon = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val playerInfo = db.collection("Account Data").document(auth.currentUser!!.uid)
        playerInfo.get().addOnSuccessListener { document ->
            playerName = document.getString("Display Name").toString()
            playerIcon = document.getString("Icon").toString()
        }
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
        userJoin()
    }

    private fun updateUI() {

    }

    private fun gameStart(){
        if (isHost) {
            val timerSetting = db.collection("Rooms").document(hostRoomCode)
        } else {
            val timerSetting = db.collection("Rooms").document(JoinRoomCode)
        }
    }
    private fun visible(x:Int){
        when (x) {
            1 -> {
                binding.P1name.visibility = View.VISIBLE
                binding.P1icon.visibility = View.VISIBLE
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
    private fun userJoin() {
        if (isHost) {
            val addHostToRoom = db.collection("Rooms").document(hostRoomCode).collection("Players").document("PlayersData")
            binding.roomcode.append(hostRoomCode)
            CurrentRoom = hostRoomCode
            addHostToRoom.addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
                    binding.P1name.text = snapshot.getString("Player 1")
                    val p1Icon = snapshot.getString("Player 1 Icon")
                    binding.P1icon.setImageResource(
                        resources.getIdentifier(
                            p1Icon,
                            "drawable",
                            packageName
                        )
                    )
                    visible(1)
                }
            }
        }else{
            val addPlayerToRoom = db.collection("Rooms").document(JoinRoomCode).collection("Players").document("PlayersData")
            binding.roomcode.append(JoinRoomCode)
            addPlayerToRoom.addSnapshotListener { snapshot, error ->
                if (snapshot != null){
                    var numPlayers = snapshot.get("Current Players")
                    when (numPlayers) {
                        1 -> {
                            binding.P2name.text = playerName
                            binding.P2icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            visible(2)
                            addPlayerToRoom.update("Current Players", 2)
                        }
                        2 -> {
                            binding.P3name.text = playerName
                            binding.P3icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            visible(3)
                            addPlayerToRoom.update("Current Players", 3)
                        }
                        3 -> {
                            binding.P4name.text = playerName
                            binding.P4icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            visible(4)
                            addPlayerToRoom.update("Current Players", 4)
                        }
                        4 -> {
                            binding.P5name.text = playerName
                            binding.P5icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            visible(5)
                            addPlayerToRoom.update("Current Players", 5)
                        }
                        5 -> {
                            binding.P6name.text = playerName
                            binding.P6icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            visible(5)
                            addPlayerToRoom.update("Current Players", 6)
                        }
                    }
                }


            }
        }

    }
}
