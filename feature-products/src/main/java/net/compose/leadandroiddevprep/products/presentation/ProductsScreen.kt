package net.compose.leadandroiddevprep.products.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.compose.leadandroiddevprep.coreui.components.LoadingComposable
import net.compose.leadandroiddevprep.products.presentation.composables.Products
import net.compose.leadandroiddevprep.products.presentation.composables.ProductsError

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ProductsScreenComposable(
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val productsList by viewModel.productsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffectFlow.collect {
            when (it) {
                is ProductSideEffect.NavigateToDetails -> {
                    // add navigation logic here
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = productsList) {
            is ProductListUiState.Loading -> {
                LoadingComposable()
            }

            is ProductListUiState.Success -> {
                Products(state.products, onProductClick = {
                    viewModel.processIntent(ProductIntent2.ProductClicked(it))
                }, onFloatClicked = {
                    viewModel.processIntent(ProductIntent2.FloatButtonClicked)
                })
            }

            is ProductListUiState.Error -> {
                ProductsError(state.message) {
                    viewModel.processIntent(ProductIntent2.Retry)
                }
            }

            else -> {}
        }
    }
}