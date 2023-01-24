package com.example.neoweather.domain.use_case

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.data.workers.QUEUE_HANDLER_WORK_NAME
import com.example.neoweather.data.workers.QueueHandlerWorker
import java.util.concurrent.TimeUnit

class ScheduleQueueHandlerUseCase(
    private val workManager: WorkManager,
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(policy: ExistingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP) {
        val repeatInterval =
            preferencesRepository.dataPreferences.updateInterval

        val queueHandlerWorker =
            PeriodicWorkRequestBuilder<QueueHandlerWorker>(
                repeatInterval,
                TimeUnit.MINUTES
            )
                .build()

        workManager.enqueueUniquePeriodicWork(
            QUEUE_HANDLER_WORK_NAME,
            policy,
            queueHandlerWorker
        )
    }
}