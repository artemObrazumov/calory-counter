package com.borsch_team.calorycounter.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
data class Meal (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mealID")
    var mealID: Long? = null,
    @ColumnInfo(name = "timestamp") var timestamp: Long?,
    @ColumnInfo(name = "mealName") var mealName: String?,
)