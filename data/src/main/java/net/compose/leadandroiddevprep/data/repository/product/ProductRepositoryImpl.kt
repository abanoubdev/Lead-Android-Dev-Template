package net.compose.leadandroiddevprep.data.repository.product

import net.compose.leadandroiddevprep.data.exception.safeApiCall
import net.compose.leadandroiddevprep.data.remote.product.ProductApiService
import net.compose.leadandroiddevprep.domain.model.Product
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService
) : ProductRepository {

    override suspend fun getProducts(): Resource<List<Product>> {
//        val networkResult = safeApiCall {
//            api.getProducts()
//        }
//        return when (networkResult) {
//            is Resource.Success -> {
//                val data = networkResult.data.map { it.toDomain() }
//                Resource.Success(data)
//            }
//            is Resource.Error -> {
//                Resource.Error(networkResult.exception)
//            }
//            else ->
//                Resource.Empty
//        }

        return Resource.Success(emptyList())
    }
}