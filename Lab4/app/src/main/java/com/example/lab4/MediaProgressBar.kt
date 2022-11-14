package com.example.lab4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab4.databinding.FragmentMediaProgressBarBinding

class MediaProgressBar : Fragment() {

    private lateinit var binding: FragmentMediaProgressBarBinding

    var delegate: MediaProgressBarDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaProgressBarBinding.inflate(inflater)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        delegate = this.activity as MediaProgressBarDelegate
    }

    companion object {
        @JvmStatic
        fun newInstance() = MediaProgressBar()
    }
}

interface MediaProgressBarDelegate {
    fun mediaTimeValueDidChange(value: Int)
}