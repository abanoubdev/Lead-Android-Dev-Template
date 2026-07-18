package net.compose.leadandroiddevprep.auth.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

@Composable
fun AuthComposable(
    backStack: NavBackStack<NavKey>,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            onLoginSuccess()
        }) {
            Text("Submit Login")
        }
    }
}