package com.example.academyhomework.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.domain.data.database.DataBaseRepository


class ViewModelFactory(
    private var applicationContext: Context
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {

        ViewModelMovie::class.java -> ViewModelMovie(
            dataBaseRepository = DataBaseRepository(applicationContext)
        )

        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}