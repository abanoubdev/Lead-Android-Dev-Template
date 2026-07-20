package net.compose.leadandroiddevprep.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import net.compose.leadandroiddevprep.domain.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE addedToCart = 1")
    suspend fun getSyncProducts(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: Int): ProductEntity?

    @Query("UPDATE products SET addedToCart = 0, cartQuantity = 0 WHERE addedToCart = 1")
    suspend fun clearAllSyncProducts(): Int

    @Upsert
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity): Int

    @Query("DELETE FROM products")
    suspend fun clearProducts()
}