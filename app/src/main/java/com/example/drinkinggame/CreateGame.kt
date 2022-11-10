package com.example.drinkinggame


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.drinkinggame.databinding.ActivityCreateGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.max

var timer = 20
var maxPlayer = 2
var roomCode = ""
var host = ""

class CreateGame : AppCompatActivity() {
    private lateinit var binding: ActivityCreateGameBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var settingValid = false
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

        qSetNamesref.document("Names").addSnapshotListener{ snapshot, e ->
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
        binding.create.setOnClickListener {
            validSettings()
        }

    }
    private fun doesRoomCodeExists(){
        val checkRoom = db.collection("Rooms").document(roomCode)
        checkRoom.addSnapshotListener(MetadataChanges.INCLUDE){ snapshot, e ->
            if (e != null) {
                Log.w("Main", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists() && roomCode == checkRoom.toString())
            {
                Log.d("Main", "Current data: ${snapshot?.data}")
                Toast.makeText(applicationContext, "Room exists,Try Again!", Toast.LENGTH_SHORT).show()

            } else {
                Log.d("Main", "Current data: null")
                val makeRoom = db.collection("Rooms").document(roomCode)
                val roomSettings = hashMapOf(
                    "Host" to host,
                    "Number of Players" to maxPlayer,
                    "Timer" to timer
                )
                makeRoom.set(roomSettings)
                val intent = Intent(this, ActiveGame::class.java)
                startActivity(intent)
            }
        }
    }
    private fun validSettings(){
        timer = binding.timer.text.toString().toInt()
        maxPlayer = binding.players.text.toString().toInt()
        var maxPlayerValid = false
        var timerValid = false
        var roomcodeValid = false
        if (maxPlayer < 2 || maxPlayer > 6 || binding.players.text.toString().isEmpty()){
            binding.playerError.visibility = View.VISIBLE
        }else{
            binding.playerError.visibility = View.INVISIBLE
            maxPlayer = binding.players.text.toString().toInt()
            maxPlayerValid = true
        }

        if(timer < 10 || timer > 99 || binding.timer.text.toString().isEmpty()){
            binding.timerError.visibility = View.VISIBLE
        }else{
            binding.timerError.visibility = View.INVISIBLE
            timer = binding.timer.text.toString().toInt()
            timerValid = true
        }
        if (binding.roomcode.text.toString().isEmpty()){
            binding.roomcodeError.visibility = View.VISIBLE
        }else{
            binding.roomcodeError.visibility = View.INVISIBLE
            roomCode = binding.roomcode.text.toString()
            roomcodeValid = true
        }
        if (maxPlayerValid && timerValid && roomcodeValid){
            doesRoomCodeExists()
        }

    }

}