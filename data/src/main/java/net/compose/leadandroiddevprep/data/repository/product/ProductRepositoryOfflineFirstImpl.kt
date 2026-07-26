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
            dao.getProducts().collectLatest {
                if (it.isNotEmpty())
                    send(Resource.Success(it.map { it.toDomain() }))
            }
        }

        val productResult = safeApiCall {
            api.getProducts()
        }

        when (productResult) {
            is Resource.Success -> {
                val entities = productResult.data.map { networkItem ->
                    networkItem.toEntity()
                }
                if (entities.isNotEmpty())
                    dao.insertProducts(entities)
                else {
                    val localProducts = dao.getAllProducts()
                    val showEmpty = localProducts.isEmpty()
                    send(Resource.Empty(showEmpty = showEmpty))
                }
            }

            is Resource.Error -> {
                val localProducts = dao.getAllProducts()
                if (localProducts.isNotEmpty())
                    send(Resource.Success(localProducts.map { it.toDomain() }))
                else
                    send(Resource.Error(productResult.exception))
            }

            else -> {}
        }
    }
}