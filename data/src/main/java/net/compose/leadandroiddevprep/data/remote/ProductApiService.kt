package net.compose.leadandroiddevprep.data.remote

import retrofit2.http.GET

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}
