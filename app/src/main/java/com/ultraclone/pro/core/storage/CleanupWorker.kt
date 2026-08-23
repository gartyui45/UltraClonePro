package com.ultraclone.pro.core.storage

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class CleanupWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        StorageManager(applicationContext).let { /* clean cache */ }
        return Result.success()
    }
}
