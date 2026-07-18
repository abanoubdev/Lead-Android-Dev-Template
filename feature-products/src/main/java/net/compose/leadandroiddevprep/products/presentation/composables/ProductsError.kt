package net.compose.leadandroiddevprep.products.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource

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