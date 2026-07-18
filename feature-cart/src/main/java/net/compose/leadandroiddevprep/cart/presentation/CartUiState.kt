package net.compose.leadandroiddevprep.cart.presentation

import net.compose.leadandroiddevprep.domain.model.CartItem

sealed interface CartUiState{

    object Loading : CartUiState
    data class Success(val items: List<CartItem>) : CartUiState
    data class Error(val message: String?) : CartUiState
}