package net.compose.leadandroiddevprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
)
