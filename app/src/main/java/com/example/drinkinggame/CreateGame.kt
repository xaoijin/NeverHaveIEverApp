package com.example.drinkinggame


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkinggame.databinding.ActivityCreateGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var timer = 20
var maxPlayer = 2
var hostRoomCode = ""
var host = ""

class CreateGame : AppCompatActivity() {
    private lateinit var binding: ActivityCreateGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        auth = FirebaseAuth.getInstance()
        host = auth.currentUser?.uid.toString()
        val qSetNamesref = db.collection("Account Data")
            .document(auth.currentUser?.uid.toString())
            .collection("Question Set Name Edit")

        qSetNamesref.document("Names").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Main", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("Main", "Current data: ${snapshot.data}")
                when (questionsetselected) {
                    1 -> {
                        binding.setSelected.text = snapshot.getString("QS1Name").toString()
                    }
                    2 -> {
                        binding.setSelected.text = snapshot.getString("QS2Name").toString()
                    }
                    3 -> {
                        binding.setSelected.text = snapshot.getString("QS3Name").toString()
                    }
                }

            } else {
                Log.d("Main", "Current data: null")
            }
        }
        binding.playerError.visibility = View.INVISIBLE
        binding.timerError.visibility = View.INVISIBLE
        binding.roomcodeError.visibility = View.INVISIBLE
        binding.roomcodeError2.visibility = View.INVISIBLE
        binding.create.setOnClickListener {
            validSettings()
        }

    }

    private fun doesRoomCodeExists() {
        val checkRoom = db.collection("Rooms").document()
        checkRoom.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            if (e != null) {
                Log.w("Main", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.exists()) {
                Log.d("Main", "making room")
                val makeRoom =
                    db.collection("Rooms").document(binding.roomcodeInput.text.toString())
                val playersInRoom = hashMapOf(
                    "Player 1" to "",
                    "Player 2" to "",
                    "Player 3" to "",
                    "Player 4" to "",
                    "Player 5" to "",
                    "Player 6" to "",

                    )
                val questionSetInUse = hashMapOf(
                    "Q1" to "",
                    "Q2" to "",
                    "Q3" to "",
                    "Q4" to "",
                    "Q5" to "",
                    "Q6" to "",
                    "Q7" to "",
                    "Q8" to "",
                    "Q9" to "",
                    "Q10" to "",
                    "Q11" to "",
                    "Q12" to "",
                    "Q13" to "",
                    "Q14" to "",
                    "Q15" to "",
                    "Q16" to "",
                    "Q17" to "",
                    "Q18" to "",
                    "Q19" to "",
                    "Q20" to ""
                )
                val roomSettings = hashMapOf(
                    "Host" to host,
                    "Max Players" to maxPlayer,
                    "Timer" to timer,
                    "Current Players" to 1,
                    "Room Status" to "Pause"
                )
                makeRoom.set(roomSettings)
                makeRoom.collection("Players").document("Player UIDs").set(playersInRoom)
                makeRoom.collection("Questions").document("Questions to be Used")
                    .set(questionSetInUse)
                binding.roomcodeError2.visibility = View.INVISIBLE
                isHost = true
                hostRoomCode = binding.roomcodeInput.text.toString()
                val intent = Intent(this, ActiveGame::class.java)
                startActivity(intent)
            } else if (snapshot != null && snapshot.exists()) {
                binding.roomcodeError2.visibility = View.VISIBLE
            }
        }
    }

    private fun validSettings() {
        timer = binding.timer.text.toString().toInt()
        maxPlayer = binding.players.text.toString().toInt()
        var maxPlayerValid = false
        var timerValid = false
        var roomcodeValid = false
        if (maxPlayer < 2 || maxPlayer > 6 || binding.players.text.toString().isEmpty()) {
            binding.playerError.visibility = View.VISIBLE
        } else {
            binding.playerError.visibility = View.INVISIBLE
            maxPlayer = binding.players.text.toString().toInt()
            maxPlayerValid = true
        }

        if (timer < 10 || timer > 99 || binding.timer.text.toString().isEmpty()) {
            binding.timerError.visibility = View.VISIBLE
        } else {
            binding.timerError.visibility = View.INVISIBLE
            timer = binding.timer.text.toString().toInt()
            timerValid = true
        }
        if (binding.roomcodeInput.text.toString().isEmpty()) {
            binding.roomcodeError.visibility = View.VISIBLE
        } else {
            binding.roomcodeError.visibility = View.INVISIBLE
            hostRoomCode = binding.roomcodeInput.text.toString()
            roomcodeValid = true
        }
        if (maxPlayerValid && timerValid && roomcodeValid) {
            doesRoomCodeExists()
        } else {
            binding.roomcodeError2.visibility = View.INVISIBLE
        }

    }


}
