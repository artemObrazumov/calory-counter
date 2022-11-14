package com.borsch_team.calorycounter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.borsch_team.calorycounter.data.models.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product): Long

    @Query("DELETE FROM data WHERE ID = :id")
    suspend fun deleteProduct(id: Int)

    @Query("SELECT * FROM data WHERE ID = 0")
    suspend fun initialize(): Product
}