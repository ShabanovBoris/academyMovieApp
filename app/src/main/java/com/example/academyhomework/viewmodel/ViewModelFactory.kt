package com.example.academyhomework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelFactory(
    private var arg: String
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        ViewModelMovie::class.java -> ViewModelMovie(arg) //args here
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}