package com.example.academyhomework.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.academyhomework.R
import com.example.academyhomework.entities.Actor
import com.example.academyhomework.entities.Movie
import com.google.android.material.imageview.ShapeableImageView

class MovieListAdapterMini:RecyclerView.Adapter<MovieListAdapterMini.ViewHolder>() {

    private var list = listOf<Movie>()
    fun bindMovies(m: List<Movie>){
        list = m
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private var image:ShapeableImageView = itemView.findViewById(R.id.iv_image_mini)

        fun bindData(movie: Movie){
            image.load(movie.imageUrl){
                crossfade(true)
                placeholder(R.drawable.ic_loading_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_actor,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

}