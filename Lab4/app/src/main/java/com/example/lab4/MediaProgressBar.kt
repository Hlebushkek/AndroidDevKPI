package com.example.lab4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.lab4.databinding.FragmentMediaProgressBarBinding

class MediaProgressBar : Fragment() {

    private lateinit var binding: FragmentMediaProgressBarBinding

    var delegate: MediaProgressBarDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaProgressBarBinding.inflate(inflater)

        binding.mediaSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(bar: SeekBar?, value: Int, fromUser: Boolean) {
                if (fromUser) { delegate?.mediaTimeValueDidChange(value) }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        return binding.root
    }

    fun setProgress(value: Int) {
        binding.mediaSeekBar.setProgress(value, true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MediaProgressBar()
    }
}

interface MediaProgressBarDelegate {
    fun mediaTimeValueDidChange(value: Int)
}