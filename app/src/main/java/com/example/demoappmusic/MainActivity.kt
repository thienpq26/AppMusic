package com.example.demoappmusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoappmusic.Adapter.SongAdapter
import com.example.demoappmusic.Model.SongModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var arrSong: ArrayList<SongModel>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initArrSong()
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerview.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(this@MainActivity, PlaySongActivity::class.java)
                intent.putExtra("data", position)
                startActivity(intent)
            }
        })
        var adapter = SongAdapter(arrSong)
        recyclerview.adapter = adapter
    }

    private fun initArrSong() {
        arrSong = ArrayList()
        arrSong.add(SongModel("Bạc phận", R.raw.bac_phan))
        arrSong.add(SongModel("Can't take my eyes off you", R.raw.can_not_take_my_eyes_off_you))
        arrSong.add(SongModel("Chắc ai đó sẽ về", R.raw.chac_ai_do_se_ve))
        arrSong.add(SongModel("Chạy ngay đi", R.raw.chay_ngay_di))
        arrSong.add(SongModel("Chúng ta không thuộc về nhau", R.raw.chung_ta_khong_thuoc_ve_nhau))
        arrSong.add(SongModel("Em gì ơi", R.raw.em_gi_oi))
        arrSong.add(SongModel("Hãy trao cho anh", R.raw.hay_trao_cho_anh))
        arrSong.add(SongModel("Kết duyên", R.raw.ket_duyen))
        arrSong.add(SongModel("Lạc trôi", R.raw.lac_troi))
        arrSong.add(SongModel("Sóng gió", R.raw.song_gio))
        arrSong.add(SongModel("Take me to your heart", R.raw.take_me_to_your_heart))
    }
}
