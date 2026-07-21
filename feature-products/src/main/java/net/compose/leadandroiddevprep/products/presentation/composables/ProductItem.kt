package net.compose.leadandroiddevprep.products.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.compose.leadandroiddevprep.coreui.components.ProductRoundedImage
import net.compose.leadandroiddevprep.domain.model.Product

@Composable
fun ProductItem(
    product: Product,
    cartQuantity: Int,
    onAddToCartClick: (Product) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductRoundedImage(
                modifier = Modifier.padding(10.dp),
                imageUrl = product.imageUrl,
                contentDescription = product.id.toString()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = product.title, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = product.getProductDescription())
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = product.getFormattedPrice(), fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(5.dp))
                if (cartQuantity > 0)
                    Text(
                        text = "Quantity: $cartQuantity",
                        fontWeight = FontWeight.Bold
                    )
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { onAddToCartClick(product) }) {
                    Text("Add ToCart")
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .height(2.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    val dummyProduct = Product(
        id = 1,
        title = "Wireless Headphones",
        description = "High-quality noise-canceling wireless headphones.",
        price = 199.99,
        imageUrl = "https://example.com/sample-image.png",
    )
    ProductItem(
        product = dummyProduct,
        cartQuantity = 10,
    )
}