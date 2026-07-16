package net.compose.leadandroiddevprep.products.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import net.compose.leadandroiddevprep.domain.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ProductsScreenComposable(
    viewModel: ProductsViewModel = hiltViewModel(),
) {

    val productsList by viewModel.productsState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.sideEffectFlow.collect {
            when (it) {
                is ProductSideEffect.NavigateToDetails -> {
                    // add navigation logic here
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        when (val state = productsList) {
            is ProductListUiState.Loading -> {
                LoadingComposable()
            }

            is ProductListUiState.Success -> {
                Products(state.products) {
                    viewModel.processIntent(ProductIntent2.ProductClicked(it))
                }
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

@Composable
fun LoadingComposable() {
    CircularProgressIndicator()
}

@Composable
fun Products(products: List<Product>, onProductClick: (Product) -> Unit = {}) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = products, key = { it.id }) { product ->
            ProductItem(product = product, onProductClick = onProductClick)
        }
    }
}

@Composable
fun ProductItem(product: Product, onProductClick: (Product) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product) }) {
        Text(text = product.title)
    }
}

@Composable
fun ProductsError(exception: Int?, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        exception?.let {
            Text(stringResource(exception))
        }
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}
//{
//
//    val productsList by viewModel.productsState.collectAsStateWithLifecycle()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { paddingValues ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues),
//            contentAlignment = Alignment.Center
//        ) {
//
//            when (val state = productsList) {
//                is ProductListUiState.Loading -> {
//                    LoadingComposable()
//                }
//
//                is ProductListUiState.Success -> {
//                    Products(state.products)
//                }
//
//                is ProductListUiState.Error -> {
//                    ProductsError(
//                        exception = state.message,
//                        onRetry = { viewModel.processIntent(ProductIntent.Retry) }
//                    )
////                    LaunchedEffect(state.exception) {
////                        snackbarHostState.showSnackbar(
////                            message = state.exception.message ?: "Unknown error"
////                        )
////                    }
//                }
//                else -> {
//                    // do nothing
//                }
//            }
//        }
//    }
//}


