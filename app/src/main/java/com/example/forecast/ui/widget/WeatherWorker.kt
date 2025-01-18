package com.example.forecast.ui.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.forecast.WeatherApplication
import kotlinx.coroutines.flow.first
import java.time.Duration

class WeatherWorker(
    private val context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(context, params) {
    companion object {
        private val uniqueWorkName = WeatherWorker::class.java.simpleName

        fun enqueue(context: Context, force: Boolean = false) {
            val manager = WorkManager.getInstance(context)
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val requestBuilder = PeriodicWorkRequestBuilder<WeatherWorker>(
                Duration.ofHours(1)
            ).setConstraints(constraints)

            var policy = ExistingPeriodicWorkPolicy.KEEP
            if (force) policy = ExistingPeriodicWorkPolicy.UPDATE

            manager.enqueueUniquePeriodicWork(uniqueWorkName, policy, requestBuilder.build())
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        }
    }

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(WeatherWidgetApp::class.java)
        val application = context.applicationContext as WeatherApplication
        val weatherAppRepository = application.container.weatherAppRepository
        val weatherAppPreferencesRepository = application.container.weatherAppPreferencesRepository

        val lat = weatherAppPreferencesRepository.localLatitude.first()
        val long = weatherAppPreferencesRepository.localLongitude.first()

        return try {
            setWidgetState(glanceIds, WeatherWidgetState.Loading)
            setWidgetState(
                glanceIds, WeatherWidgetState.Success(
                    weatherAppRepository.getCurrentWeather(lat, long)
                )
            )
            Result.success()
        } catch (e: Exception) {
            setWidgetState(glanceIds, WeatherWidgetState.Error(e.message.orEmpty()))
            if (runAttemptCount < 10) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private suspend fun setWidgetState(glanceIds: List<GlanceId>, newState: WeatherWidgetState) {
        glanceIds.forEach {
            updateAppWidgetState(context, WeatherWidgetStateDefinition, it) { newState }
            WeatherWidgetApp().updateAll(context)
        }
    }
}