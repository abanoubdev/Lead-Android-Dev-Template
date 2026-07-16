package net.compose.leadandroiddevprep.products.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.Serializable
import net.compose.leadandroiddevprep.products.presentation.ProductsScreenComposable

@Serializable
data object Products : NavKey

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun EntryProviderScope<NavKey>.ProductsEntryProvider(backStack: NavBackStack<NavKey>) {
    entry<Products> {
        ProductsScreenComposable()
    }
}