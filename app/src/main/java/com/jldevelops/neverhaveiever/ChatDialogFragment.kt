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
import com.google.firebase.firestore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class MessageWithDisplayName(
    val text: String = "",
    val displayName: String? = null,
    val userId: String? = null,
    val messageId: Long = 0
)

class ChatDialogFragment : DialogFragment() {
    private lateinit var chatLayout: LinearLayout
    private lateinit var chatInputEditText: EditText
    private lateinit var dialog: AlertDialog
    private lateinit var scrollView: ScrollView
    private lateinit var fragmentContext: Context
    private lateinit var chatRef: CollectionReference
    private lateinit var counterRef: DocumentReference
    private val db = FirebaseFirestore.getInstance()
    private var scope = CoroutineScope(Dispatchers.Main)
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
        chatRef = db.collection("ChatRooms").document(currentRoom).collection("chat")
        counterRef = db.collection("ChatRooms").document(currentRoom)

        builder.setView(dialogView)
            .setPositiveButton("Send", null)
            .setNegativeButton("Close") { _, _ ->
                dialog.cancel()
            }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(fragmentContext, R.color.LightSalmon)))
        loadChatMessages()
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
            val userInput = chatInputEditText.text.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"

            if (userInput.isNotEmpty()) {
                db.runTransaction { transaction ->
                    val snapshot = transaction.get(counterRef)
                    var messageId = snapshot.getLong("messageIdCounter") ?: 0
                    if(!snapshot.exists()) {
                        // The document does not exist, create it
                        transaction.set(counterRef, mapOf("messageIdCounter" to 1))
                        messageId = 0 // This will be the ID for the first message
                    } else {
                        transaction.update(counterRef, "messageIdCounter", messageId + 1)
                    }
                    messageId
                }.addOnSuccessListener { messageId ->
                    scope.launch {
                        val docRef = db.collection("Account Data").document(userId)
                        val displayName = docRef.get().await().getString("Display Name")

                        val newMessage = MessageWithDisplayName(
                            text = userInput,
                            userId = userId,
                            displayName = displayName,
                            messageId = messageId
                        )
                        chatRef.add(newMessage)
                        chatInputEditText.text.clear()
                    }
                }.addOnFailureListener { e ->
                    Log.w("ChatDialogFragment", "Failed to send message", e)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
    private fun loadChatMessages() {
        chatRef.orderBy("messageId").addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("ChatDialogFragment", "Failed to load chat messages", error)
                return@addSnapshotListener
            }

            value?.let { snapshot ->
                // Clear the chat layout
                chatLayout.removeAllViews()

                scope.launch {
                    val messagesWithDisplayName = snapshot.documents.mapNotNull { document ->
                        val message = document.toObject(MessageWithDisplayName::class.java)
                        message?.let { addMessageToLayout(it) }
                    }
                    messagesWithDisplayName.forEach { message ->
                        createAndAddTextView(message)
                    }
                    scrollToBottom()
                }
            }
        }
    }


    private fun scrollToBottom() {
        scrollView.postDelayed({
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }, 700)
    }

    private suspend fun addMessageToLayout(message: MessageWithDisplayName): MessageWithDisplayName? {
        val userId = message.userId ?: return null // Skip if user ID is null

        return withContext(Dispatchers.IO) {
            val docRef = db.collection("Account Data").document(userId)
            val displayName = docRef.get().await().getString("Display Name")
            MessageWithDisplayName(
                text = message.text,
                displayName = displayName,
                messageId = message.messageId
            )
        }
    }

    private fun createAndAddTextView(message: MessageWithDisplayName) {
        val messageTextView = TextView(fragmentContext).apply {
            text = buildString {
                append(message.displayName)
                append(": ")
                append(message.text)
            }
            // Add any styling you want here
        }
        chatLayout.addView(messageTextView)
    }
}
