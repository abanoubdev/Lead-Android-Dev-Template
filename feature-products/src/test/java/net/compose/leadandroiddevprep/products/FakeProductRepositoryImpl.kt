package net.compose.leadandroiddevprep.products

import net.compose.leadandroiddevprep.domain.model.Product
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.repository.ProductRepository

class FakeProductRepositoryImpl : ProductRepository {
    override suspend fun getProducts(): Resource<List<Product>> {
        return Resource.Success(ProductSampleDataSource.generateSampleProducts())
    }
}

class FakeErrorProductRepositoryImpl : ProductRepository {
    override suspend fun getProducts(): Resource<List<Product>> {
        return Resource.Error(Exception("Unknown Error"))
    }
}

