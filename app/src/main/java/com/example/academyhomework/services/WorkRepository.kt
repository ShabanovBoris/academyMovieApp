package com.example.academyhomework.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.*
import java.util.concurrent.TimeUnit

internal class WorkRepository {

    private val constraints = Constraints.Builder()
        // .setRequiredNetworkType(NetworkType.UNMETERED) todo vpn troubles =(
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(true)
        .build()

    private val periodRequest =
        PeriodicWorkRequest.Builder(DbUpdateWorker::class.java, 8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag("DpUpdateService")
            .build()


    fun initWorkManagerWithPeriodWork(workManager: WorkManager): LiveData<WorkInfo> {
        if (workManager.getWorkInfosByTag("DpUpdateService")
                .get().isEmpty()
//                workManager.getWorkInfosByTag("DpUpdateService")
//                .get()[0].state != WorkInfo.State.ENQUEUED
        ) {
            workManager.enqueue(periodRequest)
            Log.d(
                "AcademyHomework",
                "new DpUpdateService was launched" + workManager.getWorkInfosByTag("DpUpdateService")
                    .get().toString() + workManager.getWorkInfosByTag("DpUpdateService").get().size
            )
        } else {
            Log.d(
                "AcademyHomework",
                """DpUpdateService info: size ${
                    workManager.getWorkInfosByTag("DpUpdateService").get().size
                }
                    | state : ${
                    workManager.getWorkInfosByTag("DpUpdateService")
                        .get()[0].state
                }
                    |${workManager.getWorkInfosByTag("DpUpdateService")}
                    |${workManager.getWorkInfoByIdLiveData(periodRequest.id)}
                """.trimMargin()
            )

        }
        return workManager.getWorkInfoByIdLiveData(periodRequest.id)
    }
}