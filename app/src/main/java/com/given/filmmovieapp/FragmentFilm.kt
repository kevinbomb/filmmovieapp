package com.given.filmmovieapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.given.filmmovieapp.entity.Film

class FragmentFilm : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVFilmAdapter = RVFilmAdapter(Film.listOfFilm)
        val rvFilm : RecyclerView = view.findViewById(R.id.rv_film)
        rvFilm.layoutManager = layoutManager
        rvFilm.setHasFixedSize(true)
        rvFilm.adapter = adapter
    }

}