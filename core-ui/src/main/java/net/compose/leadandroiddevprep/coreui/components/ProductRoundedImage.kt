package net.compose.leadandroiddevprep.coreui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProductRoundedImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String = imageUrl,
    cornerRadius: Int = 16
) {

    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(100.dp)
    )
}

@Preview
@Composable
fun ProductRoundedImagePreview() {
    ProductRoundedImage(imageUrl = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png")
}
