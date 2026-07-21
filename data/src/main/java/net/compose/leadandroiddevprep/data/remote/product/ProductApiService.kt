package net.compose.leadandroiddevprep.data.remote.product

import retrofit2.http.GET

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}