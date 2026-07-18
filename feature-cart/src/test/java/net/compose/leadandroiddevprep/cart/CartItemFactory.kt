package net.compose.leadandroiddevprep.cart

import net.compose.leadandroiddevprep.domain.model.CartItem

object CartItemFactory {

    fun createCartItem(
        id: String = "1",
        name: String = "Product 1",
        price: Double = 10.0
    ): CartItem {
        return CartItem(
            id = id,
            name = name,
            price = price
        )
    }

    fun createCartItems(count: Int = 7): List<CartItem> {
        return (1..count).map { id ->
            createCartItem(
                id = id.toString(),
                name = "Product $id",
                price = id * 10.0
            )
        }
    }
}
