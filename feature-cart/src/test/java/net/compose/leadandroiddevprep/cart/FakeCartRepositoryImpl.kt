package net.compose.leadandroiddevprep.cart

import net.compose.leadandroiddevprep.domain.model.CartItem
import net.compose.leadandroiddevprep.domain.repository.CartRepository

class FakeCartRepositoryImpl : CartRepository {

    override suspend fun getCartItems(): List<CartItem> {
        return CartItemFactory.createCartItems(15)
    }

    override fun getPendingSyncItems(): List<CartItem> {
        return CartItemFactory.createCartItems(10)
    }

    override suspend fun syncCartItems(pendingItems: List<CartItem>): Boolean = true
}