package com.jldevelops.neverhaveiever

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jldevelops.neverhaveiever.databinding.ActivityQuestionListBinding

class QuestionList : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionListBinding
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questions: MutableList<Question>
    data class Question(val id: String, val text: String)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        Log.d("QuestionList", "Before retrieving questionsetedit")
        // Initialize Firebase Firestore
        val db = Firebase.firestore

        // Get the current user's UID
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val questionsetedit = intent.getIntExtra("questionsetedit", 0)

        if (uid != null) {
            // Reference the question sets collection for the current user
            val questionSetsRef = db.collection("Account Data").document(uid).collection("Question Sets")

            // Get the selected question set document based on the questionsetedit value
            val selectedQuestionSet = when (questionsetedit) {
                1 -> questionSetsRef.document("Set1")
                2 -> questionSetsRef.document("Set2")
                3 -> questionSetsRef.document("Set3")
                else -> questionSetsRef.document("Set1") // Default to Set1 if the value is not recognized
            }

            // Retrieve the selected question set document
            selectedQuestionSet.get()
                .addOnSuccessListener { questionSetDocument ->
                    if (questionSetDocument.exists()) {
                        val questions = mutableListOf<Question>()

                        // Retrieve the questions from the document fields
                        val question1 = questionSetDocument.getString("Q1")
                        val question2 = questionSetDocument.getString("Q2")
                        // Add more lines for each question field
                        val question3 = questionSetDocument.getString("Q3")
                        val question4 = questionSetDocument.getString("Q4")
                        val question5 = questionSetDocument.getString("Q5")
                        val question6 = questionSetDocument.getString("Q6")
                        val question7 = questionSetDocument.getString("Q7")
                        val question8 = questionSetDocument.getString("Q8")
                        val question9 = questionSetDocument.getString("Q9")
                        val question10 = questionSetDocument.getString("Q10")
                        val question11 = questionSetDocument.getString("Q11")
                        val question12 = questionSetDocument.getString("Q12")
                        val question13 = questionSetDocument.getString("Q13")
                        val question14 = questionSetDocument.getString("Q14")
                        val question15 = questionSetDocument.getString("Q15")
                        val question16 = questionSetDocument.getString("Q16")
                        val question17 = questionSetDocument.getString("Q17")
                        val question18 = questionSetDocument.getString("Q18")
                        val question19 = questionSetDocument.getString("Q19")
                        val question20 = questionSetDocument.getString("Q20")
                        // Create Question objects and add them to the list
                        val questionObj1 = Question("Q1", question1 ?: "")
                        questions.add(questionObj1)
                        val questionObj2 = Question("Q2", question2 ?: "")
                        questions.add(questionObj2)
                        val questionObj3 = Question("Q3", question3 ?: "")
                        questions.add(questionObj3)
                        val questionObj4 = Question("Q4", question4 ?: "")
                        questions.add(questionObj4)
                        val questionObj5 = Question("Q5", question5 ?: "")
                        questions.add(questionObj5)
                        val questionObj6 = Question("Q6", question6 ?: "")
                        questions.add(questionObj6)
                        val questionObj7 = Question("Q7", question7 ?: "")
                        questions.add(questionObj7)
                        val questionObj8 = Question("Q8", question8 ?: "")
                        questions.add(questionObj8)
                        val questionObj9 = Question("Q9", question9 ?: "")
                        questions.add(questionObj9)
                        val questionObj10 = Question("Q10", question10 ?: "")
                        questions.add(questionObj10)
                        val questionObj11 = Question("Q11", question11 ?: "")
                        questions.add(questionObj11)
                        val questionObj12 = Question("Q12", question12 ?: "")
                        questions.add(questionObj12)
                        val questionObj13 = Question("Q13", question13 ?: "")
                        questions.add(questionObj13)
                        val questionObj14 = Question("Q14", question14 ?: "")
                        questions.add(questionObj14)
                        val questionObj15 = Question("Q15", question15 ?: "")
                        questions.add(questionObj15)
                        val questionObj16 = Question("Q16", question16 ?: "")
                        questions.add(questionObj16)
                        val questionObj17 = Question("Q17", question17 ?: "")
                        questions.add(questionObj17)
                        val questionObj18 = Question("Q18", question18 ?: "")
                        questions.add(questionObj18)
                        val questionObj19 = Question("Q19", question19 ?: "")
                        questions.add(questionObj19)
                        val questionObj20 = Question("Q20", question20 ?: "")
                        questions.add(questionObj20)
                        // Add more lines for each question object

                        // Set up the RecyclerView and adapter
                        questionAdapter = QuestionAdapter(questions)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)
                        binding.recyclerView.adapter = questionAdapter

                        // Set up click listeners for edit and clear buttons
                        questionAdapter.setOnEditClickListener { position ->
                            val question = questions[position]
                            showEditDialog(question)
                        }

                        questionAdapter.setOnClearClickListener { position ->
                            val question = questions[position]
                            clearQuestion(question)
                        }
                    } else {
                        Log.e("Main", "Selected question set document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the failure case
                    Log.e("Main", "Failed to retrieve selected question set: ${e.message}", e)
                }
        }
        Log.d("QuestionList", "After retrieving questionsetedit")
    }

    private fun showEditDialog(question: Question) {
        val input = EditText(this)
        input.setText(question.text)

        val dialogBuilder = AlertDialog.Builder(this)
            .setMessage("Enter your question...")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("Confirm") { _, _ ->
                val updatedQuestion = question.copy(text = input.text.toString())
                updateQuestion(updatedQuestion)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun updateQuestion(question: Question) {
        // Get the current user's UID
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        questions = mutableListOf()
        if (uid != null) {
            // Reference the question sets collection for the current user
            val questionSetsRef = Firebase.firestore
                .collection("Account Data")
                .document(uid)
                .collection("Question Sets")

            // Get the selected question set document based on the questionsetedit value
            val selectedQuestionSet = when (questionsetedit) {
                1 -> questionSetsRef.document("Set1")
                2 -> questionSetsRef.document("Set2")
                3 -> questionSetsRef.document("Set3")
                else -> questionSetsRef.document("Set1") // Default to Set1 if the value is not recognized
            }

            // Update the respective question field in the selected question set document
            selectedQuestionSet.update(question.id, question.text)
                .addOnSuccessListener {
                    // Notify the adapter of the specific item change
                    val position = questions.indexOf(question)
                    if (position != -1) {
                        questionAdapter.notifyItemChanged(position)
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the failure case
                    Log.e("Main", "Failed to update question: ${e.message}", e)
                }
        }
    }

    private fun clearQuestion(question: Question) {
        // Get the current user's UID
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        questions = mutableListOf()
        if (uid != null) {
            // Reference the question sets collection for the current user
            val questionSetsRef = Firebase.firestore
                .collection("Account Data")
                .document(uid)
                .collection("Question Sets")

            // Get the selected question set document based on the questionsetedit value
            val selectedQuestionSet = when (questionsetedit) {
                1 -> questionSetsRef.document("Set1")
                2 -> questionSetsRef.document("Set2")
                3 -> questionSetsRef.document("Set3")
                else -> questionSetsRef.document("Set1") // Default to Set1 if the value is not recognized
            }

            // Clear the respective question field in the selected question set document
            selectedQuestionSet.update(question.id, "")
                .addOnSuccessListener {
                    // Notify the adapter of the item removal
                    val position = questions.indexOf(question)
                    if (position != -1) {
                        questionAdapter.notifyItemRemoved(position)
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the failure case
                    Log.e("Main", "Failed to clear question: ${e.message}", e)
                }
        }
    }


    inner class QuestionAdapter(private val questions: List<Question>) :
        RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

        private var onEditClickListener: ((position: Int) -> Unit)? = null
        private var onClearClickListener: ((position: Int) -> Unit)? = null

        inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
            private val editButton: Button = itemView.findViewById(R.id.editButton)
            private val clearButton: Button = itemView.findViewById(R.id.clearButton)

            init {
                editButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onEditClickListener?.invoke(position)
                    }
                }

                clearButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onClearClickListener?.invoke(position)
                    }
                }
            }

            fun bind(question: Question) {
                questionTextView.text = question.text
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_question, parent, false)
            return QuestionViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            val question = questions[position]
            holder.bind(question)
        }

        override fun getItemCount(): Int {
            return questions.size
        }

        fun setOnEditClickListener(listener: (position: Int) -> Unit) {
            onEditClickListener = listener
        }

        fun setOnClearClickListener(listener: (position: Int) -> Unit) {
            onClearClickListener = listener
        }
    }

}

