package com.given.filmmovieapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.given.filmmovieapp.AddEditActivity
import com.given.filmmovieapp.UpcomingActivity
import com.given.filmmovieapp.R
import com.given.filmmovieapp.models.Upcoming
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class UpcomingAdapter(private var upcomingList: List<Upcoming>, context: Context):
    RecyclerView.Adapter<UpcomingAdapter.ViewHolder>(), Filterable{

    private var filteredUpcomingList: MutableList<Upcoming>
    private val context: Context

    init {
        filteredUpcomingList = ArrayList(upcomingList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_upcoming, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredUpcomingList.size
    }

    fun setUpcomingList(upcomingList: Array<Upcoming>){
        this.upcomingList = upcomingList.toList()
        filteredUpcomingList = upcomingList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val upcoming = filteredUpcomingList[position]
        holder.tvJudul.text = upcoming.judul
        holder.tvDirektur.text = upcoming.direktur
        holder.tvTanggal.text = upcoming.tanggal
        holder.tvSinopsis.text = upcoming.sinopsis

        holder.btnDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus upcoming ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_,_ ->
                    if (context is UpcomingActivity) upcoming.id?.let { it1 ->
                        context.deleteUpcoming(
                            it1
                        )
                    }
                }
                .show()

        }

        holder.cvUpcoming.setOnClickListener {
            val i = Intent(context, AddEditActivity::class.java)
            i.putExtra("id", upcoming.id)
            if(context is UpcomingActivity)
                context.startActivityForResult(i, UpcomingActivity.LAUNCH_ADD_ACTIVITY)
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Upcoming> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(upcomingList)
                }else{
                    for (upcoming in upcomingList){
                        if(upcoming.judul.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))

                        )filtered.add(upcoming)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults

            }

            override fun publishResults( CharSequence: CharSequence, filterResults: FilterResults) {
                filteredUpcomingList.clear()
                filteredUpcomingList.addAll(filterResults.values as List<Upcoming>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvJudul: TextView
        var tvDirektur: TextView
        var tvTanggal: TextView
        var tvSinopsis: TextView
        var btnDelete: ImageButton
        var cvUpcoming: CardView

        init {
            tvJudul = itemView.findViewById(R.id.tv_judul)
            tvDirektur = itemView.findViewById(R.id.tv_direktur)
            tvTanggal = itemView.findViewById(R.id.tv_tanggal)
            tvSinopsis = itemView.findViewById(R.id.tv_sinopsis)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvUpcoming = itemView.findViewById(R.id.cv_upcoming)
        }

    }
}