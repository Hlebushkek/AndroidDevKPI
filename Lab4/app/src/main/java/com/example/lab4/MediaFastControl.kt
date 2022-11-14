package com.example.lab4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab4.databinding.FragmentMediaFastControlBinding

class MediaFastControl : Fragment() {

    private lateinit var binding: FragmentMediaFastControlBinding

    var delegate: MediaFastControlDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaFastControlBinding.inflate(inflater)

        binding.backwardButton.setOnClickListener(this::fastForward)
        binding.forwardButton.setOnClickListener(this::fastForward)
        binding.toggleButton.setOnClickListener(this::toggleMedia)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        delegate = this.activity as MediaFastControlDelegate
    }

    private fun toggleMedia(view: View) {
        Log.d("!!!", "toggleMedia")
        Log.d("!!!", delegate.toString())
        delegate?.mediaDidToggle()
    }

    private fun fastForward(view: View) {
        val value: Int = when (view) {
            binding.backwardButton -> -1
            binding.forwardButton -> 1
            else -> { 0 }
        }
        Log.d("!!!", delegate.toString())
        delegate?.mediaDidFastForwardBy(value)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MediaFastControl()
    }
}

interface MediaFastControlDelegate {
    fun mediaDidFastForwardBy(value: Int)
    fun mediaDidToggle()
}