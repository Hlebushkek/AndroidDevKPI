package com.example.lab4

import android.content.Intent
import android.graphics.Point
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.lab4.databinding.ActivityMainBinding
import java.util.Calendar.SECOND


class MainActivity : AppCompatActivity(), MediaPlayerDelegate, MediaFastControlDelegate,
    MediaProgressBarDelegate {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mediaFragment: MediaFragment

    private lateinit var mediaFastControl: MediaFastControl
    private lateinit var mediaProgress: MediaProgressBar

    private var mediaPlayer: MediaPlayer? = null
    private var playBackPosition: Int = 0

    private var animations = ArrayList<Animation>()
    companion object {
        const val FADE_IN: Int = 0
        const val FADE_OUT: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        animations.add(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        animations.add(AnimationUtils.loadAnimation(this, R.anim.fade_out))

        mediaFragment = binding.playerPlaceholder.getFragment()
        mediaFragment.delegate = this

        mediaFastControl = binding.fastControlPlaceholder.getFragment()
        mediaFastControl.delegate = this

        mediaProgress = binding.progressBarPlaceholder.getFragment()
        mediaProgress.delegate = this
    }

    override fun onResume() {
        super.onResume()
        //mediaPlayer?.start()
    }

    override fun onPause() {
//        mediaPlayer?.let {
//            playBackPosition = it.currentPosition
//        }
//        mediaPlayer?.stop()
        super.onPause()
    }

    override fun onStop() {
//        mediaPlayer?.stop()
//        mediaPlayer?.release()

        super.onStop()
    }

    private fun setupMediaPlayer(holder: SurfaceHolder) {
        val audioAttributes = createAudioAttributes()

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(
            this,
            Uri.parse("android.resource://" + (packageName ?: "") + "/" + R.raw.test_video_1),
            holder
        )

        prepareMediaPlayer()
    }

    private fun prepareMediaPlayer() {
//        mediaPlayer.prepareAsync()
//        mediaPlayer?.setOnPreparedListener {
//            binding.progressBar.visibility = View.INVISIBLE
//            mediaPlayer?.seekTo(playBackPosition)
//            mediaPlayer?.start()
//        }

        mediaPlayer?.setOnVideoSizeChangedListener { player, width, height ->
            setSurfaceDimensions(player, width, height)
            mediaPlayer?.start()
        }
    }

    private fun setSurfaceDimensions(player: MediaPlayer, width: Int, height: Int) {
        if (width > 0 && height > 0) {
            val aspectRatio = height.toFloat() / width.toFloat()
            val screenDimensions = Point()
            windowManager?.defaultDisplay?.getSize(screenDimensions)

            val surfaceWidth = screenDimensions.x
            val surfaceHeight = (surfaceWidth * aspectRatio).toInt()
            val params = ConstraintLayout.LayoutParams(surfaceWidth, surfaceHeight)

            binding.playerPlaceholder.layoutParams = params
            player.setDisplay(mediaFragment.holder)
        }
    }

    private fun createAudioAttributes(): AudioAttributes {
        var builder = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        return builder.build()
    }

    private val MediaPlayer.seconds: Float
        get() { return this.duration.toFloat() / 1000 }

    private var MediaPlayer.currentSeconds: Float
        get() { return this.currentPosition.toFloat() / 1000 }
        set(value) {
            this.seekTo((value * 1000).toLong(), MediaPlayer.SEEK_CLOSEST)
        }

    //Media MediaPlayerDelegate
    override fun surfaceDidCreated(holder: SurfaceHolder) {
        setupMediaPlayer(holder)
    }

    override fun surfaceDidClicked() {
        binding.fastControlPlaceholder.startAnimation(animations[FADE_OUT])
    }

    //Delegates
    override fun mediaDidFastForwardBy(value: Int) {
        mediaPlayer?.let {
            it.currentSeconds = it.currentSeconds + value
        }
    }

    override fun mediaDidToggle() {
        mediaPlayer?.let {
            if (it.isPlaying) it.pause() else it.start()
        }
    }

    override fun mediaTimeValueDidChange(value: Int) {
        TODO("Not yet implemented")
    }

    //File Selection
    private fun openFileSelection() {
        val intent = Intent()
        intent.type = "video/* audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivity(Intent.createChooser(intent, "Chose Media File"))
    }

    private fun requestPermission() {
//        ActivityCompat.requestPermissions(this, Manifest.permission.)
    }
}