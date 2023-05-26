package com.jldevelops.neverhaveiever


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


data class Message(
    val text: String = "",
    val userId: String? = null // or any other user identifying information you want
)

class ChatDialogFragment : DialogFragment() {
    private lateinit var chatLayout: LinearLayout
    private lateinit var chatInputEditText: EditText
    private lateinit var dialog: AlertDialog
    private lateinit var scrollView: ScrollView
    private lateinit var fragmentContext: Context
    private val db = FirebaseFirestore.getInstance()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fragment_chat_dialog, null)
        chatLayout = dialogView.findViewById(R.id.chat_layout)
        chatInputEditText = dialogView.findViewById(R.id.chat_input)
        scrollView = dialogView.findViewById(R.id.scrollView)
        val chatRef = FirebaseDatabase.getInstance().getReference("Rooms/$currentRoom/chat")

        // Add a listener to get the chat messages
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatLayout.removeAllViews() // Clear the layout first
                for (dataSnapshot in snapshot.children) {
                    val message = dataSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        addMessageToLayout(message)
                    }
                }
                scrollToBottom()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })

        builder.setView(dialogView)
            .setPositiveButton("Send", null) // set null here
            .setNegativeButton("Close") { _, _ ->
                dialog.cancel()
            }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(fragmentContext, R.color.LightSalmon)))

        return dialog
    }

    private fun scrollToBottom() {
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    override fun onStart() {
        super.onStart()

        // Overriding the default behavior of positive button
        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
            val userInput = chatInputEditText.text.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"

            if (userInput.isNotEmpty()) {
                // Create a new message
                val newMessage = Message(text = userInput, userId = userId)

                // Get a reference to the chat in the database
                val chatRef = FirebaseDatabase.getInstance().getReference("Rooms/$currentRoom/chat")

                // Push the new message to the database
                chatRef.push().setValue(newMessage)

                // Clear the input field
                chatInputEditText.text.clear()
            }
        }
    }

    private fun addMessageToLayout(message: Message) {
        // Get user's display name from Firestore
        val docRef = db.collection("Account Data").document(message.userId!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val displayName = document.getString("Display Name")
                    // Create a new TextView for the message
                    val messageTextView = TextView(fragmentContext).apply {
                        text = buildString {
                            append(displayName)
                            append(": ")
                            append(message.text)
                        }
                        // Add any styling you want here
                    }
                    // Add the TextView to the chat layout
                    chatLayout.addView(messageTextView)
                } else {
                    Log.d("addMessageToLayoutFunction", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("addMessageToLayoutFunction", "get failed with ", exception)
            }
    }
}

