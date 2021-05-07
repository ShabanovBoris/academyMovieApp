package com.example.academyhomework.services.schedule_movie_work_manager

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.academyhomework.utils.DatePickerFragment
import com.example.academyhomework.utils.TimePickerFragment
import java.util.*
import java.util.concurrent.TimeUnit


class ScheduleMovieWorkRepository(
    private val appContext: Context,
    private val movieId: Int,
    private val time: TimePickerFragment,
    private val date: DatePickerFragment
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun start() {
        val currentCalendar = Calendar.getInstance()

        val customCalendar = Calendar.Builder()
            .setDate(date.years, date.months, date.days)
            .setTimeOfDay(time.hour, time.minutes, 0)
            .build()

        val time = "Schedule movie on ${date.days}/${date.months} ${time.hour}:${time.minutes}"
        Toast.makeText(appContext, time, Toast.LENGTH_LONG).show()
        Log.d(LOG_TAG_SCHEDULER, "minute before schedule ${(customCalendar.timeInMillis - currentCalendar.timeInMillis)} millis")

        val customTime = customCalendar.timeInMillis
        val currentTime = currentCalendar.timeInMillis
        if (customTime > currentTime) {
            val data = Data.Builder().putInt( ScheduleNotificationWorker.MOVIE_ID, movieId).build() /** data*/
            val delay = customTime - currentTime

            /** call the schedule notification function
             * put the value of [Data] class with [movie_id]
             * and [Date]
             * */
            scheduleNotification(delay, data)
        }
    }
    private fun scheduleNotification(delay: Long, data: Data) {
        /** create [OneTimeWorkRequest] with delay
         * and [movie_id] as [DataClass]
         * */
        val notificationWork = OneTimeWorkRequest.Builder(ScheduleNotificationWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()


        /** create workManager instance and put here [OneTimeWorkRequest]
         * launch with [enqueue]
         */
        val instanceWorkManager = WorkManager.getInstance(appContext)
        instanceWorkManager.beginUniqueWork(
            ScheduleNotificationWorker.NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE,
            notificationWork
        ).enqueue()
    }

    companion object{
        const val LOG_TAG_SCHEDULER = "AcademyScheduler"
    }
}