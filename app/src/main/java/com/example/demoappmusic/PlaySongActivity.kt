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

    lateinit var playMusic: PlayMusic
    var isStop = true
    lateinit var animator: AnimatorSet
    var position: Int = 0

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
            button_play_pause.setImageResource(R.drawable.ic_pause_circle)
            animator.start()
        }

        button_play_pause.setOnClickListener {
            if (isStop) {
                playMusic.onPauseSong()
                isStop = false
                button_play_pause.setImageResource(R.drawable.ic_play_circle)
                animator.pause()
            } else {
                playMusic.onResumeSong(playMusic.onPauseSong())
                isStop = true
                button_play_pause.setImageResource(R.drawable.ic_pause_circle)
                animator.resume()
            }
        }

        button_skip_next.setOnClickListener {
            animator.end()
            if (!isStop) {
                isStop = true
                button_play_pause.setImageResource(R.drawable.ic_pause_circle)
            }
            animator.start()
            playMusic.onSkipNext(text_name_song, text_time_current, text_total_time, seekBar_music)
        }

        button_skip_previous.setOnClickListener {
            animator.end()
            if (!isStop) {
                isStop = true
                button_play_pause.setImageResource(R.drawable.ic_pause_circle)
            }
            animator.start()
            playMusic.onSkipPrevious(
                text_name_song,
                text_time_current,
                text_total_time,
                seekBar_music
            )
        }

        seekBar_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playMusic.setSeekBar(seekBar_music)
            }
        })
    }

    private fun initAnimator() {
        animator = AnimatorSet()
        val obj = ObjectAnimator.ofFloat(imageView_disc, "rotation", 0f, 360f)
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
                text_name_song,
                text_time_current,
                text_total_time,
                seekBar_music,
                position
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
}
