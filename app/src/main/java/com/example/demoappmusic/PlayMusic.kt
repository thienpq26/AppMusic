package com.example.demoappmusic

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.widget.*
import java.text.SimpleDateFormat

class PlayMusic : Service() {

    val iBinder = LocalBinder()
    lateinit var mediaPlayer: MediaPlayer
    var position: Int = 2

    inner class LocalBinder : Binder() {
        fun getService(): PlayMusic {
            return this@PlayMusic
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer =
            MediaPlayer.create(applicationContext, PlaySongActivity.arrSong.get(position).file)
    }

    override fun onBind(intent: Intent): IBinder {
        return iBinder
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    fun onPauseSong(): Int {
        mediaPlayer.pause()
        return mediaPlayer.currentPosition
    }

    fun onResumeSong(x: Int) {
        mediaPlayer.seekTo(x)
        mediaPlayer.start()
    }

    fun onSkipNext() {
        mediaPlayer.stop()
        position++
        if (position > PlaySongActivity.arrSong.size - 1) {
            position = 0
        }
        onCreate()
    }

    fun onSkipPrevious() {
        mediaPlayer.stop()
        position--
        if (position < 0) {
            position = PlaySongActivity.arrSong.size - 1
        }
        onCreate()
    }

    fun setSeekBar(seekBar: SeekBar) {
        mediaPlayer.seekTo(seekBar.progress)
    }

    fun onPlaySong(
        text_name: TextView,
        text_time_current: TextView,
        text_time_total: TextView,
        seekBar: SeekBar
    ) {
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        text_name.text = PlaySongActivity.arrSong[position].title
        text_time_total.text = simpleDateFormat.format(mediaPlayer.duration)
        seekBar.max = mediaPlayer.duration
        mediaPlayer.start()

        val handler = Handler()
        //Sau khoảng thời gian delay (millis) thì function run() bên trong mới được gọi
        //Nên để thời gian delay ngoài là 2s để tránh trường hợp nhạc chưa được phát thời gian đã được tính
        //Gây ra text_time_current > text_time_total

        handler.postDelayed(object : Runnable {
            override fun run() {
                val simpleDateFormat = SimpleDateFormat("mm:ss")
                text_time_current.text = simpleDateFormat.format(mediaPlayer.currentPosition)
                seekBar.progress = mediaPlayer.currentPosition
                //handler tiến hành gọi lại chính nó, có thể coi là đệ quy chính nó
                //nếu handler không tự gọi lại thì run chỉ chạy duy nhất một lần sau thời gian delay của lần gọi đầu tiên

                mediaPlayer.setOnCompletionListener {
                    onSkipNext()
                    onCreate()
                    onPlaySong(text_name, text_time_current, text_time_total, seekBar)
                }
                handler.postDelayed(this, 0)
            }
        }, 2000)
    }
}
