package net.compose.leadandroiddevprep

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.compose.leadandroiddevprep.cart.worker.SyncScheduler

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Inject
    lateinit var syncScheduler: SyncScheduler

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = getWorkerManagerConfig()

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            syncScheduler.scheduleCartItemsSync()
        }
    }
}

private fun MainApplication.getWorkerManagerConfig(): Configuration {
    val isDebuggable = BuildConfig.DEBUG
    return if (isDebuggable) {
        Configuration.Builder().setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    } else {
        Configuration.Builder().setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.ERROR)
            .build()
    }
}
