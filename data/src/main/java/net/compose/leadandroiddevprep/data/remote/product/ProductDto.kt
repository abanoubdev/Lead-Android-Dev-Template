package net.compose.leadandroiddevprep.data.remote.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import net.compose.leadandroiddevprep.data.local.product.ProductEntity
import net.compose.leadandroiddevprep.domain.model.Product

@Serializable
data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("image") val image: String?
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(
            id = id,
            title = title ?: "",
            description = description ?: "",
            price = price ?: 0.0,
            imageUrl = image ?: ""
        )
    }

    fun toDomain(): Product {
        return Product(
            id = id,
            title = title ?: "",
            description = description ?: "",
            price = price ?: 0.0,
            imageUrl = image ?: ""
        )
    }
}
