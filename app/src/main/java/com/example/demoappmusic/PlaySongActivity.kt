package com.example.demoappmusic

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.example.demoappmusic.Model.SongModel
import kotlinx.android.synthetic.main.activity_play_song.*

class PlaySongActivity : AppCompatActivity() {

    private lateinit var playMusic: PlayMusic
    private var isStop = true
    private lateinit var animator: AnimatorSet
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lc", "onCreate PlaySong")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)

        initAnimator()

        val data = intent
        if (data != null) {
            val intent = Intent(this, PlayMusic::class.java)
            position = data.getIntExtra("data", -1)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            buttonPlayPause.setImageResource(R.drawable.ic_pause_circle)
            animator.start()
        }

        buttonPlayPause.setOnClickListener {
            if (isStop) {
                playMusic.onPauseSong()
                isStop = false
                buttonPlayPause.setImageResource(R.drawable.ic_play_circle)
                animator.pause()
            } else {
                playMusic.onResumeSong(playMusic.onPauseSong())
                isStop = true
                buttonPlayPause.setImageResource(R.drawable.ic_pause_circle)
                animator.resume()
            }
        }

        buttonNext.setOnClickListener {
            animator.end()
            if (!isStop) {
                isStop = true
                buttonPlayPause.setImageResource(R.drawable.ic_pause_circle)
            }
            animator.start()
            playMusic.onSkipNext(textSong, textTimeCurrent, textTotalTime, seekBarMusic)
        }

        buttonPrevious.setOnClickListener {
            animator.end()
            if (!isStop) {
                isStop = true
                buttonPlayPause.setImageResource(R.drawable.ic_pause_circle)
            }
            animator.start()
            playMusic.onSkipPrevious(
                textSong,
                textTimeCurrent,
                textTotalTime,
                seekBarMusic
            )
        }

        seekBarMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playMusic.setSeekBar(seekBarMusic)
            }
        })
    }

    private fun initAnimator() {
        animator = AnimatorSet()
        val obj = ObjectAnimator.ofFloat(imageViewDisc, "rotation", 0f, 360f)
        obj.duration = 13000
        obj.repeatCount = ValueAnimator.INFINITE
        obj.repeatMode = ValueAnimator.RESTART
        animator.play(obj)
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayMusic.LocalBinder
            playMusic = binder.getService()
            playMusic.onPlaySong(
                textSong,
                textTimeCurrent,
                textTotalTime,
                seekBarMusic,
                position
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
}
