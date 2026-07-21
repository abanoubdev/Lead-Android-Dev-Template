package net.compose.leadandroiddevprep.data.local.product

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import net.compose.leadandroiddevprep.data.remote.product.ProductDto
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
) {

    fun toDomain(): Product {
        return Product(
            id = id,
            title = title,
            description = description,
            price = price,
            imageUrl = imageUrl,
        )
    }
}
