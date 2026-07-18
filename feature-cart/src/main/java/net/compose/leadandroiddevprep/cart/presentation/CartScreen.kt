package net.compose.leadandroiddevprep.cart.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import net.compose.leadandroiddevprep.coreui.components.LoadingComposable
import net.compose.leadandroiddevprep.domain.model.CartItem

@Composable
fun CartComposable(backStack: NavBackStack<NavKey>, viewModel: CartViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CartScreen(state)
}

@Composable
fun CartScreen(state: CartUiState) {
    when (state) {
        is CartUiState.Loading -> {
            LoadingComposable()
        }

        is CartUiState.Success -> {
            CartSuccess(state.items)
        }

        is CartUiState.Error -> {
            CartError(state.message)
        }
    }
}

@Composable
fun CartError(error: String?) {
    TODO("Not yet implemented")
}

@Composable
fun CartSuccess(itemList: List<CartItem>) {
    TODO("Not yet implemented")
}