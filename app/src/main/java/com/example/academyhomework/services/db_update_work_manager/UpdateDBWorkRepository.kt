package com.example.academyhomework.services.db_update_work_manager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.*
import java.util.concurrent.TimeUnit

internal class UpdateDBWorkRepository {

    companion object{
        const val TAG_SERVICE = "DpUpdateService"
        const val TAG_LOG = "AcademyHomework"
    }

    private val constraints = Constraints.Builder()
        // .setRequiredNetworkType(NetworkType.UNMETERED) todo vpn troubles =(
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(true)
        .build()

    private val periodRequest =
        PeriodicWorkRequest.Builder(UpdateDBWorker::class.java, 8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(TAG_SERVICE)
            .build()


    fun initWorkManagerWithPeriodWork(workManager: WorkManager): LiveData<WorkInfo> {
        if (workManager.getWorkInfosByTag(TAG_SERVICE)
                .get().isEmpty()
//                workManager.getWorkInfosByTag("DpUpdateService")
//                .get()[0].state != WorkInfo.State.ENQUEUED
        ) {
            workManager.enqueue(periodRequest)
            Log.d(
                TAG_LOG,
                "new DpUpdateService was launched" + workManager.getWorkInfosByTag(TAG_SERVICE)
                    .get().toString() + workManager.getWorkInfosByTag(TAG_SERVICE).get().size
            )
        } else {
            Log.d(
                TAG_LOG,
                """DpUpdateService info: size ${
                    workManager.getWorkInfosByTag(TAG_SERVICE).get().size
                }
                    | state : ${
                    workManager.getWorkInfosByTag(TAG_SERVICE)
                        .get()[0].state
                }
                    |${workManager.getWorkInfosByTag(TAG_SERVICE)}
                    |${workManager.getWorkInfoByIdLiveData(periodRequest.id)}
                """.trimMargin()
            )

        }
        return workManager.getWorkInfoByIdLiveData(periodRequest.id)
    }
}