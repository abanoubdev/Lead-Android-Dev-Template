package net.compose.leadandroiddevprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val addedToCart: Boolean = false,
    val cartQuantity: Int = 0
) {

    fun getFormattedPrice(): String {
        return "$$price"
    }

    fun getProductDescription(): String {
        return if (description.length > 60) {
            description.substring(0, 60) + "..."
        } else {
            description
        }
    }
}
