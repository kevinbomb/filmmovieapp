package com.given.filmmovieapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.given.filmmovieapp.databinding.FragmentUpcomingBinding
import com.given.filmmovieapp.room.upcoming.Constant
import com.given.filmmovieapp.room.upcoming.Upcoming
import com.given.filmmovieapp.room.upcoming.UpcomingDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentUpcoming : Fragment() {
    val db by lazy { UpcomingDB(requireActivity()) }
    lateinit var upcomingAdapter : RVUpcomingAdapter
    private var _binding: FragmentUpcomingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_upcoming, container, false)
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupRecyclerView()
    }

    fun intentEdit(noMov: Int, intentType: Int){
        startActivity(
            Intent(requireActivity().applicationContext, EditUpcomingActivity::class.java)
                .putExtra("intent_id", noMov)
                .putExtra("intent_type", intentType)
        )
    }

    fun setupListener(){
        binding.btnCreate.setOnClickListener{
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    private fun setupRecyclerView(){
        upcomingAdapter = RVUpcomingAdapter(arrayListOf(), object : RVUpcomingAdapter.OnAdapterListener{

            override fun onClick(upcoming: Upcoming) {
                intentEdit(upcoming.num, Constant.TYPE_READ)
            }

            override fun onUpdate(upcoming: Upcoming) {
                intentEdit(upcoming.num, Constant.TYPE_UPDATE)
            }

            override fun onDelete(upcoming: Upcoming) {
                deleteDialog(upcoming)
            }
        })
        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            adapter = upcomingAdapter
        }
    }

    private fun deleteDialog(upcoming: Upcoming){
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You sure to delete this data From ${upcoming.judul}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener{ dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.upcomingDao().deleteUpcoming(upcoming)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }

    override fun onStart(){
        super.onStart()
        loadData()
    }

    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            val upcoming = db.upcomingDao().getUpcoming()
            Log.d("FragmentUpcoming", "dbResponse: $upcoming")
            withContext(Dispatchers.Main){
                upcomingAdapter.setData(upcoming)
            }
        }
    }

}