package com.jldevelops.neverhaveiever

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jldevelops.neverhaveiever.databinding.ActivityActiveGameBinding

var JoinRoomCode = ""
var isHost = false
var currentRoom = ""
var playerNumber = 1

class ActiveGame : AppCompatActivity() {
    private lateinit var binding: ActivityActiveGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var playerName = ""
    private var playerIcon = 0
    private  var questionCounter : Int = 0
    private var isTimerRunning = false
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
            playerIcon = document.getLong("Icon")?.toInt() ?: 0
        }
        invisible()

        if (!isHost) {
            playerJoin()
            updateUIPlayer()
            binding.startBtn.visibility = View.INVISIBLE
            binding.endBtn.visibility = View.INVISIBLE
        } else {
            hostJoin()
            updateUIPlayer()
        }

        binding.startBtn.setOnClickListener {
            val timerSetting = db.collection("Rooms").document(currentRoom)
            timerSetting.update("Game Status", "Started")
            gameLogic()
            binding.startBtn.visibility = View.INVISIBLE
        }
        binding.endBtn.setOnClickListener {
            deleteRoom()
        }
        val checkTimer = db.collection("Rooms").document(currentRoom)
        checkTimer.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                if (snapshot.getString("Game Status") == "Started") {
                    timerStart()
                }
            }
        }
        playerChoice()
        checkDrunk()
        val checkGameEnded =  db.collection("Rooms").document(currentRoom)
        checkGameEnded.addSnapshotListener { snapshot, _ ->
            if (snapshot != null){
                if (snapshot.getString("Game Status") == "Ended"){
                    Toast.makeText(applicationContext, "Host has Closed The Room", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, InitialScreen::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun updateUIPlayer() {
        val playerInfo = db.collection("Account Data").document(auth.currentUser!!.uid)
        playerInfo.get().addOnSuccessListener { document ->
            playerName = document.getString("Display Name").toString()
            playerIcon = document.getLong("Icon")?.toInt() ?: 0
        }
        val playersInRoom = db.collection("Rooms").document(currentRoom).collection("Players")
            .document("PlayersData")
        playersInRoom.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                val p1name = snapshot.getString("Player 1")
                val p2name = snapshot.getString("Player 2")
                val p3name = snapshot.getString("Player 3")
                val p4name = snapshot.getString("Player 4")
                val p5name = snapshot.getString("Player 5")
                val p6name = snapshot.getString("Player 6")
                val p1icon = snapshot.getLong("Player 1 Icon")?.toInt()?: 0
                val p2icon = snapshot.getLong("Player 2 Icon")?.toInt()?: 0
                val p3icon = snapshot.getLong("Player 3 Icon")?.toInt()?: 0
                val p4icon = snapshot.getLong("Player 4 Icon")?.toInt()?: 0
                val p5icon = snapshot.getLong("Player 5 Icon")?.toInt()?: 0
                val p6icon = snapshot.getLong("Player 6 Icon")?.toInt()?: 0
                binding.P1name.text = p1name
                binding.P2name.text = p2name
                binding.P3name.text = p3name
                binding.P4name.text = p4name
                binding.P5name.text = p5name
                binding.P6name.text = p6name
                binding.P1icon.setImageResource(p1icon)
                binding.P2icon.setImageResource(p2icon)
                binding.P3icon.setImageResource(p3icon)
                binding.P4icon.setImageResource(p4icon)
                binding.P5icon.setImageResource(p5icon)
                binding.P6icon.setImageResource(p6icon)
                if (p6name == "") {
                    visible(5)
                } else if (p5name === "") {
                    visible(4)
                } else if (p4name == "") {
                    visible(3)
                } else if (p3name == "") {
                    visible(2)
                }
            }
        }
        val timerSet = db.collection("Rooms").document(currentRoom)
        timerSet.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                binding.timer.text = snapshot.get("Timer").toString()
            }
        }
    }

    private fun timerStart() {
        if (isTimerRunning) {
            return
        }

        val timerSetting = db.collection("Rooms").document(currentRoom)
        timerSetting.get()
            .addOnSuccessListener { snapshot ->
                val currentQuestion = snapshot.getLong("Current Question") ?: 0
                if (currentQuestion >= 20) {
                    // Current question is already at 20, do not start the timer again
                    return@addOnSuccessListener
                }

                val timerSet = snapshot.getLong("Timer") ?: 0
                object : CountDownTimer(timerSet * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.timer.text = (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        binding.iHaveBtn.visibility = View.VISIBLE
                        binding.haveNotBtn.visibility = View.VISIBLE
                        timerSetting.update("Current Question", currentQuestion + 1)
                            .addOnSuccessListener {
                                // Update successful
                                Log.d("Main", "Writing to Database")
                            }
                            .addOnFailureListener {
                                // Handle any errors that occurred during the update
                            }
                        gameLogic()
                        isTimerRunning = false
                    }
                }.start()
                isTimerRunning = true
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, "Failed to fetch timer settings: ${exception.message}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, InitialScreen::class.java)
                startActivity(intent)
            }
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
       val gameEnd =  db.collection("Rooms").document(currentRoom)
        gameEnd.update("Game Status", "Ended")
        val intent = Intent(this, InitialScreen::class.java)
        startActivity(intent)
    }

    private fun playerChoice() {
        var pCounter: Int
        val checkPCounter = db.collection("Rooms").document(currentRoom).collection("Players")
            .document("PlayersData")
        checkPCounter.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                when (playerNumber) {
                    1 -> {
                        binding.iHaveBtn.setOnClickListener {
                            pCounter = snapshot.get("Player 1 Counter").toString().toInt()
                            pCounter++
                            checkPCounter.update("Player 1 Counter", pCounter)
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE


                        }
                        binding.haveNotBtn.setOnClickListener {
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE

                        }
                    }
                    2 -> {
                        binding.iHaveBtn.setOnClickListener {
                            pCounter = snapshot.get("Player 2 Counter").toString().toInt()
                            pCounter++
                            checkPCounter.update("Player 2 Counter", pCounter)
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE


                        }
                        binding.haveNotBtn.setOnClickListener {
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE

                        }
                    }
                    3 -> {
                        binding.iHaveBtn.setOnClickListener {
                            pCounter = snapshot.get("Player 3 Counter").toString().toInt()
                            pCounter++
                            checkPCounter.update("Player 3 Counter", pCounter)
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE


                        }
                        binding.haveNotBtn.setOnClickListener {
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE

                        }
                    }
                    4 -> {
                        binding.iHaveBtn.setOnClickListener {
                            pCounter = snapshot.get("Player 4 Counter").toString().toInt()
                            pCounter++
                            checkPCounter.update("Player 4 Counter", pCounter)
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE


                        }
                        binding.haveNotBtn.setOnClickListener {
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE

                        }
                    }
                    5 -> {
                        binding.iHaveBtn.setOnClickListener {
                            pCounter = snapshot.get("Player 5 Counter").toString().toInt()
                            pCounter++
                            checkPCounter.update("Player 5 Counter", pCounter)
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE


                        }
                        binding.haveNotBtn.setOnClickListener {
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE

                        }
                    }
                    6 -> {
                        binding.iHaveBtn.setOnClickListener {
                            pCounter = snapshot.get("Player 6 Counter").toString().toInt()
                            pCounter++
                            checkPCounter.update("Player 6 Counter", pCounter)
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE


                        }
                        binding.haveNotBtn.setOnClickListener {
                            binding.iHaveBtn.visibility = View.INVISIBLE
                            binding.haveNotBtn.visibility = View.INVISIBLE

                        }
                    }
                }
            }
        }
    }

    private fun gameLogic() {
        //Find Current Question
        db.collection("Rooms").document(currentRoom).get().addOnSuccessListener{
            document ->
            if(document != null){
                questionCounter = document.getLong("Current Question")?.toInt() ?: 0
            }
        }
        //Find Question Text
        val changeQuestions = db.collection("Rooms").document(currentRoom).collection("Questions")
            .document("Questions to be Used")
        //Set Question Text
        changeQuestions.get().addOnSuccessListener { document ->
            when (questionCounter) {
                1 -> {
                    binding.Question.text = document.getString("Q1")
                }
                2 -> {
                    binding.Question.text = document.getString("Q2")
                }
                3 -> {
                    binding.Question.text = document.getString("Q3")
                }
                4 -> {
                    binding.Question.text = document.getString("Q4")
                }
                5 -> {
                    binding.Question.text = document.getString("Q5")
                }
                6 -> {
                    binding.Question.text = document.getString("Q6")
                }
                7 -> {
                    binding.Question.text = document.getString("Q7")
                }
                8 -> {
                    binding.Question.text = document.getString("Q8")
                }
                9 -> {
                    binding.Question.text = document.getString("Q9")
                }
                10 -> {
                    binding.Question.text = document.getString("Q10")
                }
                11 -> {
                    binding.Question.text = document.getString("Q11")
                }
                12 -> {
                    binding.Question.text = document.getString("Q12")
                }
                13 -> {
                    binding.Question.text = document.getString("Q13")
                }
                14 -> {
                    binding.Question.text = document.getString("Q14")
                }
                15 -> {
                    binding.Question.text = document.getString("Q15")
                }
                16 -> {
                    binding.Question.text = document.getString("Q16")
                }
                17 -> {
                    binding.Question.text = document.getString("Q17")
                }
                18 -> {
                    binding.Question.text = document.getString("Q18")
                }
                19 -> {
                    binding.Question.text = document.getString("Q19")
                }
                20 -> {
                    binding.Question.text = document.getString("Q20")
                }
                else -> {
                    binding.Question.text = getString(R.string.no_more_questions)
                }
            }
        }.addOnFailureListener {
            val intent = Intent(this, InitialScreen::class.java)
            startActivity(intent)
        }
        Log.d("Main", "Question Counter at $questionCounter")
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

    private fun hostJoin() {

        val addHostToRoom = db.collection("Rooms").document(currentRoom).collection("Players")
            .document("PlayersData")
        binding.roomcode.append(currentRoom)
        addHostToRoom.get().addOnSuccessListener { document ->

            binding.P1name.text = document.getString("Player 1")
            val p1Icon = document.getLong("Player 1 Icon")?.toInt()?: 0
            binding.P1icon.setImageResource(p1Icon)
            visible(1)

        }
        isHost = true
        playerNumber = 1
    }

    private fun playerJoin() {
        val playerJoin = db.collection("Rooms").document(JoinRoomCode).collection("Players")
            .document("PlayersData")
        binding.roomcode.append(JoinRoomCode)
        currentRoom = JoinRoomCode
        playerJoin.get().addOnSuccessListener { document ->
            val p2name = document.getString("Player 2")
            val p3name = document.getString("Player 3")
            val p4name = document.getString("Player 4")
            val p5name = document.getString("Player 5")
            val p6name = document.getString("Player 6")

            if (p2name == "" || p2name == playerName) {
                binding.P2name.text = playerName
                binding.P2icon.setImageResource(playerIcon)
                visible(2)
                val playerData = hashMapOf(
                    "Player 2" to playerName,
                    "Player 2 Icon" to playerIcon
                )
                playerNumber = 2
                playerJoin.update(playerData as Map<String, Any>)
            } else if (p3name == "" || p3name == playerName) {
                binding.P3name.text = playerName
                binding.P3icon.setImageResource(playerIcon)
                visible(3)
                val playerData = hashMapOf(
                    "Player 3" to playerName,
                    "Player 3 Icon" to playerIcon
                )
                playerNumber = 3
                playerJoin.update(playerData as Map<String, Any>)
            } else if (p4name == "" || p4name == playerName) {
                binding.P4name.text = playerName
                binding.P4icon.setImageResource(playerIcon)
                visible(4)
                val playerData = hashMapOf(
                    "Player 4" to playerName,
                    "Player 4 Icon" to playerIcon
                )
                playerNumber = 4
                playerJoin.update(playerData as Map<String, Any>)
            } else if (p5name == "" || p5name == playerName) {
                binding.P5name.text = playerName
                binding.P5icon.setImageResource(playerIcon)
                visible(5)
                val playerData = hashMapOf(
                    "Player 5" to playerName,
                    "Player 5 Icon" to playerIcon
                )
                playerNumber = 5
                playerJoin.update(playerData as Map<String, Any>)
            } else if (p6name == "" || p6name == playerName) {
                binding.P6name.text = playerName
                binding.P6icon.setImageResource(playerIcon)
                visible(6)
                val playerData = hashMapOf(
                    "Player 6" to playerName,
                    "Player 6 Icon" to playerIcon
                )
                playerNumber = 6
                playerJoin.update(playerData as Map<String, Any>)
            }
        }
    }
}
