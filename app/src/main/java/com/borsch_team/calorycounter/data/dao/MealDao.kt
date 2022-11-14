package com.borsch_team.calorycounter.data.dao

import androidx.room.*
import com.borsch_team.calorycounter.data.models.Meal
import com.borsch_team.calorycounter.data.models.MealAndProducts


@Dao
interface MealDao {
    @Query("SELECT * FROM meal WHERE mealID = :id")
    suspend fun getMeal(id: Int): MealAndProducts

    @Transaction
    @Query("SELECT * FROM meal WHERE timestamp BETWEEN :timestamp AND :timestamp+86400000 ORDER BY timestamp")
    suspend fun getMealsWithProducts(timestamp: Long): List<MealAndProducts>

    @Query("SELECT * FROM meal WHERE timestamp BETWEEN :timestamp AND :timestamp+86400000 ORDER BY timestamp")
    suspend fun getOnlyMeals(timestamp: Long): List<Meal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(meal: Meal): Int

    @Transaction
    suspend fun upsert(meal: Meal): Long {
        var id = insert(meal)
        if (id == -1L) {
            id = update(meal).toLong()
        }
        return id
    }

    @Query("DELETE FROM meal WHERE mealID = :id")
    suspend fun delete(id: Int)
}