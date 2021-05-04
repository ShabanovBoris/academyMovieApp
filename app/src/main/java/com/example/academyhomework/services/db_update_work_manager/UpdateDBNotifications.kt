package com.example.academyhomework.services.db_update_work_manager


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import coil.imageLoader
import coil.request.ImageRequest
import com.example.academyhomework.MainActivity
import com.example.academyhomework.R
import com.example.academyhomework.domain.data.database.MovieDatabaseRepositry
import com.example.academyhomework.entities.MovieDetails
import java.util.*

interface Notification {
    fun initialize()
    fun showNotification(movie: MovieDetails)}


class NotificationsNewMovie(private val appContext: Context) : Notification {

    companion object {
        private const val CHANNEL_TAG = "channel_for_notification"
        private const val REQUEST_PENDING_CONTENT = 123
        private const val NOTIFICATION_TAG = "notify_tag"
        fun dismissNotification(appContext: Context,movieId: Int) {
            NotificationManagerCompat.from(appContext).cancel(NOTIFICATION_TAG, movieId)
        }
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



    suspend fun showFromDb(id: Int) {
        val movie = MovieDatabaseRepositry(appContext).getMovieDetails(id)
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

        val request = ImageRequest.Builder(appContext)
            .data(movie?.imageBackdrop)
            .target(
                onStart = { placeholder ->
                    // Handle the placeholder drawable.
                },
                onSuccess = { result ->
                    // Handle the successful result.
                    bitmapImage = result.toBitmap()


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
                },
                onError = { error ->
                    // Handle the error drawable.
                }
            )
            .build()
        appContext.imageLoader.enqueue(request)
    }
}

