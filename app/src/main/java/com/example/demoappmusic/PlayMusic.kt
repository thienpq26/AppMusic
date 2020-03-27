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

    private val iBinder = LocalBinder()
    private lateinit var mediaPlayer: MediaPlayer
    private var position: Int = 0
    private var isPlay = false

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
        textName: TextView,
        textTimeCurrent: TextView,
        textTimeTotal: TextView,
        seekBar: SeekBar
    ) {
        mediaPlayer.stop()
        position++
        if (position > MainActivity.arrSong.size - 1) {
            position = 0
        }
        onPlaySong(textName, textTimeCurrent, textTimeTotal, seekBar, position)
    }

    fun onSkipPrevious(
        textName: TextView,
        textTimeCurrent: TextView,
        textTimeTotal: TextView,
        seekBar: SeekBar
    ) {
        mediaPlayer.stop()
        position--
        if (position < 0) {
            position = MainActivity.arrSong.size - 1
        }
        onPlaySong(textName, textTimeCurrent, textTimeTotal, seekBar, position)
    }

    fun onPauseSong(): Int {
        mediaPlayer.pause()
        return mediaPlayer.currentPosition
    }

    fun onResumeSong(positionCurrent: Int) {
        mediaPlayer.seekTo(positionCurrent)
        mediaPlayer.start()
    }

    fun setSeekBar(seekBar: SeekBar) {
        mediaPlayer.seekTo(seekBar.progress)
    }

    fun onPlaySong(
        textName: TextView,
        textTimeCurrent: TextView,
        textTimeTotal: TextView,
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
        textName.text = MainActivity.arrSong[position].title
        textTimeTotal.text = simpleDateFormat.format(mediaPlayer.duration)
        seekBar.max = mediaPlayer.duration
        mediaPlayer.start()

        //Sau khoảng thời gian delay (millis) thì function run() bên trong mới được gọi
        //Nên để thời gian delay ngoài là 2s để tránh trường hợp nhạc chưa được phát thời gian đã được tính
        //Gây ra text_time_current > text_time_total

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                textTimeCurrent.text = simpleDateFormat.format(mediaPlayer.currentPosition)
                seekBar.progress = mediaPlayer.currentPosition

                mediaPlayer.setOnCompletionListener {
                    onSkipNext(textName, textTimeCurrent, textTimeTotal, seekBar)
                }

                //handler tiến hành gọi lại chính nó, có thể coi là đệ quy chính nó
                //nếu handler không tự gọi lại thì run chỉ chạy duy nhất một lần sau thời gian delay của lần gọi đầu tiên
                handler.postDelayed(this, 0)
            }
        }, 2000)
    }
}
