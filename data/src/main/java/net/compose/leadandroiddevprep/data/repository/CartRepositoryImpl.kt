package net.compose.leadandroiddevprep.data.repository

import jakarta.inject.Inject
import net.compose.leadandroiddevprep.domain.model.CartItem
import net.compose.leadandroiddevprep.domain.repository.CartRepository

class CartRepositoryImpl @Inject constructor() : CartRepository {

    override suspend fun getCartItems(): List<CartItem> {
        return listOf(
            CartItem(id = "1", name = "Product 1", price = 10.0),
            CartItem(id = "2", name = "Product 2", price = 20.0),
            CartItem(id = "3", name = "Product 3", price = 30.0),
            CartItem(id = "4", name = "Product 4", price = 40.0),
            CartItem(id = "5", name = "Product 5", price = 50.0),
            CartItem(id = "6", name = "Product 6", price = 60.0),
            CartItem(id = "7", name = "Product 7", price = 70.0),
        )
    }
}