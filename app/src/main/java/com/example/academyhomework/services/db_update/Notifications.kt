package com.example.academyhomework.services


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.academyhomework.MainActivity
import com.example.academyhomework.R
import com.example.academyhomework.domain.data.database.DataBaseRepository
import com.example.academyhomework.model.MovieDetails
import java.util.*

interface Notification {
    fun initialize()
    fun showNotification(movie: MovieDetails)
    fun dismissNotification(movieId: Int)
}


class NotificationsNewMovie(private val appContext: Context) : Notification {

    companion object {
        private const val CHANNEL_TAG = "channel_for_notification"
        private const val REQUEST_PENDING_CONTENT = 123
        private const val NOTIFICATION_TAG = "notify_tag"

    }

    private val notificationManager = NotificationManagerCompat.from(appContext)


    override fun initialize() {
        if (notificationManager.getNotificationChannel(CHANNEL_TAG) == null) {
            val channelNewMovie = NotificationChannelCompat.Builder(
                CHANNEL_TAG,
                NotificationManagerCompat.IMPORTANCE_HIGH
            )
                .setName("New movies notification")
                .setDescription("Show notification when appear new movies")
                .build()

            notificationManager.createNotificationChannel(channelNewMovie)
        }

    }

    override fun showNotification(movie: MovieDetails) {
        createNotification(movie)
    }

    override fun dismissNotification(movieId: Int) {
        notificationManager.cancel(NOTIFICATION_TAG, movieId)
    }

    suspend fun showFromDb(id: Int) {
        val movie = DataBaseRepository(appContext).getMovieDetails(id)
        movie?.let {
            val contentUri = "https://www.google.com/${movie.id}".toUri()
            val movieIntent = Intent(appContext, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                data = contentUri
            }
            val pendingIntentMovie = PendingIntent.getActivity(
                appContext,
                REQUEST_PENDING_CONTENT,
                movieIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationMovie = NotificationCompat.Builder(appContext, CHANNEL_TAG).apply {
                setContentTitle(movie.title)
                setContentText(movie.overview)
                setSmallIcon(R.drawable.ic_new)
                priority = NotificationManagerCompat.IMPORTANCE_HIGH
                setWhen(Date().time)
                setContentIntent(pendingIntentMovie)
            }.build()

            notificationManager.notify(NOTIFICATION_TAG, movie.id, notificationMovie)
        }
    }

    private fun createNotification(movie: MovieDetails) {
        val contentUri = "https://www.google.com/${movie.id}".toUri()
        val movieIntent = Intent(appContext, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = contentUri
        }
        val pendingIntentMovie = PendingIntent.getActivity(
            appContext,
            REQUEST_PENDING_CONTENT,
            movieIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        var bitmapImage: Bitmap

        Glide.with(appContext)
            .asBitmap()
            .load(movie.imageBackdrop)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapImage = resource


                    val notificationMovie =
                        NotificationCompat.Builder(appContext, CHANNEL_TAG).apply {
                            setContentTitle(movie.title)
                            setContentText(movie.overview)
                            setSmallIcon(IconCompat.createWithResource(appContext,R.drawable.ic_new))
                            priority = NotificationManagerCompat.IMPORTANCE_HIGH
                            setWhen(Date().time)
                            setContentIntent(pendingIntentMovie)
                            setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmapImage))
                        }.build()

                    notificationManager.notify(NOTIFICATION_TAG, movie.id, notificationMovie)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }


            })
    }
}

