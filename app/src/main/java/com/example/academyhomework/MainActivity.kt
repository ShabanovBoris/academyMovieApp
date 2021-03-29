package com.example.academyhomework

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.MovieDetails
import com.example.academyhomework.viewmodel.ViewModelFactory
import com.example.academyhomework.viewmodel.ViewModelMovie

class MainActivity : AppCompatActivity(), Router {

    override var transitView: View? = null
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ViewModelMovie



    private var rootFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createViewModel()

        if (savedInstanceState == null) {
            /**
             *
             * FragmentMovieList
             *
             */
            viewModel.loadMovieCache()
            viewModel.loadMovieList()
            rootFragment = FragmentMovieList.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerMainActivity, rootFragment as FragmentMovieList)
                .commit()

            intent?.let(::handleIntent)
        }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.data != null) {
            viewModel.loadDetails(intent.data!!.lastPathSegment?.toIntOrNull() ?: -1)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            val list = viewModel.movieList.value
            val movie: Movie?
            list?.let {
                movie = list.find {
                    it.id == intent?.data?.lastPathSegment?.toInt()
                }
                movie?.let {
                    viewModel.loadDetails(movie.id)
                }
            }
        }
        super.onNewIntent(intent)
    }


    private fun createViewModel() {
        viewModelFactory = ViewModelFactory(applicationContext = applicationContext)
        viewModel =
            ViewModelProvider(this.viewModelStore, viewModelFactory).get(ViewModelMovie::class.java)

        /** Details observer*/
        viewModel.details.observe(this, ::moveToDetails)

    }

    override fun moveToDetails(movie: MovieDetails) {

        rootFragment = FragmentMoviesDetails.newInstance(movie)

        supportFragmentManager.popBackStack(DETAILS, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction().apply {
            addSharedElement(
                transitView!!,
                getString(R.string.DetailsTransitionName)
            )
            addToBackStack(DETAILS)
            replace(R.id.containerMainActivity, rootFragment as FragmentMoviesDetails)
            commit()
        }
    }


    override fun backFromDetails() {
        if (rootFragment is FragmentMoviesDetails) {
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

companion object{
    const val DETAILS = "details"
}
}