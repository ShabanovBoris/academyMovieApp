package com.example.academyhomework.model.movielist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.academyhomework.R
import com.example.academyhomework.model.Movie
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MovieListAdapter(val onClick: (Int) -> Unit):ListAdapter<Movie, MovieListAdapter.ViewHolderMovie>(
    DiffCallback()
) {



     class ViewHolderMovie(view:View):RecyclerView.ViewHolder(view){
        private val title:TextView = view.findViewById(R.id.tv_title_movie)
        private val genre:TextView = view.findViewById(R.id.tv_genre)
        private val image:ImageView = view.findViewById(R.id.iv_image_card)
        private val rating:RatingBar = view.findViewById(R.id.ratingBarCard)
        private val release:TextView = view.findViewById(R.id.tv_release)


        fun bindData(movie: Movie){
            image.load(movie.imageUrl){
                crossfade(true)
                placeholder(R.drawable.ic_loading_image)
            }



            title.text = movie.title

            genre.text=""
            for(g in movie.genres){
                genre.append(g.name+" ")
            }
            rating.rating = movie.rating.toFloat()
            release.text = movie.releaseDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMovie {
        return ViewHolderMovie(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie,
        parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderMovie, position: Int) {
            holder.bindData(getItem(position))
            holder.itemView.setOnClickListener{
                onClick(getItem(position).id)
            }


    }
}

class DiffCallback: DiffUtil.ItemCallback<Movie>(){
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

}