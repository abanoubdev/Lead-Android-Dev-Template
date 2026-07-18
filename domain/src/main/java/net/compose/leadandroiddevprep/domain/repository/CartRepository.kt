package net.compose.leadandroiddevprep.domain.repository

import net.compose.leadandroiddevprep.domain.model.CartItem

interface CartRepository {
    suspend fun getCartItems(): List<CartItem>
}