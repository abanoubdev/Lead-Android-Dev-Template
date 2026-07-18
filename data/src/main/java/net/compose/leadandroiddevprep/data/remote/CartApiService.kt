package net.compose.leadandroiddevprep.data.remote

import net.compose.leadandroiddevprep.domain.model.CartItem
import retrofit2.http.POST

interface CartApiService {
    @POST("cartItems")
    suspend fun syncCartItems(items: List<CartItem>): Boolean
}