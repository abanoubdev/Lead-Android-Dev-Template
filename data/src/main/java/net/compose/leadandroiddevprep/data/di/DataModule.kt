package net.compose.leadandroiddevprep.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.compose.leadandroiddevprep.data.local.AppDatabase
import net.compose.leadandroiddevprep.data.local.ProductDao
import net.compose.leadandroiddevprep.data.remote.ProductApiService
import net.compose.leadandroiddevprep.data.repository.ProductRepositoryImpl
import net.compose.leadandroiddevprep.data.repository.ProductRepositoryOfflineFirstImpl
import net.compose.leadandroiddevprep.domain.repository.ProductRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao
    }

    @Provides
    @Singleton
    fun provideProductApiService(): ProductApiService {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductOfflineFirstRepository(
        dao: ProductDao,
        api: ProductApiService
    ): ProductRepository {
        return ProductRepositoryOfflineFirstImpl(dao, api)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        api: ProductApiService
    ): ProductRepository {
        return ProductRepositoryImpl(api)
    }
}