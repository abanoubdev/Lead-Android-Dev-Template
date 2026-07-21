package net.compose.leadandroiddevprep.domain.repository

import kotlinx.coroutines.flow.Flow
import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.model.Product

interface ProductRepositoryOfflineFirst {
    fun getProducts(): Flow<Resource<List<Product>>>
}
