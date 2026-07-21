package net.compose.leadandroiddevprep.data.repository.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.compose.leadandroiddevprep.data.exception.safeApiCall
import net.compose.leadandroiddevprep.data.local.product.ProductDao
import net.compose.leadandroiddevprep.data.remote.product.ProductApiService
import net.compose.leadandroiddevprep.domain.model.Product
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.repository.ProductRepositoryOfflineFirst
import javax.inject.Inject

class ProductRepositoryOfflineFirstImpl @Inject constructor(
    private val dao: ProductDao,
    private val api: ProductApiService,
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
//                val localProducts = dao.getAllProducts()
                val entities = networkResult.data.map { networkItem ->
                    networkItem.toEntity()
                }
                dao.insertProducts(entities)
//                val localMap = localProducts.associateBy { it.id }
//                val entities = networkResult.data.map { networkItem ->
//                    var entity = networkItem.toEntity()
//                    val existingProduct = localMap[entity.id]
//
//                    if (existingProduct != null) {
//                        entity = entity.copy(
//                            addedToCart = existingProduct.addedToCart,
//                            cartQuantity = existingProduct.cartQuantity
//                        )
//                    }
//                    entity
//                }
//                dao.insertProducts(entities)
            }

            is Resource.Error -> {
                val localProducts = dao.getAllProducts()
                if (localProducts.isEmpty())
                    send(Resource.Error(networkResult.exception))
            }

            else -> send(Resource.Error(Exception("Unknown error")))
        }

    }
}