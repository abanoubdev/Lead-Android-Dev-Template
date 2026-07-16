package net.compose.leadandroiddevprep.products.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.compose.leadandroiddevprep.data.exception.DomainException
import net.compose.leadandroiddevprep.data.exception.toDomainException
import net.compose.leadandroiddevprep.domain.Resource
import net.compose.leadandroiddevprep.domain.model.Product
import net.compose.leadandroiddevprep.domain.repository.ProductRepositoryOfflineFirst
import net.compose.leadandroiddevprep.products.R
import javax.inject.Inject

sealed interface ProductIntent {
    data object Retry : ProductIntent
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductsOfflineFirstViewModel @Inject constructor(
    private val repository: ProductRepositoryOfflineFirst
) : ViewModel() {

    private val retryTrigger = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val intentFlow = MutableSharedFlow<ProductIntent>()

    val productsState: StateFlow<ProductListUiState> = retryTrigger
        .flatMapLatest {
            repository.getProducts()
                .scan<Resource<List<Product>>, ProductListUiState>(ProductListUiState.Loading) { currentState, resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (currentState is ProductListUiState.Success && currentState.products.isNotEmpty()) {
                                currentState
                            } else {
                                ProductListUiState.Loading
                            }
                        }

                        is Resource.Success -> {
                            ProductListUiState.Success(
                                products = resource.data, null
                            )
                        }

                        is Resource.Error -> {
                            if ((currentState is ProductListUiState.Success && currentState.products.isNotEmpty())) {
                                ProductListUiState.Success(
                                    currentState.products,
                                    mapDomainExceptionToStringRes(resource.exception.toDomainException())
                                )
                            } else {
                                val errorRes =
                                    mapDomainExceptionToStringRes(resource.exception as? DomainException)
                                ProductListUiState.Error(errorRes)
                            }
                        }

                        is Resource.Empty -> {
                            if ((currentState is ProductListUiState.Success && currentState.products.isNotEmpty()) || currentState is ProductListUiState.Loading) {
                                currentState
                            } else {
                                ProductListUiState.Empty
                            }
                        }

                        else -> {
                            throw IllegalArgumentException("Unknown Resource type: $resource")
                        }
                    }
                }
                .catch { throwable ->
                    emit(ProductListUiState.Error(mapDomainExceptionToStringRes(throwable.toDomainException())))
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductListUiState.Loading
        )

    init {
        retryTrigger.tryEmit(Unit)
        handleIntents()
    }

    fun processIntent(intent: ProductIntent) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentFlow.collect { intent ->
                when (intent) {
                    is ProductIntent.Retry -> {
                        retryTrigger.emit(Unit)
                    }
                }
            }
        }
    }

    private fun mapDomainExceptionToStringRes(exception: DomainException?): Int {
        return when (exception) {
            is DomainException.Network -> R.string.network_error_title
            is DomainException.Unauthorized -> R.string.unauthorized_error
            is DomainException.NotFound -> R.string.not_found_error
            is DomainException.Server -> R.string.server_error_title
            is DomainException.Unknown, null -> R.string.unknown_error_title
        }
    }
}