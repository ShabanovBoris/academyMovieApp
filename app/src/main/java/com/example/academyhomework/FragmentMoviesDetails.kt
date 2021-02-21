package com.example.academyhomework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academyhomework.adapters.ActorRecyclerAdapter
import com.example.academyhomework.models.Actor


class FragmentMoviesDetails : Fragment() {

    private lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.actor_recycler)
        var mockup = listOf<Actor>(
            Actor("Robert Downey Jr.",R.drawable.rob),
            Actor("Chris Evans",R.drawable.cris),
            Actor("Mark Ruffalo",R.drawable.halk),
            Actor("Chris Hemsworth",R.drawable.tor),

        )
        var adapter = ActorRecyclerAdapter()
        adapter.bindActors(mockup)
        recyclerView.adapter = adapter

    }
}