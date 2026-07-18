package net.compose.leadandroiddevprep.cart

import net.compose.leadandroiddevprep.domain.model.CartItem
import net.compose.leadandroiddevprep.domain.repository.CartRepository

class FakeErrorCartImplementation : CartRepository {
    override suspend fun getCartItems(): List<CartItem> {
        throw Exception("Error")
    }
}