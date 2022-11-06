package com.example.drinkinggame


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.drinkinggame.databinding.ActivityCreateGameBinding
import com.google.firebase.auth.FirebaseAuth
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
    val isValid = false
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
                if(questionsetselected == 1){
                    binding.setSelected.text = snapshot.getString("QS1Name").toString()
                }else if(questionsetselected == 2){
                    binding.setSelected.text = snapshot.getString("QS2Name").toString()
                }else if(questionsetselected == 3){
                    binding.setSelected.text = snapshot.getString("QS3Name").toString()
                }

            } else {
                Log.d("Main", "Current data: null")
            }
        }
        binding.create.setOnClickListener {
            createGameSettings()
        }

    }
    private fun createGameSettings(){
        timer = binding.timer.text.toString().toInt()
        maxPlayer = binding.players.text.toString().toInt()
        roomCode = binding.roomcode.text.toString()
        host = auth.currentUser?.uid.toString()

        val intent = Intent(this, ActiveGame::class.java)
        startActivity(intent)

    }
    private fun validate(){

    }


}