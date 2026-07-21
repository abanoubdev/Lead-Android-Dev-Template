package net.compose.leadandroiddevprep.data.local.cart

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.compose.leadandroiddevprep.data.remote.cart.CartItemDto

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val id: Int,
    val quantity: Int
) {

    fun toCartDto(): CartItemDto {
        return CartItemDto(
            productId = id,
            quantity = quantity
        )
    }
}
