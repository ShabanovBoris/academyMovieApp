package com.example.academyhomework

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
import com.example.academyhomework.presentation.BaseFragment
import com.example.academyhomework.presentation.details.FragmentMoviesDetails
import com.example.academyhomework.presentation.ViewModelFactory
import com.example.academyhomework.presentation.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Router {







    override var transitView: View? = null


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mainViewModel: MainViewModel by viewModels{ viewModelFactory }


    private var rootFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val component = (application as MovieApp).appComponent
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Details observer*/
        mainViewModel.details.observe(this, ::moveToDetails)

        if (savedInstanceState == null) {
            rootFragment = LaunchFragment.newInstance("","")
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerMainActivity, rootFragment as LaunchFragment)
                .commit()
            //deeplink handler
            intent?.let(::handleIntent)
        }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.data != null) {
            val id = intent.data!!.lastPathSegment?.toIntOrNull() ?: -1
            mainViewModel.loadDetails(id)
        }
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

    override fun openWebPage(movieId: Int): Boolean {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, "https://www.themoviedb.org/movie/${movieId}".toUri())
        browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return try {
            supportFragmentManager.popBackStack(DETAILS, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            startActivity(browserIntent)
            true
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            false
        }
    }

    // region onDestroy
    override fun onDestroy() {
        super.onDestroy()
        rootFragment = null
    }
//endregion

    companion object {
        const val DETAILS = "details"
    }
}