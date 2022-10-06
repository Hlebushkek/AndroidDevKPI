package com.example.lab1

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.lab1.databinding.PopupDialogBinding

class PopupDialogFragment(private var msg: String, private var typeface: Typeface?) : DialogFragment() {

    private lateinit var binding: PopupDialogBinding

    var onDismissCallback: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PopupDialogBinding.inflate(inflater)

        binding.okPopupButton.setOnClickListener {
            dismiss()
        }
        updateContent()

        return binding.root
    }

    override fun dismiss() {
        onDismissCallback()
        super.dismiss()
    }

    private fun updateContent() {
        binding.messageView.text = msg
        binding.messageView.typeface = typeface
    }
}