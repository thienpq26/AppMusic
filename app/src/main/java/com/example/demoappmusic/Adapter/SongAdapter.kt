package com.example.demoappmusic.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoappmusic.Model.SongModel
import com.example.demoappmusic.R

class SongAdapter(val mList: ArrayList<SongModel>) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text_name_song = itemView.findViewById<TextView>(R.id.item_text_name)
        val image_singer = itemView.findViewById<ImageView>(R.id.item_image_singer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_song, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text_name_song.text = mList[position].title
    }
}
