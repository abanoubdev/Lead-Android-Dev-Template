package net.compose.leadandroiddevprep.products.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import net.compose.leadandroiddevprep.coreui.components.LoadingComposable
import net.compose.leadandroiddevprep.products.presentation.composables.Products
import net.compose.leadandroiddevprep.products.presentation.composables.ProductsError

//@OptIn(ExperimentalCoroutinesApi::class)
//@Composable
//fun ProductsScreenComposable(
//    viewModel: ProductsViewModel = hiltViewModel(),
//) {
//    val productsList by viewModel.productsState.collectAsStateWithLifecycle()
//
//    LaunchedEffect(Unit) {
//        viewModel.sideEffectFlow.collect {
//            when (it) {
//                is ProductSideEffect.NavigateToDetails -> {
//                    // add navigation logic here
//                }
//            }
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        when (val state = productsList) {
//            is ProductListUiState.Loading -> {
//                LoadingComposable()
//            }
//
//            is ProductListUiState.Success -> {
//                Products(state.products, onProductClick = {
//                    viewModel.processIntent(ProductIntent2.ProductClicked(it))
//                }, onFloatClicked = {
//                    viewModel.processIntent(ProductIntent2.FloatButtonClicked)
//                })
//            }
//
//            is ProductListUiState.Error -> {
//                ProductsError(state.message) {
//                    viewModel.processIntent(ProductIntent2.Retry)
//                }
//            }
//
//            else -> {}
//        }
//    }
//}


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ProductsScreenComposable(
    viewModel: ProductsOfflineFirstViewModel = hiltViewModel(),
    onNavigateToDetails: (Int) -> Unit
) {

    val productsList by viewModel.productsState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.sideEffectFlow.collect {
            when (it) {
                is ProductSideEffect.NavigateToDetails -> {
                    onNavigateToDetails.invoke(it.product.id)
                }

                is ProductSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        it.message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
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
                Products(state.products, cartQuantities = state.cartQuantities, onAddToCartClick = {
                    scope.launch {
                        viewModel.addToCart(it)
                    }
                })
            }

            is ProductListUiState.Error -> {
                ProductsError(state.message) {
                    viewModel.processIntent(ProductIntent.Retry)
                }
            }

            else -> {}
        }
    }
}