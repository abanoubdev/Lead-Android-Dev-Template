package net.compose.leadandroiddevprep.cart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.Serializable
import net.compose.leadandroiddevprep.cart.presentation.CartComposable

@Serializable
data object Cart : NavKey

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun EntryProviderScope<NavKey>.CartEntryProvider(
    backStack: NavBackStack<NavKey>
) {
    entry<Cart> {
        CartComposable(backStack)
    }
}