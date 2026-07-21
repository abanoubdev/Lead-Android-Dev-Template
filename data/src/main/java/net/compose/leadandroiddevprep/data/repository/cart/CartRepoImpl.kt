package net.compose.leadandroiddevprep.data.repository.cart

import android.util.Log
import net.compose.leadandroiddevprep.data.exception.safeApiCall
import net.compose.leadandroiddevprep.data.local.cart.CartItemDao
import net.compose.leadandroiddevprep.data.local.cart.CartItemEntity
import net.compose.leadandroiddevprep.data.remote.cart.CartApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.repository.CartRepository
import javax.inject.Inject

class CartRepoImpl @Inject constructor(
    private val cartItemDao: CartItemDao,
    private val cartApi: CartApiService
) : CartRepository {

    override fun getCartItemsStream(): Flow<Map<Int, Int>> {
        return cartItemDao.getAllItemsFlow().map { items ->
            items.associate { it.id to it.quantity }
        }
    }

    override suspend fun syncCartItems(): Resource<Boolean> {
        val cartItems = cartItemDao.getAllItems().map {
            it.toCartDto()
        }

        if (cartItems.isNotEmpty()) {
            val networkResult = safeApiCall {
                cartApi.syncCartItems(cartItems)
            }

            return when (networkResult) {
                is Resource.Success -> Resource.Success(true)
                is Resource.Error -> Resource.Error(networkResult.exception)
                else -> Resource.Error(Exception("Unknown error"))
            }
        }
        return Resource.Success(true)
    }

    override suspend fun clearSyncItems(): Resource<Boolean> {
        val result = cartItemDao.clear()
        if (result > 0) return Resource.Success(true)
        return Resource.Success(false)
    }

    override suspend fun addToCart(productId: Int): Resource<Boolean> {
        val allProducts = cartItemDao.getAllItems()
        Log.d("Products:-", "addToCart: $allProducts")
        var cartItem = cartItemDao.getItemById(productId)
        var result: Long
        if (cartItem != null) {
            result = cartItemDao.update(cartItem.copy(quantity = cartItem.quantity + 1)).toLong()
        } else {
            cartItem = CartItemEntity(id = productId, quantity = 1)
            result = cartItemDao.insert(cartItem)
        }

        return when (result) {
            -1L -> Resource.Error(Exception("Failed to add to cart"))
            else -> Resource.Success(true)
        }
    }
}