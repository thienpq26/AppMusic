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
import android.widget.SeekBar
import com.example.demoappmusic.Model.SongModel
import kotlinx.android.synthetic.main.activity_play_song.*

class PlaySongActivity : AppCompatActivity() {

    companion object {
        lateinit var arrSong: ArrayList<SongModel>
    }

    lateinit var playMusic: PlayMusic
    var isPlay = true
    var isStop = true
    lateinit var animator: AnimatorSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)

        initArrSong()
        initAnimator()
        button_play_pause.setOnClickListener {
            if (isPlay) {
                val intent = Intent(this, PlayMusic::class.java)
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
                button_play_pause.setImageResource(R.drawable.ic_pause_circle)
                animator.start()
                isPlay = false
            } else {
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

        }

        button_skip_next.setOnClickListener {
            animator.end()
            playMusic.onSkipNext()
            if (!isStop) {
                isStop = true
                button_play_pause.setImageResource(R.drawable.ic_pause_circle)
            }
            animator.start()
            playMusic.onPlaySong(text_name_song, text_time_current, text_total_time, seekBar_music)
        }

        button_skip_previous.setOnClickListener {
            animator.end()
            playMusic.onSkipPrevious()
            if (!isStop) {
                isStop = true
                button_play_pause.setImageResource(R.drawable.ic_pause_circle)
            }
            animator.start()
            playMusic.onPlaySong(text_name_song, text_time_current, text_total_time, seekBar_music)
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

    private fun initArrSong() {
        arrSong = ArrayList()
        arrSong.add(SongModel("Can't take my eyes off you", R.raw.can_not_take_my_eyes_off_you))
        arrSong.add(SongModel("Gặp nhau làm ngơ", R.raw.gap_nhau_lam_ngo))
        arrSong.add(SongModel("Kết duyên", R.raw.ket_duyen))
        arrSong.add(SongModel("Take me to your heart", R.raw.take_me_to_your_heart))
    }

    val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayMusic.LocalBinder
            playMusic = binder.getService()
            playMusic.onPlaySong(text_name_song, text_time_current, text_total_time, seekBar_music)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
}
