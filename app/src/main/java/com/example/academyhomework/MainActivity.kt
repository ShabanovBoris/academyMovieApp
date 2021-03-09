package com.example.academyhomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.model.MovieDetails
import com.example.academyhomework.viewmodel.ViewModelFactory
import com.example.academyhomework.viewmodel.ViewModelMovie

class MainActivity : AppCompatActivity(), Router {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel : ViewModelMovie

    var rootFragment:BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createViewModel()

        if(savedInstanceState == null)
        {
            /**
             *
             * FragmentMovieList
             *
             */
            viewModel.loadMovieCache()
            viewModel.loadMovieList()
            rootFragment = FragmentMovieList.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerMainActivity,rootFragment as FragmentMovieList)
                .commit()
        }
    }


    private fun createViewModel() {
        viewModelFactory = ViewModelFactory(applicationContext =  applicationContext)
        viewModel = ViewModelProvider(this.viewModelStore, viewModelFactory).get(ViewModelMovie::class.java)

        /** Details observer*/
        viewModel.details.observe(this,this::moveToDetails)

    }

    override fun moveToDetails(movie: MovieDetails) {
       rootFragment = FragmentMoviesDetails.newInstance(movie)
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            add(R.id.containerMainActivity,rootFragment as FragmentMoviesDetails)
            commit()
        }
    }


    override fun backFromDetails() {
        if(rootFragment is FragmentMoviesDetails) {
            supportFragmentManager.beginTransaction()
                .remove(rootFragment as FragmentMoviesDetails)
                .commit()
        }
    }
    // region onDestroy
    override fun onDestroy() {
        super.onDestroy()
        rootFragment = null
    }
//endregion



}