package net.compose.leadandroiddevprep.data.remote.cart

import retrofit2.http.Body
import retrofit2.http.POST

interface CartApiService {

    @POST("cart/sync")
    suspend fun syncCartItems(@Body cartItems: List<CartItemDto>)
}

