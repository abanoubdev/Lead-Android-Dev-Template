package net.compose.leadandroiddevprep.products.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.compose.leadandroiddevprep.domain.model.Product

@Composable
fun Products(
    products: List<Product>,
    cartQuantities: Map<Int, Int>,
    onAddToCartClick: (Product) -> Unit = {},
) {

    Log.d("ComposePerf", "Products Screen Recomposed!")
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            items(items = products, key = { it.id }) { product ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    ProductItem(product = product, cartQuantity = cartQuantities[product.id] ?: 0, onAddToCartClick = onAddToCartClick)
                }
            }
        }
    }
}