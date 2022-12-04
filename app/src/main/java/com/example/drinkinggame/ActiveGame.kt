package com.example.drinkinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private fun updateUI(){

    }
    private fun playersJoin(){

        val playerInfo = db.collection("Account Data").document(auth.currentUser!!.uid)
        val roomSettings = db.collection("Rooms").document(JoinRoomCode)
        roomSettings.addSnapshotListener(MetadataChanges.INCLUDE){ snapshot, e ->
            if (e != null) {
                Log.w("Main", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val playersinroom = snapshot.getString("Current Players")!!.toInt()
                if (isHost){
                    playerInfo.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->

                        if (snapshot != null && snapshot.exists()) {
                            val playerIcon = snapshot.getString("Icon")
                            binding.P1name.text = snapshot.getString("Display Name")
                            binding.P1icon.setImageResource(resources.getIdentifier(playerIcon, "drawable", packageName))
                            roomSettings.update("Current Players", 1)
                        }
                    }


                }else if (!isHost){
                    playerInfo.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->

                        if (snapshot != null && snapshot.exists()) {
                            val playerIcon = snapshot.getString("Icon")
                            binding.P2name.text = snapshot.getString("Display Name")
                            binding.P2icon.setImageResource(resources.getIdentifier(playerIcon, "drawable", packageName))
                            roomSettings.update("Current Players", 2)
                        }

                    }
                     when (playersinroom) {
                        2 -> {
                            playerInfo.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->

                                if (snapshot != null && snapshot.exists()) {
                                    val playerIcon = snapshot.getString("Icon")
                                    binding.P3name.text = snapshot.getString("Display Name")
                                    binding.P3icon.setImageResource(resources.getIdentifier(playerIcon, "drawable", packageName))
                                    roomSettings.update("Current Players", 3)
                                }

                            }
                        }
                        3 -> {
                            playerInfo.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->

                                if (snapshot != null && snapshot.exists()) {
                                    val playerIcon = snapshot.getString("Icon")
                                    binding.P4name.text = snapshot.getString("Display Name")
                                    binding.P4icon.setImageResource(resources.getIdentifier(playerIcon, "drawable", packageName))
                                    roomSettings.update("Current Players", 4)
                                }

                            }
                        }
                        4 -> {
                            playerInfo.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->

                                if (snapshot != null && snapshot.exists()) {
                                    val playerIcon = snapshot.getString("Icon")
                                    binding.P5name.text = snapshot.getString("Display Name")
                                    binding.P5icon.setImageResource(resources.getIdentifier(playerIcon, "drawable", packageName))
                                    roomSettings.update("Current Players", 5)
                                }

                            }
                        }
                        5 -> {
                            playerInfo.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->

                                if (snapshot != null && snapshot.exists()) {
                                    val playerIcon = snapshot.getString("Icon")
                                    binding.P3name.text = snapshot.getString("Display Name")
                                    binding.P3icon.setImageResource(resources.getIdentifier(playerIcon, "drawable", packageName))
                                    roomSettings.update("Current Players", 6)
                                }

                            }
                        }
                    }
                }
            }

        }
    }
}