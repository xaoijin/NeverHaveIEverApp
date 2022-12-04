package com.example.drinkinggame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkinggame.databinding.ActivityActiveGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var JoinRoomCode = ""
var isHost = false

class ActiveGame : AppCompatActivity() {
    private lateinit var binding: ActivityActiveGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        updateUI()
        playersJoin()
    }

    private fun updateUI() {
        if (isHost) {
            val timerSetting = db.collection("Rooms").document(hostRoomCode)
        } else {
            val timerSetting = db.collection("Rooms").document(JoinRoomCode)
        }
    }

    private fun playersJoin() {
        auth = FirebaseAuth.getInstance()
        val playerInfo = db.collection("Account Data").document(auth.currentUser!!.uid)
        if (isHost) {
            binding.roomcode.append(hostRoomCode)
            val roomSettings = db.collection("Rooms").document(hostRoomCode)
            roomSettings.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                if (e != null) {
                    Log.w("Main", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val playersinroom = snapshot.get("Current Players")
                    if (isHost) {
                        playerInfo.get().addOnSuccessListener { document ->
                            val playerIcon = document.getString("Icon")
                            binding.P1name.text = document.getString("Display Name")
                            binding.P1icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            roomSettings.update("Current Players", 1)
                        }


                    } else if (!isHost) {
                        playerInfo.get().addOnSuccessListener { document ->
                            val playerIcon = document.getString("Icon")
                            binding.P2name.text = document.getString("Display Name")
                            binding.P2icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            roomSettings.update("Current Players", 2)
                        }

                    }
                    when (playersinroom) {
                        2 -> {
                            playerInfo.get().addOnSuccessListener { document ->


                                val playerIcon = document.getString("Icon")
                                binding.P3name.text = document.getString("Display Name")
                                binding.P3icon.setImageResource(
                                    resources.getIdentifier(
                                        playerIcon,
                                        "drawable",
                                        packageName
                                    )
                                )
                                roomSettings.update("Current Players", 3)


                            }
                        }
                        3 -> {
                            playerInfo.get().addOnSuccessListener { document ->


                                val playerIcon = document.getString("Icon")
                                binding.P4name.text = document.getString("Display Name")
                                binding.P4icon.setImageResource(
                                    resources.getIdentifier(
                                        playerIcon,
                                        "drawable",
                                        packageName
                                    )
                                )
                                roomSettings.update("Current Players", 4)


                            }
                        }
                        4 -> {
                            playerInfo.get().addOnSuccessListener { document ->


                                val playerIcon = document.getString("Icon")
                                binding.P5name.text = document.getString("Display Name")
                                binding.P5icon.setImageResource(
                                    resources.getIdentifier(
                                        playerIcon,
                                        "drawable",
                                        packageName
                                    )
                                )
                                roomSettings.update("Current Players", 5)


                            }
                        }
                        5 -> {
                            playerInfo.get().addOnSuccessListener { document ->


                                val playerIcon = document.getString("Icon")
                                binding.P3name.text = document.getString("Display Name")
                                binding.P3icon.setImageResource(
                                    resources.getIdentifier(
                                        playerIcon,
                                        "drawable",
                                        packageName
                                    )
                                )
                                roomSettings.update("Current Players", 6)


                            }
                        }
                    }
                }
            }
        } else {
            binding.roomcode.append(JoinRoomCode)
            val roomSettings = db.collection("Rooms").document(JoinRoomCode)
            roomSettings.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                if (e != null) {
                    Log.w("Main", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val playersinroom = snapshot.getString("Current Players")!!.toInt()
                    if (isHost) {
                        playerInfo.get().addOnSuccessListener { document ->
                            val playerIcon = document.getString("Icon")
                            binding.P1name.text = document.getString("Display Name")
                            binding.P1icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            roomSettings.update("Current Players", 1)
                        }


                    } else if (!isHost) {
                        playerInfo.get().addOnSuccessListener { document ->
                            val playerIcon = document.getString("Icon")
                            binding.P2name.text = document.getString("Display Name")
                            binding.P2icon.setImageResource(
                                resources.getIdentifier(
                                    playerIcon,
                                    "drawable",
                                    packageName
                                )
                            )
                            roomSettings.update("Current Players", 2)
                        }

                    }
                    when (playersinroom) {
                        2 -> {
                            playerInfo.get().addOnSuccessListener { document ->


                                val playerIcon = document.getString("Icon")
                                binding.P3name.text = document.getString("Display Name")
                                binding.P3icon.setImageResource(
                                    resources.getIdentifier(
                                        playerIcon,
                                        "drawable",
                                        packageName
                                    )
                                )
                                roomSettings.update("Current Players", 3)


                            }
                        }
                        3 -> {
                            playerInfo.get().addOnSuccessListener { document ->


                                val playerIcon = document.getString("Icon")
                                binding.P4name.text = document.getString("Display Name")
                                binding.P4icon.setImageResource(
                                    resources.getIdentifier(
                                        playerIcon,
                                        "drawable",
                                        packageName
                                    )
                                )
                                roomSettings.update("Current Players", 4)


                            }
                        }
                        4 -> {
                            playerInfo.get().addOnSuccessListener { document ->


                                val playerIcon = document.getString("Icon")
                                binding.P5name.text = document.getString("Display Name")
                                binding.P5icon.setImageResource(
                                    resources.getIdentifier(
                                        playerIcon,
                                        "drawable",
                                        packageName
                                    )
                                )
                                roomSettings.update("Current Players", 5)


                            }
                        }
                        5 -> {
                            playerInfo.get().addOnSuccessListener { document ->


                                val playerIcon = document.getString("Icon")
                                binding.P3name.text = document.getString("Display Name")
                                binding.P3icon.setImageResource(
                                    resources.getIdentifier(
                                        playerIcon,
                                        "drawable",
                                        packageName
                                    )
                                )
                                roomSettings.update("Current Players", 6)


                            }
                        }
                    }
                }
            }
        }

    }
}
