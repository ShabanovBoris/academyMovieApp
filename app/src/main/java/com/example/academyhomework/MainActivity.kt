package com.example.academyhomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.viewmodel.ViewModelFactory
import com.example.academyhomework.viewmodel.ViewModelMovie
import java.io.Serializable

class MainActivity : AppCompatActivity(), Router {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel : ViewModelMovie

    var rootFragment:BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /** VIEWMODEL
         *
         */
        viewModelFactory = ViewModelFactory("arg")
        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewModelMovie::class.java)
        viewModel.loadMovieList()
          if(savedInstanceState == null)
        {
            /**
             *
             * FragmentMovieList
             *
             */
            rootFragment = FragmentMovieList.newInstance(viewModel)
         supportFragmentManager.beginTransaction()
             .addToBackStack(null)
             .replace(R.id.containerMainActivity,rootFragment as FragmentMovieList)
             .commit()
        }


    }
    override fun moveToDetails(movie:Serializable) {
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