package net.compose.leadandroiddevprep.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.compose.leadandroiddevprep.domain.repository.CartRepository

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            try {
                val items = repository.getCartItems()
                _uiState.value = CartUiState.Success(items)
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message)
            }
        }
    }
}