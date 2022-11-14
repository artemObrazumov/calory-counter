package com.borsch_team.calorycounter.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
class MealProduct(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mealproductID")
    var mealProductID: Long? = null,
    @ColumnInfo(name = "mealattachID")
    var mealID: Long?,
    @ColumnInfo(name = "foodID") var foodID: String?,
    @ColumnInfo(name = "foodWeight") var foodWeight: Int?,
    @ColumnInfo(name = "foodTimestamp") var foodTimestamp: Long?,
)