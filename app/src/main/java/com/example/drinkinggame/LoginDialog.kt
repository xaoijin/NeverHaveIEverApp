package com.example.drinkinggame

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class LoginDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(requireActivity().layoutInflater.inflate(R.layout.fragment_login, null))
            alertDialog.create()
        }?:throw IllegalStateException("Activity is null")

    }
}