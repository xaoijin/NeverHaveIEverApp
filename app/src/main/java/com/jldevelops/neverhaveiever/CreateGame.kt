package com.jldevelops.neverhaveiever


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jldevelops.neverhaveiever.databinding.ActivityCreateGameBinding

var timer = 20
var maxPlayer = 2
var host = ""

class CreateGame : AppCompatActivity() {
    private lateinit var hostQuestions: DocumentReference
    private lateinit var binding: ActivityCreateGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var playerIcon = 0
    private var playerName = ""
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
        val playerInfo = db.collection("Account Data").document(auth.currentUser!!.uid)
        playerInfo.get().addOnSuccessListener { document ->
            playerName = document.getString("Display Name").toString()
            playerIcon = document.getLong("Icon")?.toInt() ?: 0
        }
        binding.playerError.visibility = View.INVISIBLE
        binding.timerError.visibility = View.INVISIBLE
        binding.roomcodeError.visibility = View.INVISIBLE
        binding.roomcodeError2.visibility = View.INVISIBLE
        binding.create.setOnClickListener {
            validSettings()
        }

    }

    private fun createRoom() {
        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val roomRef = database.getReference("Rooms")
        hostQuestions = when (questionsetselected) {
            3 -> {
                db.collection("Account Data").document(auth.currentUser!!.uid)
                    .collection("Question Sets").document("Set3")
            }

            2 -> {
                db.collection("Account Data").document(auth.currentUser!!.uid)
                    .collection("Question Sets").document("Set2")
            }

            else -> {
                db.collection("Account Data").document(auth.currentUser!!.uid)
                    .collection("Question Sets").document("Set1")
            }
        }
        val questionList = mutableListOf<String>()
        val questionData = hashMapOf<String, Any>()
        hostQuestions.get().addOnSuccessListener { document ->
            for (i in 1..20) {
                val questionKey = "Q$i"
                val questionValue = document.getString(questionKey)
                questionValue?.let {
                    questionList.add(it)
                }
            }

            for (i in 0 until questionList.size) {
                val questionKey = "question${i + 1}"
                val questionValue = questionList[i]
                questionData[questionKey] = mapOf("text" to questionValue)
            }
        }

        val playerData = HashMap<String, Any>()
        for (i in 1..maxPlayer){
            val playerPosition = "player$i"
            val playerName = "Waiting"

            val playerInfo = hashMapOf(//placeholders
                "uid" to "blank", // where player uid will be
                "name" to playerName, // where player display name will be
                "player choice" to "Undecided", // checks for player answer
                "icon" to 0, // player icon is int format
                "IHaveCount" to 0,// player answering I have
                "playerJoined" to false // checks for actual player
            )
            playerData[playerPosition] = playerInfo
        }
        val roomSettings = hashMapOf(
            "Host" to host,
            "Max Players" to maxPlayer,
            "Players in Room" to 1,
            "Timer" to timer,
            "chat" to arrayListOf<HashMap<String, String>>(),
            "Game Status" to "Waiting For Players To Join",
            "Current Question" to 0,
            "Player Turn" to host,
            "players" to playerData,
            "questions" to questionData
        )
        binding.roomcodeError2.visibility = View.INVISIBLE
        currentRoom = binding.roomcodeInput.text.toString()

        roomRef.child(currentRoom).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.roomcodeError2.visibility = View.VISIBLE
                }else{
                    isHost = true
                    roomRef.child(currentRoom).setValue(roomSettings)
                    val activeGame = Intent(applicationContext, ActiveGame::class.java)
                    startActivity(activeGame)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to create the room.", Toast.LENGTH_SHORT).show()
                // Navigate to a different screen (e.g., MainActivity)
                val homeScreen = Intent(applicationContext, InitialScreen::class.java)
                startActivity(homeScreen)
                finish()
            }
        })
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
            currentRoom = binding.roomcodeInput.text.toString()
            roomcodeValid = true
        }
        if (maxPlayerValid && timerValid && roomcodeValid) {
            createRoom()
        } else {
            binding.roomcodeError2.visibility = View.INVISIBLE
        }
    }
}
