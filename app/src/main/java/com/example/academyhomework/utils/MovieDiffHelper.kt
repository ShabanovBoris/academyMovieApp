package com.example.academyhomework.utils

import android.util.Log
import com.example.academyhomework.entities.Movie

object MovieDiffHelper {

    fun getDiff(movie1: List<Movie>, movie2: List<Movie>): Relevance {
        val m1: List<Int> = movie1.sortedBy { it.popularity }.map { it.id }
        Log.d("AcademyHomework=====Network", "$m1")
        val m2: List<Int> = movie2.sortedBy { it.popularity }.map { it.id }
        Log.d("AcademyHomework=====DB", "$m2")
        var diff = m1.filterNot { m2.contains(it) }
        if (diff.isEmpty()) {
            diff = m2.filterNot { m1.contains(it) }
        }
        return if (diff.isNotEmpty()) {
            Relevance.StaleData(diff)
        } else {
            Relevance.FreshData
        }
    }

    sealed class Relevance {
        data class StaleData(val newListIndies: List<Int>) : Relevance()
        object FreshData : Relevance()
    }

}