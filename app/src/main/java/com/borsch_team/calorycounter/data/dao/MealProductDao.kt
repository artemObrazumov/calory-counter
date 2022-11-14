package com.borsch_team.calorycounter.data.dao

import androidx.room.*
import com.borsch_team.calorycounter.data.models.MealProduct

@Dao
interface MealProductDao {
    @Query("DELETE FROM mealproduct WHERE mealattachID = :id")
    suspend fun deleteInMeal(id: Int)

    @Query("DELETE FROM mealproduct WHERE foodID = :id")
    suspend fun deleteProduct(id: Int)

    @Query("SELECT * FROM mealproduct WHERE foodTimestamp BETWEEN :timestamp AND :timestamp+86400000 ORDER BY foodTimestamp")
    suspend fun getProductsInDay(timestamp: Long): List<MealProduct>

    @Query("SELECT * FROM mealproduct WHERE mealattachID = :id")
    suspend fun getProduct(id: Int): MealProduct

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: MealProduct): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(meal: MealProduct): Int

    @Transaction
    suspend fun upsert(meal: MealProduct): Long {
        var id = insert(meal)
        if (id == -1L) {
            id = update(meal).toLong()
        }
        return id
    }

    @Delete
    suspend fun delete(meal: MealProduct)
}