package net.compose.leadandroiddevprep.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)
