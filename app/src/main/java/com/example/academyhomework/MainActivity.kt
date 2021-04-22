package com.example.academyhomework

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.MovieDetails
import com.example.academyhomework.services.db_update.Notification
import com.example.academyhomework.services.db_update.NotificationsNewMovie
import com.example.academyhomework.viewmodel.ViewModelFactory
import com.example.academyhomework.viewmodel.ViewModelMovie
import java.util.concurrent.TimeUnit

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
            val id = intent.data!!.lastPathSegment?.toIntOrNull() ?: -1
            viewModel.loadDetails(id)}
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
            /**
             * SharedElement for animate [trasitView],
             * usually get from holder.itemView by recycler adapter
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
        if (rootFragment is FragmentMoviesDetails) {
            supportFragmentManager.beginTransaction()
                .remove(rootFragment as FragmentMoviesDetails)
                .commit()
        }
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