package com.example.academyhomework.view.adapters

import android.view.View

class SearchListAdapter(private val onClick: (Int, View) -> Unit): MovieListAdapter(onClick) {

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return DEFAULT
    }
}