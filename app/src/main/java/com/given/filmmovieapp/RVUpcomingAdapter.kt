package com.given.filmmovieapp


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.given.filmmovieapp.databinding.RvItemUpcomingBinding
import com.given.filmmovieapp.room.upcoming.Upcoming

class RVUpcomingAdapter(private val upcoming: ArrayList<Upcoming>, private val listener: OnAdapterListener) : RecyclerView.Adapter<RVUpcomingAdapter.UpcomingHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingHolder {
        val itemView = RvItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingHolder(itemView)
    }

    override fun onBindViewHolder(holder: UpcomingHolder, position: Int){
        val currentItem = upcoming[position]
        holder.binding.tvJudul.text = currentItem.judul
        holder.binding.tvDirektur.text = currentItem.direktur
        holder.binding.tvTanggal.text = currentItem.tanggal
        holder.binding.sinopsis.text = currentItem.sinopsis
        holder.binding.layoutK.setOnClickListener{
            listener.onClick(currentItem)
        }
        holder.binding.edtBtn.setOnClickListener {
            listener.onUpdate(currentItem)
        }
        holder.binding.delBtn.setOnClickListener {
            listener.onDelete(currentItem)
        }
    }

    override fun getItemCount() = upcoming.size
    inner class UpcomingHolder(var binding : RvItemUpcomingBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Upcoming>){
        upcoming.clear()
        upcoming.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(upcoming: Upcoming)
        fun onUpdate(upcoming: Upcoming)
        fun onDelete(upcoming: Upcoming)
    }
}