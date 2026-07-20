package net.compose.leadandroiddevprep.cart.worker

import android.content.Context
import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import net.compose.leadandroiddevprep.data.exception.safeApiCall
import net.compose.leadandroiddevprep.data.repository.ProductRepositoryOfflineFirstImpl
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.repository.ProductRepository
import net.compose.leadandroiddevprep.domain.repository.ProductRepositoryOfflineFirst
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

@HiltWorker
class CartItemsSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val repo: ProductRepositoryOfflineFirst
) : CoroutineWorker(context, workerParameters) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {

            val syncCartItemsResult = repo.syncCartItems()
            when (syncCartItemsResult) {
                is Resource.Success -> {
                    val result = repo.clearSyncItems()
                    when (result) {
                        is Resource.Success -> Result.success()
                        else -> {
                            Result.failure()
                        }
                    }
                }

                is Resource.Error -> Result.retry()
                else -> {
                    Result.failure()
                }
            }
        } catch (e: CancellationException) {
            Result.failure()
        }
    }
}