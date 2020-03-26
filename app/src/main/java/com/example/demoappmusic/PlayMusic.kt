package com.example.demoappmusic

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.ViewManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_play_song.*
import java.text.SimpleDateFormat

class PlayMusic : Service() {

    val iBinder = LocalBinder()
    lateinit var mediaPlayer: MediaPlayer
    var position: Int = 0
    var isPlay = false

    inner class LocalBinder : Binder() {
        fun getService(): PlayMusic {
            return this@PlayMusic
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return iBinder
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    fun onSkipNext(
        text_name: TextView,
        text_time_current: TextView,
        text_time_total: TextView,
        seekBar: SeekBar
    ) {
        mediaPlayer.stop()
        position++
        if (position > MainActivity.arrSong.size - 1) {
            position = 0
        }
        onPlaySong(text_name, text_time_current, text_time_total, seekBar, position)
    }

    fun onSkipPrevious(
        text_name: TextView,
        text_time_current: TextView,
        text_time_total: TextView,
        seekBar: SeekBar
    ) {
        mediaPlayer.stop()
        position--
        if (position < 0) {
            position = MainActivity.arrSong.size - 1
        }
        onPlaySong(text_name, text_time_current, text_time_total, seekBar, position)
    }

    fun onPauseSong(): Int {
        mediaPlayer.pause()
        return mediaPlayer.currentPosition
    }

    fun onResumeSong(x: Int) {
        mediaPlayer.seekTo(x)
        mediaPlayer.start()
    }

    fun setSeekBar(seekBar: SeekBar) {
        mediaPlayer.seekTo(seekBar.progress)
    }

    fun onPlaySong(
        text_name: TextView,
        text_time_current: TextView,
        text_time_total: TextView,
        seekBar: SeekBar,
        positionCurrent: Int
    ) {
        position = positionCurrent
        if (isPlay) {
            mediaPlayer.stop()
        }
        isPlay = true
        mediaPlayer = MediaPlayer.create(applicationContext, MainActivity.arrSong[position].file)
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        text_name.text = MainActivity.arrSong[position].title
        text_time_total.text = simpleDateFormat.format(mediaPlayer.duration)
        seekBar.max = mediaPlayer.duration
        mediaPlayer.start()

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                text_time_current.text = simpleDateFormat.format(mediaPlayer.currentPosition)
                seekBar.progress = mediaPlayer.currentPosition

                mediaPlayer.setOnCompletionListener {
                    onSkipNext(text_name, text_time_current, text_time_total, seekBar)
                }
                handler.postDelayed(this, 0)
            }
        }, 2000)
    }
}
