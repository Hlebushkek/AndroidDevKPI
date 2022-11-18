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

    private val toggleButtonIcons = arrayOf(R.drawable.ic_baseline_play_arrow_32, R.drawable.ic_baseline_pause_32)

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

    fun setControlEnabled(b: Boolean) {
        binding.backwardButton.isClickable = b
        binding.forwardButton.isClickable = b
        binding.toggleButton.isClickable = b
    }

     fun toggleIcon() {
        binding.toggleButton.setImageResource(toggleButtonIcons[0])
        val temp = toggleButtonIcons[0]
        toggleButtonIcons[0] = toggleButtonIcons[1]
        toggleButtonIcons[1] = temp
    }

    private fun toggleMedia(view: View) {
        delegate?.mediaDidToggle()
        toggleIcon()
    }

    private fun fastForward(view: View) {
        val value: Int = when (view) {
            binding.backwardButton -> -2
            binding.forwardButton -> 2
            else -> { 0 }
        }

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