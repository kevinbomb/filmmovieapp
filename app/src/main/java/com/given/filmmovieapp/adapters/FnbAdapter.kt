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
import com.given.filmmovieapp.AddEditFnbActivity
import com.given.filmmovieapp.R
import com.given.filmmovieapp.FnbActivity
import com.given.filmmovieapp.models.Fnb
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class FnbAdapter (private var fnbList: List<Fnb>, context: Context):
    RecyclerView.Adapter<FnbAdapter.ViewHolder>(), Filterable {

    private var filteredFnbList: MutableList<Fnb>
    private val context: Context

    init {
        filteredFnbList = ArrayList(fnbList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_fnb, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredFnbList.size
    }

    fun setFnbList(fnbList: Array<Fnb>){
        this.fnbList = fnbList.toList()
        filteredFnbList = fnbList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fnb = filteredFnbList[position]
        holder.tvnamafnb.text = fnb.namafnb
        holder.tvjenisfnb.text = fnb.jenisfnb
        holder.tvjumlahfnb.text = fnb.jumlahfnb
        holder.tvhargafnb.text = fnb.hargafnb

        holder.btnDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus FNB ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_,_ ->
                    if (context is FnbActivity) fnb.id?.let { it1 ->
                        context.deleteFnb(
                            it1
                        )
                    }
                }
                .show()

        }

        holder.cvFnb.setOnClickListener {
            val i = Intent(context, AddEditFnbActivity::class.java)
            i.putExtra("id", fnb.id)
            if(context is FnbActivity)
                context.startActivityForResult(i, FnbActivity.LAUNCH_ADD_ACTIVITY)
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Fnb> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(fnbList)
                }else{
                    for (fnb in fnbList){
                        if(fnb.namafnb.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))

                        )filtered.add(fnb)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults

            }

            override fun publishResults( CharSequence: CharSequence, filterResults: FilterResults) {
                filteredFnbList.clear()
                filteredFnbList.addAll(filterResults.values as List<Fnb>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvnamafnb: TextView
        var tvjenisfnb: TextView
        var tvjumlahfnb: TextView
        var tvhargafnb: TextView
        var btnDelete: ImageButton
        var cvFnb: CardView

        init {
            tvnamafnb = itemView.findViewById(R.id.tv_namafnb)
            tvjenisfnb = itemView.findViewById(R.id.tv_jenisfnb)
            tvjumlahfnb = itemView.findViewById(R.id.tv_jumlahfnb)
            tvhargafnb = itemView.findViewById(R.id.tv_hargafnb)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvFnb = itemView.findViewById(R.id.cv_fnb)
        }

    }
}