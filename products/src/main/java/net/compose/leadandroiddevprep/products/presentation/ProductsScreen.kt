package net.compose.leadandroiddevprep.products.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.compose.leadandroiddevprep.domain.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ProductsScreenComposable(
    viewModel: ProductsOfflineFirstViewModel = hiltViewModel(),
) {

    val productsList by viewModel.productsState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            when (val state = productsList) {
                is ProductListUiState.Loading -> {
                    LoadingComposable()
                }

                is ProductListUiState.Success -> {
                    Products(state.products)
                }

                is ProductListUiState.Error -> {
                    ProductsError(
                        exception = state.message,
                        onRetry = { viewModel.processIntent(ProductIntent.Retry) }
                    )
//                    LaunchedEffect(state.exception) {
//                        snackbarHostState.showSnackbar(
//                            message = state.exception.message ?: "Unknown error"
//                        )
//                    }
                }
                else -> {
                    // do nothing
                }
            }
        }
    }
}

@Composable
fun LoadingComposable() {
    CircularProgressIndicator()
}

@Composable
fun Products(products: List<Product>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(products, key = { it.id }) { product ->
            Text(text = product.title)
        }
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
