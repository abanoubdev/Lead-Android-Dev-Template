package net.compose.leadandroiddevprep.data.repository

import android.net.http.HttpException
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import net.compose.leadandroiddevprep.data.exception.safeApiCall
import net.compose.leadandroiddevprep.data.exception.toDomainException
import net.compose.leadandroiddevprep.data.local.ProductDao
import net.compose.leadandroiddevprep.data.local.ProductEntity
import net.compose.leadandroiddevprep.data.remote.ProductApiService
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.model.Product
import net.compose.leadandroiddevprep.domain.repository.ProductRepositoryOfflineFirst
import javax.inject.Inject
import kotlin.collections.copy

class ProductRepositoryOfflineFirstImpl @Inject constructor(
    private val dao: ProductDao, private val api: ProductApiService
) : ProductRepositoryOfflineFirst {

    override fun getProducts(): Flow<Resource<List<Product>>> = channelFlow {

        send(Resource.Loading)

        launch {
            dao.getProducts().collectLatest { it ->
                if (it.isNotEmpty()) send(Resource.Success(it.map { it.toDomain() }))
                else send(Resource.Empty)
            }
        }

        val networkResult = safeApiCall {
            api.getProducts()
        }

        when (networkResult) {
            is Resource.Success -> {
                val localProducts = dao.getAllProducts()
                val localMap = localProducts.associateBy { it.id }

                val entities = networkResult.data.map { networkItem ->
                    var entity = networkItem.toEntity()
                    val existingProduct = localMap[entity.id]

                    if (existingProduct != null) {
                        entity = entity.copy(
                            addedToCart = existingProduct.addedToCart,
                            cartQuantity = existingProduct.cartQuantity
                        )
                    }
                    entity
                }
                dao.insertProducts(entities)
            }

            is Resource.Error -> {
                val localProducts = dao.getAllProducts()
                if (localProducts.isEmpty())
                    send(Resource.Error(networkResult.exception))
            }

            else -> send(Resource.Error(Exception("Unknown error")))
        }

    }


    override suspend fun syncCartItems(): Resource<Boolean> {

        val cartItems = dao.getSyncProducts()

        if (cartItems.isNotEmpty()) {

            val networkResult = safeApiCall {
                api.syncCartItems(cartItems)
            }

            return when (networkResult) {
                is Resource.Success -> {
                    Resource.Success(true)
                }

                is Resource.Error -> {
                    Resource.Error(networkResult.exception)
                }

                else -> Resource.Error(Exception("Unknown error"))
            }
        }

        return Resource.Success(true)
    }

    override suspend fun clearSyncItems(): Resource<Boolean> {
        val result = dao.clearAllSyncProducts()
        if (result > 0) return Resource.Success(true)

        return Resource.Success(false)
    }

    override suspend fun addToCart(productId: Int): Resource<Boolean> {

        val allproducts = dao.getAllProducts()
        Log.d("allproducts", "addToCart: $allproducts")

        val product =
            dao.getProductById(productId) ?: return Resource.Error(Exception("Product not found"))
        val result: Int = dao.updateProduct(
            product.copy(
                addedToCart = true,
                cartQuantity = product.cartQuantity + 1,
            )
        )
        return if (result > 0) Resource.Success(true)
        else Resource.Error(Exception("Unknown error"))
    }
}