package com.example.lab4

import android.content.Context
import android.graphics.Point
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.lab4.databinding.ActivityMainBinding
import com.example.lab4.databinding.FragmentMediaFastControlBinding
import com.example.lab4.databinding.FragmentMediaProgressBarBinding


class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, MediaFastControlDelegate,
    MediaProgressBarDelegate {

    private lateinit var binding: ActivityMainBinding

    private var mediaPlayer: MediaPlayer? = null
    private var playBackPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val holder = binding.surfaceView.holder
        holder.addCallback(this)
    }

    override fun onStart() {
        super.onStart()
        
        MediaProgressBar.newInstance().delegate = this
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        setupMediaPlayer(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        onStop()
        setupMediaPlayer(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()

        Log.d("###", mediaFastControl.toString())
        Log.d("###", mediaProgressBar.toString())
    }

    override fun onPause() {
        mediaPlayer?.let {
            playBackPosition = it.currentPosition
        }
        super.onPause()
    }

    override fun onStop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()

        super.onStop()
    }

    private fun createAudioAttributes(): AudioAttributes {
        var builder = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        return builder.build()
    }

    private fun setupMediaPlayer(holder: SurfaceHolder) {
        val audioAttributes = createAudioAttributes()

        mediaPlayer = MediaPlayer.create(this, Uri.parse("android.resource://" + packageName + "/" + R.raw.test_video_1), holder)
        mediaPlayer?.start()
    }

    private fun setSurfaceDimensions(player: MediaPlayer, width: Int, height: Int) {
        if (width > 0 && height > 0) {
            val aspectRatio = height.toFloat() / width.toFloat()
            val screenDimensions = Point()
            windowManager.defaultDisplay.getSize(screenDimensions)

            val surfaceWidth = screenDimensions.x
            val surfaceHeight = (surfaceWidth * aspectRatio).toInt()
            val params = ConstraintLayout.LayoutParams(surfaceWidth, surfaceHeight)
            binding.surfaceView.layoutParams = params
            val holder = binding.surfaceView.holder
            player.setDisplay(holder)
        }
    }

    private fun prepareMediaPlayer() {
//        mediaPlayer.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
//            binding.progressBar.visibility = View.INVISIBLE
            mediaPlayer?.seekTo(playBackPosition)
            mediaPlayer?.start()
        }

        mediaPlayer?.setOnVideoSizeChangedListener { player, width, height ->
            setSurfaceDimensions(player, width, height)
        }
    }

    override fun mediaDidFastForwardBy(value: Int) {
        mediaPlayer?.let {
            it.seekTo(it.currentPosition + value)
        }
    }

    override fun mediaDidToggle() {
        mediaPlayer?.let {
            Log.d("!!!", it.isPlaying.toString())
            if (it.isPlaying) it.pause() else it.start()
        }
    }

    override fun mediaTimeValueDidChange(value: Int) {
        TODO("Not yet implemented")
    }
}