package net.compose.leadandroiddevprep.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.Serializable
import net.compose.leadandroiddevprep.auth.presentation.AuthComposable

@Serializable
data object Login : NavKey

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun EntryProviderScope<NavKey>.AuthEntryProvider(
    backStack: NavBackStack<NavKey>,
    onLoginSuccess: () -> Unit
) {
    entry<Login> {
        AuthComposable(backStack, onLoginSuccess)
    }
}