package net.compose.leadandroiddevprep.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.compose.leadandroiddevprep.data.local.cart.CartItemDao
import net.compose.leadandroiddevprep.data.local.keystore.MerchantKeyStore
import net.compose.leadandroiddevprep.data.local.keystore.MerchantKeyStoreImpl
import net.compose.leadandroiddevprep.data.local.product.ProductDao
import net.compose.leadandroiddevprep.data.remote.cart.CartApiService
import net.compose.leadandroiddevprep.data.remote.product.ProductApiService
import net.compose.leadandroiddevprep.data.repository.product.ProductRepositoryImpl
import net.compose.leadandroiddevprep.data.repository.product.ProductRepositoryOfflineFirstImpl
import net.compose.leadandroiddevprep.data.repository.cart.CartRepoImpl
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
        api: ProductApiService,
    ): ProductRepositoryOfflineFirst {
        return ProductRepositoryOfflineFirstImpl(
            dao = dao,
            api = api
        )
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartItemDao,
        cartApi: CartApiService,
    ): CartRepository {
        return CartRepoImpl(
            cartItemDao = cartDao,
            cartApi = cartApi
        )
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        api: ProductApiService
    ): ProductRepository {
        return ProductRepositoryImpl(api)
    }
}