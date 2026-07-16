package net.compose.leadandroiddevprep.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
}
