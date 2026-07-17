package net.compose.leadandroiddevprep.domain.repository

import net.compose.leadandroiddevprep.domain.network.Resource
import net.compose.leadandroiddevprep.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): Resource<List<Product>>
}