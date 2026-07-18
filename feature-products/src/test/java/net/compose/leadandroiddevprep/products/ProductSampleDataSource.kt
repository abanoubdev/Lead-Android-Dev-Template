package net.compose.leadandroiddevprep.products

import net.compose.leadandroiddevprep.domain.model.Product

object ProductSampleDataSource {
    fun generateSampleProducts(): List<Product> {
        val products = mutableListOf<Product>()
        for (i in 1..10) {
            products.add(
                Product(
                    id = i,
                    title = "Product $i",
                    description = "Description for product $i",
                    price = 9.99 + i,
                    imageUrl = ""
                )
            )
        }
        return products
    }
}