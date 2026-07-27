package net.compose.leadandroiddevprep.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

data class Transaction(val id: String, val amount: Double)

@Composable
fun MerchantDashboard(
    viewModel: MerchantDashboardViewModel = hiltViewModel()
) {

    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    when (transactions) {
        is MerchantResource.Success -> {
            Column {
                LiveClock()
                TransactionHistory(
                    transactions = (transactions as MerchantResource.Success).data.toPersistentList(),
                    onTransactionClick = {
                        viewModel.handleTransaction(it)
                    }
                )
            }
        }

        is MerchantResource.Error -> {
            Text(text = "Error: ${(transactions as MerchantResource.Error).exception.message}")
        }

        is MerchantResource.Empty -> {
            Text(text = "No transactions available")
        }

        is MerchantResource.Loading -> {
            Text(text = "Loading...")
        }
    }
}

@Composable
fun LiveClock() {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000.milliseconds)
            currentTime = System.currentTimeMillis()
        }
    }

    Text(text = "Current Time: $currentTime")
}

@Composable
fun TransactionHistory(
    transactions: PersistentList<Transaction>,
    onTransactionClick: (String) -> Unit
) {

    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(transactions, key = { it.id }) { tx ->
            TransactionItem(tx, onTransactionClick)
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onTransactionClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onTransactionClick(transaction.id) }
    ) {
        Text(text = "Transaction ID: ${transaction.id}")
    }
}


object TransactionGenerator {

    fun createSampleTransactions(
        count: Int = 20,
        minAmount: Double = 1.0,
        maxAmount: Double = 1000.0
    ): List<Transaction> {
        return List(count) {
            Transaction(
                id = UUID.randomUUID().toString(),
                amount = Random.nextDouble(minAmount, maxAmount)
            )
        }
    }
}

@HiltViewModel
class MerchantDashboardViewModel @Inject constructor() : ViewModel() {

    private val _transactions: MutableStateFlow<MerchantResource<PersistentList<Transaction>>> =
        MutableStateFlow(MerchantResource.Loading)
    val transactions: StateFlow<MerchantResource<PersistentList<Transaction>>> =
        _transactions.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000.milliseconds)
            val sampleTransactions = TransactionGenerator.createSampleTransactions()
            if (sampleTransactions.isNotEmpty()) {
                _transactions.value =
                    MerchantResource.Success(sampleTransactions.toPersistentList())
            } else {
                _transactions.value = MerchantResource.Empty()
            }
        }
    }

    fun handleTransaction(id: String) {
        Log.d("MerchantDashboardViewModel", "Handling transaction with ID: $id")
    }
}

sealed interface MerchantResource<out T> {
    data class Success<T>(val data: T) : MerchantResource<T>
    data class Error(val exception: Throwable) : MerchantResource<Nothing>
    data class Empty(val showEmpty: Boolean = false) : MerchantResource<Nothing>
    data object Loading : MerchantResource<Nothing>
}