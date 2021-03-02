package com.example.academyhomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.viewmodel.ViewModelFactory
import com.example.academyhomework.viewmodel.ViewModelMovie
import java.io.Serializable

class MainActivity : AppCompatActivity(), Router {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel : ViewModelMovie

    var fragment:FragmentMoviesDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModelFactory = ViewModelFactory("arg")
        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewModelMovie::class.java)
        viewModel.loadMovieList(this.applicationContext)
          if(savedInstanceState == null)
        {
         supportFragmentManager.beginTransaction()
             .addToBackStack(null)
             .replace(R.id.containerMainActivity,FragmentMovieList.newInstance(viewModel))
             .commit()
        }


    }
    override fun moveToDetails(movie:Serializable) {
        fragment = FragmentMoviesDetails.newInstance(movie)
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            add(R.id.containerMainActivity,fragment!!)
            commit()
        }
    }
    override fun backFromDetails() {
        supportFragmentManager.beginTransaction()
            .remove(fragment!!)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        fragment = null
    }

}