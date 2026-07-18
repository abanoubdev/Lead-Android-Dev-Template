package net.compose.leadandroiddevprep.products.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.compose.leadandroiddevprep.data.exception.DomainException
import net.compose.leadandroiddevprep.data.exception.toDomainException
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.model.Product
import net.compose.leadandroiddevprep.domain.repository.ProductRepository
import net.compose.leadandroiddevprep.products.R

sealed interface ProductIntent2 {
    data object Retry : ProductIntent2
    data object FloatButtonClicked : ProductIntent2
    data class ProductClicked(val product: Product) : ProductIntent2

}

sealed interface ProductSideEffect {
    data class NavigateToDetails(val product: Product) : ProductSideEffect
}

@HiltViewModel
class ProductsViewModel @Inject constructor(val repository: ProductRepository) : ViewModel() {

    private val _productsState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val productsState: StateFlow<ProductListUiState> = _productsState

    private val intentFlow = MutableSharedFlow<ProductIntent2>()

    private val _sideEffectChannel = Channel<ProductSideEffect>()
    val sideEffectFlow = _sideEffectChannel.receiveAsFlow()

    init {
        handleIntents()
        fetchProducts()
    }

    fun processIntent(intent: ProductIntent2) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentFlow.collect { intent ->
                when (intent) {
                    is ProductIntent2.Retry -> {
                        fetchProducts()
                    }
                    is ProductIntent2.ProductClicked -> {
                        _sideEffectChannel.send(ProductSideEffect.NavigateToDetails(intent.product))
                    }
                    is ProductIntent2.FloatButtonClicked -> {

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

    private fun fetchProducts() {
        viewModelScope.launch {
            _productsState.value = ProductListUiState.Loading

            _productsState.value = when (val products = repository.getProducts()) {
                is Resource.Success -> {
                    if (products.data.isEmpty()) {
                        ProductListUiState.Empty
                    } else {
                        ProductListUiState.Success(
                            products.data,
                            message = null
                        )
                    }
                }

                is Resource.Error -> {
                    ProductListUiState.Error(mapDomainExceptionToStringRes(products.exception.toDomainException()))
                }

                is Resource.Empty -> {
                    ProductListUiState.Empty
                }

                else -> {
                    ProductListUiState.Error(R.string.unknown_error_title)
                }
            }
        }
    }
}