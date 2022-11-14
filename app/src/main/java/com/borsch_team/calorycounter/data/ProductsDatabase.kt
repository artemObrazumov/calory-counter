package com.borsch_team.calorycounter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.borsch_team.calorycounter.data.dao.ProductDao
import com.borsch_team.calorycounter.data.models.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductsDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}