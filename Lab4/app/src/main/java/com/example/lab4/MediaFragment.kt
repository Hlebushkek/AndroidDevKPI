package com.example.lab4

import android.graphics.Point
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import com.example.lab4.databinding.FragmentMediaBinding
import com.example.lab4.databinding.FragmentMediaFastControlBinding

class MediaFragment : Fragment(), SurfaceHolder.Callback {

    var delegate: MediaPlayerDelegate? = null

    private lateinit var binding: FragmentMediaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.surfaceView.setOnClickListener {
            delegate?.surfaceDidClicked()
        }

        val holder = binding.surfaceView.holder
        holder.addCallback(this)
    }

    val holder: SurfaceHolder get() { return binding.surfaceView.holder }

    override fun surfaceCreated(holder: SurfaceHolder) {
        delegate?.surfaceDidCreated(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    fun updateSurfaceSize(param: LayoutParams): SurfaceHolder {
        binding.surfaceView.layoutParams = param
        return binding.surfaceView.holder
    }

    companion object {
        @JvmStatic
        fun newInstance() = MediaFragment()
    }
}

interface MediaPlayerDelegate {
    fun surfaceDidCreated(holder: SurfaceHolder)
    fun surfaceDidClicked()
}