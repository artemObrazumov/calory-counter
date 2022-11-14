package com.borsch_team.calorycounter.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var ID: Int?,
    var Name: String?,
    var Calories: Int?,
    var Proteins: Float?,
    var Fats: Float?,
    var Carbohydrates: Int?,
) {
    fun toMealProduct(): MealProduct =
        MealProduct( mealID = 0,
            foodID = this.ID.toString(),
            foodWeight = null,
            foodTimestamp = null
        )
}