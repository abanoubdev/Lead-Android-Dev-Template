package net.compose.leadandroiddevprep.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.compose.leadandroiddevprep.data.local.ProductDao
import net.compose.leadandroiddevprep.data.remote.ProductApiService
import net.compose.leadandroiddevprep.data.repository.CartRepositoryImpl
import net.compose.leadandroiddevprep.data.repository.ProductRepositoryImpl
import net.compose.leadandroiddevprep.data.repository.ProductRepositoryOfflineFirstImpl
import net.compose.leadandroiddevprep.domain.repository.CartRepository
import net.compose.leadandroiddevprep.domain.repository.ProductRepository
import net.compose.leadandroiddevprep.domain.repository.ProductRepositoryOfflineFirst
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideProductOfflineFirstRepository(
        dao: ProductDao,
        api: ProductApiService
    ): ProductRepositoryOfflineFirst {
        return ProductRepositoryOfflineFirstImpl(dao, api)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        api: ProductApiService
    ): ProductRepository {
        return ProductRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
    ): CartRepository {
        return CartRepositoryImpl()
    }
}