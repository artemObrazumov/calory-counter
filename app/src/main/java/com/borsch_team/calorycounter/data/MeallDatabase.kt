package com.borsch_team.calorycounter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.borsch_team.calorycounter.data.dao.MealDao
import com.borsch_team.calorycounter.data.dao.MealProductDao
import com.borsch_team.calorycounter.data.models.Meal
import com.borsch_team.calorycounter.data.models.MealProduct

@Database(entities = [Meal::class, MealProduct::class], version = 1)
abstract class MeallDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun mealProductDao(): MealProductDao
}