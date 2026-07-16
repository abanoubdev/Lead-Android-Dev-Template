package net.compose.leadandroiddevprep.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import net.compose.leadandroiddevprep.data.exception.safeApiCall
import net.compose.leadandroiddevprep.data.local.ProductDao
import net.compose.leadandroiddevprep.data.remote.ProductApiService
import net.compose.leadandroiddevprep.domain.Resource
import net.compose.leadandroiddevprep.domain.model.Product
import net.compose.leadandroiddevprep.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService
) : ProductRepository {

    override fun getProducts(): Flow<Resource<List<Product>>> = flow {

        emit(Resource.Loading)

        val networkResult = safeApiCall {
            api.getProducts()
        }

        when (networkResult) {
            is Resource.Success -> {
                val data = networkResult.data.map { it.toDomain() }
                emit(Resource.Success(data))
            }

            is Resource.Error -> {
                emit(Resource.Error(networkResult.exception))
            }

            else ->
                emit(Resource.Empty)
        }
    }
}