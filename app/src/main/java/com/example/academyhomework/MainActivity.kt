package com.example.academyhomework

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.di.ContextModule
import com.example.academyhomework.di.DaggerApplicationComponent
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
import com.example.academyhomework.presentation.BaseFragment
import com.example.academyhomework.presentation.FragmentMovieList
import com.example.academyhomework.presentation.FragmentMoviesDetails
import com.example.academyhomework.viewmodels.MainViewModelFactory
import com.example.academyhomework.viewmodels.MainViewModelMovie
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Router {

   override var transitView: View? = null


    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var mainViewModel: MainViewModelMovie



    private var rootFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val component = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()

        component.inject(this)

        createViewModel()

        if (savedInstanceState == null) {
            /**
             *
             * FragmentMovieList
             *
             */
            mainViewModel.loadMovieCache()
            mainViewModel.loadMovieList()
            rootFragment = FragmentMovieList.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerMainActivity, rootFragment as FragmentMovieList)
                .commit()

            intent?.let(::handleIntent)
        }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.data != null) {
            val id = intent.data!!.lastPathSegment?.toIntOrNull() ?: -1
            mainViewModel.loadDetails(id)}
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            val list = mainViewModel.movieList.value
            val movie: Movie?
            list?.let {
                movie = list.find {
                    it.id == intent.data?.lastPathSegment?.toInt()
                }
                movie?.let {
                    mainViewModel.loadDetails(movie.id)
                }
            }
        }
        super.onNewIntent(intent)
    }


    private fun createViewModel() {

        mainViewModel = ViewModelProvider(viewModelStore, mainViewModelFactory).get(MainViewModelMovie::class.java)


        /** Details observer*/
        mainViewModel.details.observe(this, ::moveToDetails)


    }

    override fun moveToDetails(movie: MovieDetails) {

        rootFragment = FragmentMoviesDetails.newInstance(movie)

        supportFragmentManager.popBackStack(DETAILS, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction().apply {
            /**
             * SharedElement for animate [trasitView],
             * usually given from holder.itemView by recycler adapter
             */
            transitView?.let {
                addSharedElement(
                    transitView!!,
                    getString(R.string.DetailsTransitionName)
                )
            }

            addToBackStack(DETAILS)
            replace(R.id.containerMainActivity, rootFragment as FragmentMoviesDetails)
            commit()
        }
    }


    override fun backFromDetails() {
        onBackPressed()
//        if (rootFragment is FragmentMoviesDetails) {
//            supportFragmentManager.beginTransaction()
//                .remove(rootFragment as FragmentMoviesDetails)
//                .commit()
//        }
    }

    override fun openWebPage(movieId: Int):Boolean {
        val browserIntent = Intent(Intent.ACTION_VIEW, "https://www.themoviedb.org/movie/${movieId}".toUri())
        browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return try {
            supportFragmentManager.popBackStack(DETAILS, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            startActivity(browserIntent)
            true
        } catch (e: ActivityNotFoundException) {
            false
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