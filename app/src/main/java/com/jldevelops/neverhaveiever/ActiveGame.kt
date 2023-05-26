package com.jldevelops.neverhaveiever

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
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
    private var playerPosition = ""
    private val database = FirebaseDatabase.getInstance()
    private val roomRef = database.getReference("Rooms").child(currentRoom)
    private var iHaveCounter = 0
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isTablet = resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        if (isTablet) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        invisible()
        //Sets Room Code Text to be displayed
        binding.roomcode.text = buildString {
            append("Room Code: ")
            append(roomRef.key)
        }
        //Different start functions for Host/Players
        if (isHost) {
            startGameAsHost()

        } else {
            binding.startBtn.visibility = View.GONE
            binding.endBtn.visibility = View.GONE
            startGameAsPlayer()
        }
        //Host Controls starting the game
        binding.startBtn.setOnClickListener {
            binding.startBtn.visibility = View.GONE
            gameStart()
            val gameStatusRef = database.getReference("Rooms/$currentRoom/Game Status")
            gameStatusRef.setValue("Game Started")
                .addOnSuccessListener {
                    // Success
                }
                .addOnFailureListener { error ->
                    // Failed to set the game status
                    Log.d("Main", "Failed to set game status: $error")
                }
        }
        //Host ends the game and closes room

        binding.endBtn.setOnClickListener {
            deleteRoom()
        }
        //keeps reading from the realtime database for any players that join
        //and will update the screen accordingly
        startRoomListener()

        binding.iHaveBtn.setOnClickListener {
            iHaveCounter++
            iHave()
        }
        binding.haveNotBtn.setOnClickListener {
            haveNot()
        }
        binding.chatBtn.setOnClickListener {
            chatBox()
        }
    }
    private fun chatBox(){
        val chatDialogFragment = ChatDialogFragment()
        chatDialogFragment.show(supportFragmentManager, "ChatDialog")
    }
    private fun iHave() {
        binding.iHaveBtn.visibility = View.INVISIBLE
        binding.haveNotBtn.visibility = View.INVISIBLE
        val playerRef =
            database.getReference("Rooms/$currentRoom/players/$playerPosition/player choice")
        playerRef.setValue("Have")
            .addOnSuccessListener {
                when (playerPosition) {
                    "player1" -> {
                        binding.P1icon.setBackgroundResource(R.drawable.ihaveimageborder)
                    }

                    "player2" -> {
                        binding.P2icon.setBackgroundResource(R.drawable.ihaveimageborder)
                    }

                    "player3" -> {
                        binding.P3icon.setBackgroundResource(R.drawable.ihaveimageborder)
                    }

                    "player4" -> {
                        binding.P4icon.setBackgroundResource(R.drawable.ihaveimageborder)
                    }

                    "player5" -> {
                        binding.P5icon.setBackgroundResource(R.drawable.ihaveimageborder)
                    }

                    "player6" -> {
                        binding.P6icon.setBackgroundResource(R.drawable.ihaveimageborder)
                    }
                }
            }
            .addOnFailureListener { error ->
                // Failed to set the IHave value
                Log.d("Main", "Failed to set IHave value: $error")
            }
    }

    private fun haveNot() {
        binding.iHaveBtn.visibility = View.INVISIBLE
        binding.haveNotBtn.visibility = View.INVISIBLE
        val playerRef =
            database.getReference("Rooms/$currentRoom/players/$playerPosition/player choice")
        playerRef.setValue("Have Not")
            .addOnSuccessListener {
                when (playerPosition) {
                    "player1" -> {
                        binding.P1icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                    }

                    "player2" -> {
                        binding.P2icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                    }

                    "player3" -> {
                        binding.P3icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                    }

                    "player4" -> {
                        binding.P4icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                    }

                    "player5" -> {
                        binding.P5icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                    }

                    "player6" -> {
                        binding.P6icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                    }
                }
            }
            .addOnFailureListener { error ->
                // Failed to set the IHave value
                Log.d("Main", "Failed to set IHave value: $error")
            }
    }

    private fun resetPlayerChoice() {
        binding.iHaveBtn.visibility = View.VISIBLE
        binding.haveNotBtn.visibility = View.VISIBLE
        val playerRef =
            database.getReference("Rooms/$currentRoom/players/$playerPosition/player choice")
        playerRef.setValue("Undecided")
            .addOnSuccessListener {
                when (playerPosition) {
                    "player1" -> {
                        binding.P1icon.setBackgroundResource(R.drawable.choosingimgborder)
                    }

                    "player2" -> {
                        binding.P2icon.setBackgroundResource(R.drawable.choosingimgborder)
                    }

                    "player3" -> {
                        binding.P3icon.setBackgroundResource(R.drawable.choosingimgborder)
                    }

                    "player4" -> {
                        binding.P4icon.setBackgroundResource(R.drawable.choosingimgborder)
                    }

                    "player5" -> {
                        binding.P5icon.setBackgroundResource(R.drawable.choosingimgborder)
                    }

                    "player6" -> {
                        binding.P6icon.setBackgroundResource(R.drawable.choosingimgborder)
                    }
                }
            }
            .addOnFailureListener { error ->
                // Failed to set the IHave value
                Log.d("Main", "Failed to set IHave value: $error")
            }
    }

    private fun deleteRoom() {
        roomRef.child("Game Status").setValue("Game Ended")
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error updating game status", exception)
                Toast.makeText(this, "Failed to update game status.", Toast.LENGTH_LONG).show()
            }
    }
    private fun startRoomListener() {
        roomRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val roomData = snapshot.value as? Map<*, *>
                    val playersData = roomData?.get("players") as? Map<*, *>
                    playersData?.let { players ->
                        for ((playerPosition, playerData) in players) {
                            playerName =
                                ((playerData as? Map<*, *>)?.get("name") as? String).toString()
                            playerIcon =
                                (((playerData as? Map<*, *>)?.get("icon") as? Long) ?: 0).toInt()

                            // Update the UI with the player's name and icon based on the player position
                            when (playerPosition) {
                                "player1" -> {
                                    binding.P1name.text = playerName
                                    binding.P1icon.setImageResource(playerIcon)
                                    when (((playerData as? Map<*, *>)?.get("player choice") as? String)) {
                                        "Have" -> {
                                            binding.P1icon.setBackgroundResource(R.drawable.ihaveimageborder)

                                        }

                                        "Have Not" -> {
                                            binding.P1icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                                        }

                                        else -> {
                                            binding.P1icon.setBackgroundResource(R.drawable.choosingimgborder)
                                        }
                                    }
                                }

                                "player2" -> {
                                    binding.P2name.text = playerName
                                    if (playerIcon == 0) {
                                        binding.P2icon.setImageResource(R.drawable.missingperson)
                                    } else {
                                        binding.P2icon.setImageResource(playerIcon)
                                    }
                                    when (((playerData as? Map<*, *>)?.get("player choice") as? String)) {
                                        "Have" -> {
                                            binding.P2icon.setBackgroundResource(R.drawable.ihaveimageborder)
                                        }

                                        "Have Not" -> {
                                            binding.P2icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                                        }

                                        else -> {
                                            binding.P2icon.setBackgroundResource(R.drawable.choosingimgborder)
                                        }
                                    }
                                }

                                "player3" -> {
                                    binding.P3name.text = playerName
                                    if (playerIcon == 0) {
                                        binding.P3icon.setImageResource(R.drawable.missingperson)
                                    } else {
                                        binding.P3icon.setImageResource(playerIcon)
                                    }
                                    when (((playerData as? Map<*, *>)?.get("player choice") as? String)) {
                                        "Have" -> {
                                            binding.P3icon.setBackgroundResource(R.drawable.ihaveimageborder)
                                        }

                                        "Have Not" -> {
                                            binding.P3icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                                        }

                                        else -> {
                                            binding.P3icon.setBackgroundResource(R.drawable.choosingimgborder)
                                        }
                                    }
                                }

                                "player4" -> {
                                    binding.P4name.text = playerName
                                    if (playerIcon == 0) {
                                        binding.P4icon.setImageResource(R.drawable.missingperson)
                                    } else {
                                        binding.P4icon.setImageResource(playerIcon)
                                    }
                                    when (((playerData as? Map<*, *>)?.get("player choice") as? String)) {
                                        "Have" -> {
                                            binding.P4icon.setBackgroundResource(R.drawable.ihaveimageborder)
                                        }

                                        "Have Not" -> {
                                            binding.P4icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                                        }

                                        else -> {
                                            binding.P4icon.setBackgroundResource(R.drawable.choosingimgborder)
                                        }
                                    }
                                }

                                "player5" -> {
                                    binding.P5name.text = playerName
                                    if (playerIcon == 0) {
                                        binding.P5icon.setImageResource(R.drawable.missingperson)
                                    } else {
                                        binding.P5icon.setImageResource(playerIcon)
                                    }
                                    when (((playerData as? Map<*, *>)?.get("player choice") as? String)) {
                                        "Have" -> {
                                            binding.P5icon.setBackgroundResource(R.drawable.ihaveimageborder)
                                        }

                                        "Have Not" -> {
                                            binding.P5icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                                        }

                                        else -> {
                                            binding.P5icon.setBackgroundResource(R.drawable.choosingimgborder)
                                        }
                                    }
                                }

                                "player6" -> {
                                    binding.P6name.text = playerName
                                    if (playerIcon == 0) {
                                        binding.P6icon.setImageResource(R.drawable.missingperson)
                                    } else {
                                        binding.P6icon.setImageResource(playerIcon)
                                    }
                                    when (((playerData as? Map<*, *>)?.get("player choice") as? String)) {
                                        "Have" -> {
                                            binding.P6icon.setBackgroundResource(R.drawable.ihaveimageborder)
                                        }

                                        "Have Not" -> {
                                            binding.P6icon.setBackgroundResource(R.drawable.ihavenotimageborder)
                                        }

                                        else -> {
                                            binding.P6icon.setBackgroundResource(R.drawable.choosingimgborder)
                                        }
                                    }
                                }
                                // Add other player cases here
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Main", "Start Room Listener Function Failed")
            }
        })
        roomRef.child("Max Players").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val maxPlayers = dataSnapshot.getValue(Int::class.java)
                maxPlayers?.let {
                    visible(it)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Main", "Max Players Listener Failed")
            }
        })
        //starts the game for other players
        if(!isHost) {
            roomRef.child("Game Status").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val gameStatus = dataSnapshot.getValue(String::class.java)
                    if (gameStatus == "Game Started") {
                        gameStart()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Main", "Game Status Listener Failed")
                }
            })
        }
        //ends game for all players
        roomRef.child("Game Status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gameStatus = dataSnapshot.getValue(String::class.java)
                if(gameStatus == "Game Ended"){
                    roomRef.setValue(null)
                        .addOnSuccessListener {
                            timer?.cancel()
                            Log.d("Firebase", "Room successfully deleted.")
                            Toast.makeText(applicationContext, "Room has been closed by host.", Toast.LENGTH_LONG).show()
                            // Redirect to a different activity or handle the UI change here
                            val backHome = Intent(applicationContext, InitialScreen::class.java)
                            backHome.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                            startActivity(backHome)
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firebase", "Error deleting room", exception)
                            Toast.makeText(applicationContext, "Failed to close room.", Toast.LENGTH_LONG).show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Main", "Game Status Listener Failed")
            }
        })
    }

    private fun startGameAsHost() {
        auth = FirebaseAuth.getInstance()
        val playerInfoRef = db.collection("Account Data").document(auth.uid.toString())
        val playerRef = database.getReference("Rooms/$currentRoom/players/player1")
        playerInfoRef.get().addOnSuccessListener { document ->
            if (document != null) {
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
        playerPosition = "player1"

    }

    private fun startGameAsPlayer() {
        when (playerNumber) {
            2 -> {
                binding.P2name.text = playerName
                binding.P2icon.setImageResource(playerIcon)
                playerPosition = "player2"
            }

            3 -> {
                binding.P3name.text = playerName
                binding.P3icon.setImageResource(playerIcon)
                playerPosition = "player3"
            }

            4 -> {
                binding.P4name.text = playerName
                binding.P4icon.setImageResource(playerIcon)
                playerPosition = "player4"
            }

            5 -> {
                binding.P5name.text = playerName
                binding.P5icon.setImageResource(playerIcon)
                playerPosition = "player5"
            }

            6 -> {
                binding.P6name.text = playerName
                binding.P6icon.setImageResource(playerIcon)
                playerPosition = "player6"
            }

        }
        visible(playerNumber)
    }

    private fun gameStart() {
        changeQuestion()
        startTimer()
    }

    private fun changeQuestion() {
        // Get the current question index from Firebase Database
        roomRef.child("Current Question")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Rest of your code
                        // Get the current question index
                        var currentQuestionIndex = (dataSnapshot.value as Long).toInt() + 1

                        // Get the next question from Firebase Database
                        roomRef.child("questions")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val questionKey = "question$currentQuestionIndex"
                                    val questionData =
                                        dataSnapshot.child(questionKey).value as? Map<*, *>
                                    val questionText =
                                        questionData?.get("text") as? String ?: "No question found"
                                    binding.Question.text = questionText

                                    // Increment the question index
                                    currentQuestionIndex++
                                    // If there are no more questions, reset the index to 1
                                    if (currentQuestionIndex > dataSnapshot.childrenCount ) {
                                        binding.Question.text = buildString {
                                            append("No More Questions!")
                                        }
                                    }

                                    // Update the current question index in Firebase
                                    roomRef.child("Current Question").setValue(currentQuestionIndex)
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.d("Main", "failed to change question!")
                                }
                            })
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Main", "failed to get current question index!")
                }
            })
    }

    private fun startTimer() {
        val timerRef = FirebaseDatabase.getInstance().getReference("Rooms/$currentRoom/Timer")

        // Fetch the timer value from Firebase
        timerRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timerValue = snapshot.getValue(Long::class.java) // Assuming the timer value is stored as a Long
                if (timerValue != null) {
                    // Initialize and start the timer with the fetched value
                    timer = object : CountDownTimer(timerValue * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            // Update timer display on UI
                            val seconds = (millisUntilFinished / 1000).toInt()
                            binding.timer.text = buildString {
                                append("Time left: ")
                                append(seconds)
                                append(" seconds")
                            }
                        }

                        override fun onFinish() {
                            // Change the question when timer ends
                            changeQuestion()
                            if (binding.Question.text == "No More Questions!") {
                                // Code to end the game goes here
                            } else {
                                // If there are still questions, start the timer again
                                resetPlayerChoice()
                                startTimer()
                            }
                            checkDrunk()
                        }
                    }
                    (timer as CountDownTimer).start()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })
    }



    private fun checkDrunk() {
        val playerRef = database.getReference("Rooms/$currentRoom/players/$playerPosition/IHaveCount")
        playerRef.setValue(iHaveCounter)
            .addOnSuccessListener {
            Log.d("Main", "Successfully added to I Have Counter")
        }
            .addOnFailureListener {
            Log.d("Main", "Failed to add to I Have Counter")
        }
        if (iHaveCounter % 5 == 0){
            showCameraMiniGameDialog()
        }
    }

    private fun showCameraMiniGameDialog() {
        val cameraMiniGameDialogFragment = CameraMiniGameDialogFragment()
        cameraMiniGameDialogFragment.show(supportFragmentManager, "cameraMiniGameDialog")
    }

    private fun visible(x: Int) {
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


}
