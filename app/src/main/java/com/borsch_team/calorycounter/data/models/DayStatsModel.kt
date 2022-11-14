package com.borsch_team.calorycounter.data.models

data class DayStatsModel(
    val timestamp: Long,
    val caloriesInDay: Int,
    val proteinsInDay: Float,
    val fatsInDay: Float,
    val carbohydratesInDay: Int,
)