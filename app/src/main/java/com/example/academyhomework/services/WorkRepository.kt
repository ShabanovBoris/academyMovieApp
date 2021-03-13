package com.example.academyhomework.services

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

internal class WorkRepository {

private val constraints = Constraints.Builder()
   // .setRequiredNetworkType(NetworkType.UNMETERED) todo vpn troubles =(
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresCharging(true)
    .build()

    val periodRequest = PeriodicWorkRequest.Builder(DbUpdateWorker::class.java,5,TimeUnit.SECONDS)
        .setConstraints(constraints)
        .addTag("DpUpdateService")
        .build()
}