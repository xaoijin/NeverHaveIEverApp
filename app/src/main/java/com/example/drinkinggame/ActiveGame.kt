package com.example.drinkinggame

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkinggame.databinding.ActivityActiveGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Timer

var JoinRoomCode = ""
var isHost = false

class ActiveGame : AppCompatActivity() {
    private lateinit var binding: ActivityActiveGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var playerName = ""
    private var playerIcon = ""
    private var currentRoom = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        auth = FirebaseAuth.getInstance()
        val playerInfo = db.collection("Account Data").document(auth.currentUser!!.uid)
        playerInfo.get().addOnSuccessListener { document ->
            playerName = document.getString("Display Name").toString()
            playerIcon = document.getString("Icon").toString()
        }
        invisible()

        if (!isHost) {
            playerJoin()
            updateUIPlayer()
        } else {
            hostJoin()
            updateUIPlayer()
        }

        binding.startBtn.setOnClickListener {
            timerStart()
        }
        binding.endBtn.setOnClickListener {
            deleteRoom()
        }

    }

    private fun updateUIPlayer() {
        val playerInfo = db.collection("Account Data").document(auth.currentUser!!.uid)
        playerInfo.get().addOnSuccessListener { document ->
            playerName = document.getString("Display Name").toString()
            playerIcon = document.getString("Icon").toString()
        }
        val playersInRoom = db.collection("Rooms").document(currentRoom).collection("Players").document("PlayersData")
        playersInRoom.addSnapshotListener { snapshot, error ->
            if (snapshot != null) {
                val p1name = snapshot.getString("Player 1")
                val p2name = snapshot.getString("Player 2")
                val p3name = snapshot.getString("Player 3")
                val p4name = snapshot.getString("Player 4")
                val p5name = snapshot.getString("Player 5")
                val p6name = snapshot.getString("Player 6")
                val p1icon = snapshot.getString("Player 1 Icon")
                val p2icon = snapshot.getString("Player 2 Icon")
                val p3icon = snapshot.getString("Player 3 Icon")
                val p4icon = snapshot.getString("Player 4 Icon")
                val p5icon = snapshot.getString("Player 5 Icon")
                val p6icon = snapshot.getString("Player 6 Icon")
                binding.P1name.text = p1name
                binding.P2name.text = p2name
                binding.P3name.text = p3name
                binding.P4name.text = p4name
                binding.P5name.text = p5name
                binding.P6name.text = p6name
                binding.P1icon.setImageResource(
                    resources.getIdentifier(
                        p1icon,
                        "drawable",
                        packageName
                    )
                )
                binding.P2icon.setImageResource(
                    resources.getIdentifier(
                        p2icon,
                        "drawable",
                        packageName
                    )
                )
                binding.P3icon.setImageResource(
                    resources.getIdentifier(
                        p3icon,
                        "drawable",
                        packageName
                    )
                )
                binding.P4icon.setImageResource(
                    resources.getIdentifier(
                        p4icon,
                        "drawable",
                        packageName
                    )
                )
                binding.P5icon.setImageResource(
                    resources.getIdentifier(
                        p5icon,
                        "drawable",
                        packageName
                    )
                )
                binding.P6icon.setImageResource(
                    resources.getIdentifier(
                        p6icon,
                        "drawable",
                        packageName
                    )
                )
                if (p6name == "" ){
                    visible(5)
                }else if(p5name === ""){
                    visible(4)
                }else if (p4name == ""){
                    visible(3)
                }else if (p3name == ""){
                    visible(2)
                }
                val initQuestion = db.collection("Rooms").document(currentRoom).collection("Questions").document("Questions to be Used")
                initQuestion.get().addOnSuccessListener { document ->
                    binding.Question.text = document.getString("Q1")
                }
                if (isHost || p1name == playerName){
                    binding.startBtn.visibility = View.VISIBLE
                    binding.endBtn.visibility = View.VISIBLE
                    isHost = true
                }
                Log.d("Main", "updateUIPlayer: making buttons visible to host")
            }
        }
        val timerSet = db.collection("Rooms").document(currentRoom)
        timerSet.addSnapshotListener { snapshot, error ->
            if (snapshot != null ){
                binding.timer.text = snapshot.get("Timer").toString()
            }
        }
        Log.d("Main", "updateUIPlayer: done running")

    }
    private fun timerStart(){

        if (isHost) {
            val timerSetting = db.collection("Rooms").document(currentRoom)
            timerSetting.addSnapshotListener { snapshot, error ->
                var timerSet = snapshot?.get("Timer").toString().toInt()
                object : CountDownTimer(timerSet.toLong()*1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.timer.text = timerSet.toString()
                        timerSet--
                        timerSetting.update("Timer Status", "Play")
                    }
                    override fun onFinish() {
                        gameLogic()
                        timerStart()
                    }
                }.start()
            }
        }
    }
    private fun deleteRoom(){

    }
    private fun gameLogic() {

    }
    private fun invisible(){
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
        Log.d("Main", "visible: is working")
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
    private fun hostJoin() {
        
            val addHostToRoom = db.collection("Rooms").document(hostRoomCode).collection("Players")
                .document("PlayersData")
            binding.roomcode.append(hostRoomCode)
            currentRoom = hostRoomCode
            addHostToRoom.get().addOnSuccessListener { document ->

                binding.P1name.text = document.getString("Player 1")
                val p1Icon = document.getString("Player 1 Icon")
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
    private fun playerJoin(){
        val playerJoin = db.collection("Rooms").document(JoinRoomCode).collection("Players").document("PlayersData")
        binding.roomcode.append(JoinRoomCode)
        currentRoom = JoinRoomCode
        playerJoin.get().addOnSuccessListener { document->
            val p1name = document.getString("Player 1")
            val p2name = document.getString("Player 2")
            val p3name = document.getString("Player 3")
            val p4name = document.getString("Player 4")
            val p5name = document.getString("Player 5")
            val p6name = document.getString("Player 6")

            if (p1name == "" || p1name == playerName ){
                binding.P1name.text = playerName
                binding.P1icon.setImageResource(
                    resources.getIdentifier(
                        playerIcon,
                        "drawable",
                        packageName
                    )
                )
                visible(1)
                val playerData = hashMapOf(
                    "Player 1" to playerName,
                    "Player 1 Icon" to playerIcon
                )
                playerJoin.update(playerData as Map<String, String>)
            }else if (p2name == "" || p2name == playerName){
                binding.P2name.text = playerName
                binding.P2icon.setImageResource(
                    resources.getIdentifier(
                        playerIcon,
                        "drawable",
                        packageName
                    )
                )
                visible(2)
                val playerData = hashMapOf(
                    "Player 2" to playerName,
                    "Player 2 Icon" to playerIcon
                )
                playerJoin.update(playerData as Map<String, String>)
            }else if (p3name == "" || p3name == playerName){
                binding.P3name.text = playerName
                binding.P3icon.setImageResource(
                    resources.getIdentifier(
                        playerIcon,
                        "drawable",
                        packageName
                    )
                )
                visible(3)
                val playerData = hashMapOf(
                    "Player 3" to playerName,
                    "Player 3 Icon" to playerIcon
                )
                playerJoin.update(playerData as Map<String, String>)
            }else if (p4name == "" || p4name == playerName){
                binding.P4name.text = playerName
                binding.P4icon.setImageResource(
                    resources.getIdentifier(
                        playerIcon,
                        "drawable",
                        packageName
                    )
                )
                visible(4)
                val playerData = hashMapOf(
                    "Player 4" to playerName,
                    "Player 4 Icon" to playerIcon
                )
                playerJoin.update(playerData as Map<String, String>)
            }else if (p5name == "" || p5name == playerName){
                binding.P5name.text = playerName
                binding.P5icon.setImageResource(
                    resources.getIdentifier(
                        playerIcon,
                        "drawable",
                        packageName
                    )
                )
                visible(5)
                val playerData = hashMapOf(
                    "Player 5" to playerName,
                    "Player 5 Icon" to playerIcon
                )
                playerJoin.update(playerData as Map<String, String>)
            }else if (p6name == "" || p6name == playerName){
                binding.P6name.text = playerName
                binding.P6icon.setImageResource(
                    resources.getIdentifier(
                        playerIcon,
                        "drawable",
                        packageName
                    )
                )
                visible(6)
                val playerData = hashMapOf(
                    "Player 2" to playerName,
                    "Player 2 Icon" to playerIcon
                )
                playerJoin.update(playerData as Map<String, String>)
            }
        }
    }
}
