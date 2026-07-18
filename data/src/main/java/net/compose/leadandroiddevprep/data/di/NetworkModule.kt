package net.compose.leadandroiddevprep.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import net.compose.leadandroiddevprep.data.remote.CartApiService
import net.compose.leadandroiddevprep.data.remote.ProductApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context))
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideProductApiService(client: OkHttpClient, networkJson: Json): ProductApiService {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun providCartsApiService(client: OkHttpClient, networkJson: Json): CartApiService {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create(CartApiService::class.java)
    }
}