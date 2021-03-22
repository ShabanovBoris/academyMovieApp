package com.example.academyhomework.services.schedule_watch

import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.academyhomework.MainActivity
import com.example.academyhomework.R
import com.example.academyhomework.domain.data.database.DataBaseRepository
import com.example.academyhomework.model.MovieDetails
import kotlinx.coroutines.*
import java.util.*

class NotifyWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {
    companion object {
        const val MOVIE_ID = "appName_notification_id"
        const val NOTIFICATION_CHANNEL_TAG = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
        const val REQUEST_PENDING_CONTENT = 123
        const val TAG = "AcademyScheduler"
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun doWork(): Result {
        scope.launch {
            Log.d(TAG, "doWork: Scheduler ")
            val id = inputData.getInt(MOVIE_ID, 0)
            initChannel()
            sendNotification(id)




        }
        return Result.success()

    }

    private suspend fun sendNotification(id: Int) {
        val contentUri = "https://www.google.com/${id}".toUri()

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = contentUri

        }
        val pendingIntent = getActivity(
            applicationContext,
            REQUEST_PENDING_CONTENT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        val bitmap =
            AppCompatResources.getDrawable(applicationContext, R.drawable.ic_baseline_schedule_24)
                ?.toBitmap()


            val movie = getMovie(id)
            val titleNotification = movie?.title
            val subtitleNotification = "Don't forget to watch =)"
            var bitmapImage : Bitmap

            Glide.with(applicationContext)
                .asBitmap()
                .load(movie?.imageBackdrop)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bitmapImage = resource


                        val notification =
                            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_TAG)
                                .apply {
                                    setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_baseline_schedule_24)
                                    setContentTitle(titleNotification).setContentText(
                                        subtitleNotification
                                    )
                                    setWhen(Date().time)
                                    setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmapImage))
                                    setDefaults(DEFAULT_ALL)
                                    setContentIntent(pendingIntent)
                                    setAutoCancel(true)
                                    priority = PRIORITY_MAX
                                }.build()


                        notificationManager.notify(id, notification)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
            Log.d(TAG, "sendNotification: notification has appear")


    }


    private fun initChannel() {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_TAG) == null) {

            val channel = NotificationChannelCompat.Builder(
                NOTIFICATION_CHANNEL_TAG,
                IMPORTANCE_HIGH
            )
                .setName("Scheduler notification")
                .setDescription("schedule movie to watch later")
                .build()

            notificationManager.createNotificationChannel(channel)

        }
    }

    private suspend fun getMovie(id: Int): MovieDetails? {
        val repository = DataBaseRepository(applicationContext)
        return repository.getMovieDetails(id)
    }

}