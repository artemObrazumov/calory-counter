package com.borsch_team.calorycounter.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class MealAndProducts (
    @Embedded
    var meal: Meal,

    @Relation(parentColumn = "mealID", entityColumn = "mealattachID")
    var products: List<MealProduct>
)