package com.example.academyhomework.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.academyhomework.R
import com.example.academyhomework.entities.Movie
import com.google.android.material.imageview.ShapeableImageView

class MovieListAdapterMini:RecyclerView.Adapter<MovieListAdapterMini.ViewHolder>() {
    private var onClick: ((Int) -> Unit)? = null
    private var list = listOf<Movie>()
    fun bindMovies(m: List<Movie>){
        list = m
    }
    fun setOnContentClick(action: (Int) -> Unit){
        onClick = action
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private var image:ShapeableImageView = itemView.findViewById(R.id.iv_image_mini)

        fun bindData(movie: Movie, onClick: ((Int) -> Unit)?){
            image.load(movie.imageUrl){
                crossfade(true)
                placeholder(R.drawable.ic_loading_image)
            }
            itemView.setOnClickListener { onClick?.invoke(movie.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_movie_mini,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindData(list[position], onClick)
    }

    override fun getItemCount(): Int = list.size

}