package com.example.academyhomework

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academyhomework.adapters.MovieListAdapter
import com.example.academyhomework.data.JsonMovieRepository
import com.example.academyhomework.interfaces.ScreenChangeable
import com.example.academyhomework.models.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentMovieList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var listener: ScreenChangeable? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is ScreenChangeable){
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_movie_list, container, false)
        view.findViewById<RecyclerView>(R.id.rv_movie_list).setOnClickListener{}

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = MovieListAdapter{movie -> listener?.moveToDetails(movie)}
        recyclerView = view.findViewById(R.id.rv_movie_list)
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            val list = JsonMovieRepository(requireContext()).loadMovies()
             adapter.submitList(list)
        }
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMovieList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}