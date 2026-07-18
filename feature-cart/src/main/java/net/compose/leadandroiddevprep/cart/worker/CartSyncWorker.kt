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
import kotlinx.coroutines.withContext
import net.compose.leadandroiddevprep.domain.repository.CartRepository
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

@HiltWorker
class CartItemsSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val cartRepository: CartRepository
) : CoroutineWorker(context, workerParameters) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val pendingItems = cartRepository.getPendingSyncItems()
            if (pendingItems.isNullOrEmpty())
                return@withContext Result.success()
            val success = cartRepository.syncCartItems(pendingItems)
            if (success) Result.success()
            else Result.failure()

        } catch (e: HttpException) {
            Result.retry()
        } catch (e: IOException) {
            Result.failure()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Result.failure()
        }
    }
}