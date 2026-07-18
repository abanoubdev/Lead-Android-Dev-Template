package net.compose.leadandroiddevprep.products.presentation

import androidx.annotation.StringRes
import net.compose.leadandroiddevprep.domain.model.Product

sealed interface ProductListUiState {
    data object Loading : ProductListUiState

    data object Empty : ProductListUiState
    data class Success(val products: List<Product>, @StringRes val message: Int?) :
        ProductListUiState

    data class Error(@StringRes val message: Int) : ProductListUiState
}