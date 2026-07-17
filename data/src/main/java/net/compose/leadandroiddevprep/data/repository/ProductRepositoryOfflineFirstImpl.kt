package net.compose.leadandroiddevprep.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.compose.leadandroiddevprep.data.exception.safeApiCall
import net.compose.leadandroiddevprep.data.local.ProductDao
import net.compose.leadandroiddevprep.data.remote.ProductApiService
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.model.Product
import net.compose.leadandroiddevprep.domain.repository.ProductRepositoryOfflineFirst
import javax.inject.Inject

class ProductRepositoryOfflineFirstImpl @Inject constructor(
    private val dao: ProductDao,
    private val api: ProductApiService
) : ProductRepositoryOfflineFirst {

    override fun getProducts(): Flow<Resource<List<Product>>> = channelFlow {

        send(Resource.Loading)

        launch {
            dao.getProducts().collectLatest {
                if (it.isNotEmpty())
                    send(Resource.Success(it.map { it.toDomain() }))
                else
                    send(Resource.Empty)
            }
        }

        val networkResult = safeApiCall {
            api.getProducts()
        }

        when (networkResult) {
            is Resource.Success -> {
                dao.insertProducts(networkResult.data.map { it.toEntity() })
            }
            is Resource.Error -> {
                send(Resource.Error(networkResult.exception))
            }
            else ->
                send(Resource.Empty)
        }
    }
}