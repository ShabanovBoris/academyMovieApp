package com.example.academyhomework.utils

import android.util.Log
import androidx.work.WorkInfo
import com.example.academyhomework.view.MainViewModel

object WorkManagerHelper {

    fun statesHandler(workInfo: WorkInfo): Boolean =
        when (workInfo.state) {
            WorkInfo.State.RUNNING -> {
                Log.d(MainViewModel.TAG, "WorkInfo.State.RUNNING")
                false
            }
            WorkInfo.State.ENQUEUED -> {
                Log.d(MainViewModel.TAG, "WorkInfo.State.ENQUEUED")
                true
            }
            WorkInfo.State.FAILED -> {
                Log.d(MainViewModel.TAG, "WorkInfo.State.FAILED")
                false
            }
            else -> {
                Log.e(MainViewModel.TAG, "WorkInfo.State.ELSE BRANCH")
                false
            }
        }
}

