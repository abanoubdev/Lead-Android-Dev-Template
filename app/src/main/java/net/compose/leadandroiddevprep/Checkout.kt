package net.compose.leadandroiddevprep

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

//sealed interface CheckoutSideEffect {
//    data class ShowSnackbar(val message: String) : CheckoutSideEffect
//}
//
//sealed interface CheckoutUiState {
//    data object Loading : CheckoutUiState
//    data class Success(val message: Int?) : CheckoutUiState
//    data class Error(val message: String) : CheckoutUiState
//}
//
//@HiltViewModel
//class CheckoutViewModel @Inject constructor(
//    private val repo: CheckoutRepo,
//) : ViewModel() {
//
//    private val _payState: MutableStateFlow<CheckoutUiState?> =
//        MutableStateFlow(CheckoutUiState.Loading)
//    val payState = _payState.asStateFlow()
//
//    private val _sideEffectChannel = Channel<CheckoutSideEffect>()
//    val sideEffectFlow = _sideEffectChannel.receiveAsFlow()
//
//    fun onPayClicked(transaction: Transaction) {
//
//        viewModelScope.launch {
//            try {
//                repo.processCharge(transaction).collect { repoResult ->
//                    when (repoResult) {
//                        is CheckoutResource.Success -> {
//                            if (repoResult.message == null) {
//                                _payState.value = CheckoutUiState.Success(R.string.checkout_sucecss)
//                            } else {
//                                _payState.value = CheckoutUiState.Success(repoResult.message)
//                            }
//                        }
//
//                        is CheckoutResource.Error -> {
//                            _payState.value =
//                                CheckoutUiState.Error(repoResult.exception.message.toString())
//                            repoResult.exception.message?.let {
//                                _sideEffectChannel.send(CheckoutSideEffect.ShowSnackbar(it))
//                            }
//                        }
//
//                        is CheckoutResource.Loading -> {
//                            _payState.value = CheckoutUiState.Loading
//                        }
//
//                        else -> {
//                            throw Exception("Unknown State")
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                e.message?.let {
//                    _sideEffectChannel.send(CheckoutSideEffect.ShowSnackbar(it))
//                }
//            }
//        }
//    }
//}
//
//interface CheckoutRepo {
//    fun processCharge(transaction: Transaction): Flow<CheckoutResource<Boolean?>>
//    suspend fun PendingCheckoutItems(): List<Transaction>
//}
//
//class CheckoutRepoImpl @Inject constructor(
//    private val apiService: SumUpApiService,
//    private val localDb: TransactionDao
//) : CheckoutRepo {
//
//    override fun processCharge(transaction: Transaction?) = flow {
//
//        emit(CheckoutResource.Loading)
//
//        if (transaction == null || transaction.amount < 0) {
//            emit(CheckoutResource.Error(Exception("Invalid amount")))
//            return@flow
//        }
//
//        try {
//            val result = apiService.processCharge(transaction)
//            if (result) {
//                val dbResult = localDb.saveTransaction(transaction.copy(status = "SYNCED"))
//                if (dbResult > 0) {
//                    emit(CheckoutResource.Success(true, null))
//                } else {
//                    emit(
//                        CheckoutResource.Success(
//                            null,
//                            R.string.transaction_db_save_message
//                        )
//                    )
//                }
//            } else {
//                val dbResult = localDb.saveTransaction(transaction.copy(status = "PENDING"))
//                if (dbResult > 0) {
//                    emit(CheckoutResource.Success(null, R.string.transaction_db_save_message))
//                } else {
//                    emit(CheckoutResource.Error(Exception("Something went wrong")))
//                }
//            }
//        } catch (e: Exception) {
//            if (e is CancellationException)
//                throw e
//            emit(CheckoutResource.Error(e))
//        }
//    }
//
//    override suspend fun PendingCheckoutItems(): List<Transaction> {
//        return localDb.getPendingTransactions()
//    }
//}
//
//sealed interface CheckoutResource<out T> {
//    data object Loading : CheckoutResource<Nothing>
//    data class Success<T>(val data: T, val message: Int?) : CheckoutResource<T>
//    data class Error(val exception: Throwable) : CheckoutResource<Nothing>
//}
//
//interface SumUpApiService {
//    fun processCharge(transaction: Transaction): Boolean
//}
//
//@Module
//@InstallIn(SingletonComponent::class)
//interface CheckoutRepositoryModule {
//
//    @Singleton
//    @Binds
//    fun bindCheckoutRepository(repositoryImpl: CheckoutRepoImpl): CheckoutRepo
//}
//
//
//@HiltWorker
//class CheckoutSyncWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted workerParameters: WorkerParameters,
//    val repo: CheckoutRepo
//) : CoroutineWorker(context, workerParameters) {
//
//    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        try {
//            val localItemsResult = repo.PendingCheckoutItems()
//            if (localItemsResult.isNotEmpty()) {
//                val syncCartItemsResult = repo.syncCheckoutItems(localItemsResult)
//                when (syncCartItemsResult) {
//                    is Resource.Success -> {
//                        val result = repo.clearSyncItems()
//                        when (result) {
//                            is Resource.Success -> Result.success()
//                            else -> {
//                                Result.failure()
//                            }
//                        }
//                    }
//
//                    is Resource.Error -> Result.retry()
//                    else -> {
//                        Result.failure()
//                    }
//                }
//            } else {
//                Result.success()
//            }
//
//        } catch (e: CancellationException) {
//            throw e
//        }
//    }
//}
//
//@Singleton
//class CheckoutSyncScheduler @Inject constructor(@ApplicationContext val context: Context) {
//
//    fun scheduleCheckoutItemsSync() {
//        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
//            .setRequiresBatteryNotLow(true).build()
//
//        val periodicSyncRequest =
//            PeriodicWorkRequestBuilder<CheckoutSyncWorker>(15, TimeUnit.MINUTES)
//                .setConstraints(constraints).build()
//
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//            "CheckoutSyncWorker",
//            ExistingPeriodicWorkPolicy.KEEP,
//            periodicSyncRequest
//        )
//    }
//
//    fun triggerImmediateSync() {
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        val oneTimeRequest = OneTimeWorkRequestBuilder<CheckoutSyncWorker>()
//            .setConstraints(constraints)
//            .build()
//
//        WorkManager.getInstance(context).enqueueUniqueWork(
//            "ImmediateCheckoutSyncWorker",
//            ExistingWorkPolicy.REPLACE,
//            oneTimeRequest
//        )
//    }
//}