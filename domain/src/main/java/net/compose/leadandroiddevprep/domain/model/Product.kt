package net.compose.leadandroiddevprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int, val title: String, val description: String, val price: Double, val imageUrl: String
) {
    fun getProductDescription(): String {
        return if (description.length > 20) {
            description.substring(0, 60) + "..."
        } else {
            description
        }
    }
}
