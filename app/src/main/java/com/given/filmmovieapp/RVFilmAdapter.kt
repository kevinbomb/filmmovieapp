package com.given.filmmovieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.given.filmmovieapp.entity.Film

class RVFilmAdapter(private val data: Array<Film>): RecyclerView.Adapter<RVFilmAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_film, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvNama.text = currentItem.name
        holder.tvDetails.text = currentItem.director
        holder.tvDate.text = currentItem.release.toString()
        holder.ivImg.setImageResource(currentItem.img)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama)
        val tvDetails: TextView = itemView.findViewById(R.id.tv_details)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val ivImg: ImageView = itemView.findViewById(R.id.imageView2)
    }
}