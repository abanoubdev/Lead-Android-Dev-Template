package net.compose.leadandroiddevprep

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import net.compose.leadandroiddevprep.auth.navigation.AuthEntryProvider
import net.compose.leadandroiddevprep.composables.MerchantDashboard
import net.compose.leadandroiddevprep.composables.TransactionGenerator
import net.compose.leadandroiddevprep.products.navigation.Products
import net.compose.leadandroiddevprep.products.navigation.ProductsEntryProvider
import net.compose.leadandroiddevprep.ui.theme.LeadAndroidDevPrepTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            LeadAndroidDevPrepTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    MainNavigation()
//                    ProfileScreen(userId = 123)
                    MerchantDashboard()
                }
            }
        }
    }
}

//@Composable
//fun MainNavigation() {
//
//    val backStack = rememberNavBackStack(Products)
//
//    NavDisplay(
//        backStack = backStack,
//        onBack = { backStack.removeLastOrNull() },
//        entryDecorators = listOf(
//            rememberSaveableStateHolderNavEntryDecorator(),
//            rememberViewModelStoreNavEntryDecorator()
//        ), entryProvider = entryProvider {
//            ProductsEntryProvider(
//                backStack = backStack,
//                onNavigateToDetails = {
//
//                }
//            )
//            AuthEntryProvider(backStack = backStack) {
//                backStack.removeLastOrNull()
//            }
//        },
//        transitionSpec = {
//            slideInHorizontally(initialOffsetX = { it }) togetherWith slideOutHorizontally(
//                targetOffsetX = { -it }
//            )
//        }, popTransitionSpec = {
//            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(
//                targetOffsetX = { it })
//        })
//}