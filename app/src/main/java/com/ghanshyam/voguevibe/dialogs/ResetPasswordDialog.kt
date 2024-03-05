package com.ghanshyam.voguevibe.dialogs

import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.ghanshyam.voguevibe.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
) {

    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)

    dialog.setContentView(view)
    dialog.show()
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

    val input_email: EditText = view.findViewById(R.id.input_reset_email)
    val cancel: Button = view.findViewById(R.id.cancel_button)
    val submit: Button = view.findViewById(R.id.submit_button)

    submit.setOnClickListener {
        val email = input_email.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    cancel.setOnClickListener {
        dialog.dismiss()
    }
}