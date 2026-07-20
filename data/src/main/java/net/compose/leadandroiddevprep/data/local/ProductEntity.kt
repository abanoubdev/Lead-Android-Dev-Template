package net.compose.leadandroiddevprep.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import net.compose.leadandroiddevprep.domain.model.Product

@Entity(tableName = "products")
@Serializable
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val addedToCart: Boolean = false,
    val cartQuantity: Int = 0
) {

    fun toDomain(): Product {
        return Product(
            id = id,
            title = title,
            description = description,
            price = price,
            imageUrl = imageUrl,
            addedToCart = addedToCart,
            cartQuantity = cartQuantity
        )
    }
}
