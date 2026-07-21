package net.compose.leadandroiddevprep.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import net.compose.leadandroiddevprep.data.local.cart.CartItemDao
import net.compose.leadandroiddevprep.data.local.cart.CartItemEntity
import net.compose.leadandroiddevprep.data.local.product.ProductDao
import net.compose.leadandroiddevprep.data.local.product.ProductEntity

@Database(
    entities = [ProductEntity::class, CartItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val cartItemDao: CartItemDao
}