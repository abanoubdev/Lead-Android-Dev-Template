package net.compose.leadandroiddevprep.data.remote.cart

import kotlinx.serialization.Serializable

@Serializable
data class CartItemDto(
    val productId: Int,
    val quantity: Int
)