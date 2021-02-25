package com.example.academyhomework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academyhomework.R
import com.example.academyhomework.models.Actor
import com.example.academyhomework.models.Movie
import com.google.android.material.imageview.ShapeableImageView

class ActorRecyclerAdapter:RecyclerView.Adapter<ActorRecyclerAdapter.ViewHolderDataActor>() {

    private var list = listOf<Actor>()
    private var movie: Movie? = null
    fun bindActors(l: List<Actor>){
        list = l
    }
    class ViewHolderDataActor(itemView: View):RecyclerView.ViewHolder(itemView){
        private var avatar:ShapeableImageView = itemView.findViewById(R.id.iv_avatar)
        private var name:TextView = itemView.findViewById(R.id.tv_actor_fullname)

        fun bindData(actor: Actor){
            name.text = actor.name
            Glide.with(itemView.context)
                .load(actor.imageUrl)
                .into(avatar)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDataActor {
       return ViewHolderDataActor(LayoutInflater.from(parent.context)
           .inflate(R.layout.view_holder_actor,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderDataActor, position: Int) {

        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

}