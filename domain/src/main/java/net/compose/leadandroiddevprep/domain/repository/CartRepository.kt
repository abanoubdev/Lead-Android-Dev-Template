package net.compose.leadandroiddevprep.domain.repository

import kotlinx.coroutines.flow.Flow
import net.compose.leadandroiddevprep.domain.network.Resource

interface CartRepository {
    suspend fun addToCart(productId: Int): Resource<Boolean>
    suspend fun syncCartItems(): Resource<Boolean>
    suspend fun clearSyncItems(): Resource<Boolean>
    fun getCartItemsStream(): Flow<Map<Int, Int>>
}
