package com.example.neoweather.domain.use_case

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.neoweather.data.workers.GetCurrentLocationWorker
import com.example.neoweather.data.workers.NotificationUtils
import java.util.concurrent.TimeUnit

class EnqueueWorkersUseCase(private val context: Context) {

    operator fun invoke(interval: Long, policy: ExistingPeriodicWorkPolicy, delay: Long? = null) {
        val getCurrentLocationWorker =
            PeriodicWorkRequestBuilder<GetCurrentLocationWorker>(
                interval,
                TimeUnit.HOURS
            )
                .setInitialDelay(delay ?: 0L, TimeUnit.HOURS)
                .addTag(NotificationUtils.GET_CURRENT_LOCATION_WORK_TAG)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                NotificationUtils.GET_CURRENT_LOCATION_WORK_NAME,
                policy,
                getCurrentLocationWorker
            )
    }
}