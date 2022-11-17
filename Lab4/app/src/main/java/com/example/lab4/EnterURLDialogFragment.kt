package com.example.lab4

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.example.lab4.databinding.FragmentEnterUrlDialogFragmentBinding

class EnterURLDialogFragment(val onCompletionHandler: (Uri) -> Unit): DialogFragment() {

    private lateinit var binding: FragmentEnterUrlDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterUrlDialogFragmentBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener { dismiss() }
        binding.okButton.setOnClickListener {
            val text = binding.textInputLayout.editText?.text
            if (text.toString().isNotEmpty()) {
                onCompletionHandler(text.toString().toUri())
            }
            dismiss()
        }

        return binding.root
    }
}