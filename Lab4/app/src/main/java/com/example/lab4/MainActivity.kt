package com.example.lab4

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import com.example.lab4.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), MediaPlayerDelegate, MediaFastControlDelegate,
    MediaProgressBarDelegate {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mediaFragment: MediaFragment

    private lateinit var mediaFastControl: MediaFastControl
    private lateinit var mediaProgress: MediaProgressBar

    private var mediaPlayer: MediaPlayer? = null
    private var currentURI: Uri? = null
    private var currentMediaProgressHandler: Handler? = null
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

        binding.browseAudioButton.setOnClickListener { openFileSelection("audio/*") }
        binding.browseVideoButton.setOnClickListener { openFileSelection("video/*") }
        binding.browseByLinkButton.setOnClickListener {
            if (hasInternet()) {
                val dialog = EnterURLDialogFragment { uri -> currentURI = uri ; updateMediaPlayer() }
                dialog.show(supportFragmentManager, "EnterURLDialog")
            } else {
                Toast.makeText(this, "Your device is not connected to the Internet", Toast.LENGTH_SHORT).show()
            }
//            openByWebURI("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3".toUri())
//            openByWebURI("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4".toUri())
        }

        currentURI = ("android.resource://" + packageName + "/" + R.raw.test_video_1).toUri()
    }

    override fun onPause() {
        currentMediaProgressHandler?.removeCallbacksAndMessages(null)
        mediaPlayer?.stop()
        super.onPause()
    }

    override fun onStop() {
        releaseMediaPlayer()
        super.onStop()
    }

    //Media Player Control
    private fun updateMediaPlayer() {
        updateMediaPlayer(currentURI, mediaFragment.holder)
    }
    private fun updateMediaPlayer(uri: Uri?, holder: SurfaceHolder) {
        releaseMediaPlayer()

        mediaPlayer = MediaPlayer.create(this, uri, holder)
        mediaPlayer?.setOnVideoSizeChangedListener { player, width, height ->
            setSurfaceDimensions(player, width, height)
        }

        mediaProgress.setProgress(0)
        updateMediaProgressHandler()

        mediaPlayer?.start()
    }

    private fun updateMediaProgressHandler() {
        currentMediaProgressHandler = Handler(Looper.getMainLooper())
        currentMediaProgressHandler?.postDelayed(object: Runnable {
            override fun run() {
                mediaPlayer?.let {
                    mediaProgress.setProgress((it.currentPosition.toFloat() / it.duration.toFloat() * 100).toInt())
                    currentMediaProgressHandler?.postDelayed(this, 1000)
                }
            }
        }, 0)
    }

    private fun prepareMediaPlayer(uri: String) {
        try {
            Log.d("!!!", "TRY!")
            mediaPlayer?.setDataSource(uri)
            mediaPlayer?.prepareAsync()
        } catch (e: IllegalArgumentException)  {
            Log.d("!!!", e.toString())
        }

        mediaPlayer?.setOnPreparedListener {
            Log.d("!!!", "PREPARED")
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

    private fun releaseMediaPlayer() {
        currentMediaProgressHandler?.removeCallbacksAndMessages(null)
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private val MediaPlayer.seconds: Float
        get() { return this.duration.toFloat() / 1000 }

    private var MediaPlayer.currentSeconds: Float
        get() { return this.currentPosition.toFloat() / 1000 }
        set(value) = this.seekTo((value * 1000).toLong(), MediaPlayer.SEEK_CLOSEST)

    //Media MediaPlayerDelegate
    override fun surfaceDidCreated(holder: SurfaceHolder) {
        updateMediaPlayer()
    }

    override fun surfaceDidClicked() {
        binding.fastControlPlaceholder.startAnimation(animations[FADE_OUT])
    }

    //Player Control Delegate
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
        mediaPlayer?.let { mp ->
            mp.currentSeconds = mp.seconds / 100.0f * value.toFloat()
        }
    }

    //File Selection
    private fun openFileSelection(contentType: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = contentType
//        startActivity(Intent.createChooser(intent, "Chose Media File"))
        mediaPlayer?.stop()
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { currentURI = it }
        }
    }

    private fun hasInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun requestPermission() {
//        ActivityCompat.requestPermissions(this, Manifest.permission.)
    }
}