package net.compose.leadandroiddevprep.data.remote

import net.compose.leadandroiddevprep.data.local.ProductEntity
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
    @POST
    fun syncCartItems(cartItems: List<ProductEntity>)
}