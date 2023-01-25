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

    operator fun invoke(update: Boolean = false) {

        val repeatInterval = preferencesRepository.dataPreferences.updateInterval

        val queueHandlerWorker =
            PeriodicWorkRequestBuilder<QueueHandlerWorker>(
                repeatInterval,
                TimeUnit.HOURS
            ).build()

        if (update) {
            workManager.updateWork(queueHandlerWorker)
            return
        }

        workManager.enqueueUniquePeriodicWork(
            QUEUE_HANDLER_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            queueHandlerWorker
        )
    }
}