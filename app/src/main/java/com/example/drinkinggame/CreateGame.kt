package com.example.drinkinggame


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.drinkinggame.databinding.ActivityCreateGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var timer = 0
var maxPlayer = 0
var roomCode = ""
var host = ""

class CreateGame : AppCompatActivity() {
    private lateinit var binding: ActivityCreateGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var isValid = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        auth = FirebaseAuth.getInstance()
        val qSetNamesref = db.collection("Account Data").document(auth.currentUser?.uid.toString()).collection(
            "Question Set Name Edit"
        ).document("Names").addSnapshotListener{ snapshot, e ->
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
        binding.create.setOnClickListener {
            validSettings()
            ifRoomExist()
            createGameSettings()
        }

    }

    private fun createGameSettings(){
        auth = FirebaseAuth.getInstance()
        timer = binding.timer.text.toString().toInt()
        maxPlayer = binding.players.text.toString().toInt()
        roomCode = binding.roomcode.text.toString()
        host = auth.currentUser?.uid.toString()
        val data = hashMapOf(
            "Host" to host,
            "Player 1" to host,
            "PLayer 2" to "",
            "PLayer 3" to "",
            "PLayer 4" to "",
            "PLayer 5" to "",
            "PLayer 6" to "",
        )
        db.collection("Games Created").document(roomCode).set(data)
        val intent = Intent(this, ActiveGame::class.java)
        startActivity(intent)

    }
    private fun validSettings(){
        timer = binding.timer.text.toString().toInt()
        maxPlayer = binding.players.text.toString().toInt()

            if( timer < 10){
                binding.cErrorInfo2.visibility = View.VISIBLE
            }else if (maxPlayer > 6 || maxPlayer < 2 ){
                binding.cErrorInfo.visibility = View.VISIBLE
            }else if(binding.roomcode.text.isEmpty()){
                Toast.makeText(applicationContext, "Please enter a room code", Toast.LENGTH_SHORT).show()
            }
            binding.cErrorInfo.visibility = View.INVISIBLE
            binding.cErrorInfo2.visibility = View.INVISIBLE


    }
    private fun ifRoomExist() {
        roomCode = binding.roomcode.text.toString()

            val gameCode = db.collection("Games Created").document(roomCode)
            gameCode.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        Log.d("TAG", "Document already exists.")
                        Toast.makeText(applicationContext, "Room Code Taken", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Log.d("TAG", "Document doesn't exist.")

                    }
                } else {
                    Log.d("TAG", "Error: ", task.exception)
                }
            }



    }
}