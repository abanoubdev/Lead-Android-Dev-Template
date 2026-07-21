package net.compose.leadandroiddevprep.data.local.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity): Long

    @Update
    suspend fun update(item: CartItemEntity): Int

    @Query("DELETE FROM cart_items")
    suspend fun clear(): Int

    @Query("SELECT * FROM cart_items")
    suspend fun getAllItems(): List<CartItemEntity>

    @Query("SELECT * FROM cart_items")
    fun getAllItemsFlow(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE id = :productId LIMIT 1")
    suspend fun getItemById(productId: Int): CartItemEntity?
}
