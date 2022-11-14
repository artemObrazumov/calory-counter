package com.borsch_team.calorycounter.ui.slideshow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borsch_team.calorycounter.data.models.DayStatsModel
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.extensions.toProduct
import com.borsch_team.calorycounter.utils.Calculators
import kotlinx.coroutines.launch

class AnalyticsViewModel : ViewModel() {
    val prodStats: MutableLiveData<List<DayStatsModel>> = MutableLiveData()

    fun getStats(startTimestamp: Long, mode: Int) {
        var realTimestamp = startTimestamp
        viewModelScope.launch {
            val mealProductsDao = App.meallDatabase.mealProductDao()
            val stats = mutableListOf<DayStatsModel>()
            App.foodDatabaseAccess.open()
            val days = when(mode) {
                MODE_WEEK -> 7
                MODE_MONTH -> 30
                else -> 1
            }
            for (i in 1..days) {
                val products = mealProductsDao.getProductsInDay(realTimestamp)
                var caloriesInDay = 0
                var proteinsInDay = 0f
                var fatsInDay = 0f
                var carbohydratesInDay = 0
                products.forEach { mealProduct ->
                    val product = App.foodDatabaseAccess.getProduct(mealProduct.foodID!!.toInt())
                        .toProduct()
                    caloriesInDay += Calculators.calculateCFC(mealProduct.foodWeight!!, product.Calories!!).toInt()
                    proteinsInDay += Calculators.calculateCFC(mealProduct.foodWeight!!, product.Proteins!!)
                        .replace(',', '.').toFloat()
                    fatsInDay += Calculators.calculateCFC(mealProduct.foodWeight!!, product.Fats!!)
                        .replace(',', '.').toFloat()
                    carbohydratesInDay += Calculators.calculateCFC(mealProduct.foodWeight!!, product.Carbohydrates!!).toInt()
                }
                stats.add(DayStatsModel(
                    realTimestamp, caloriesInDay, proteinsInDay, fatsInDay, carbohydratesInDay
                ))
                realTimestamp -= 86400000
            }
            App.foodDatabaseAccess.close()
            prodStats.value = stats.reversed()
        }
    }

    companion object {
        const val MODE_WEEK = 0
        const val MODE_MONTH = 1
    }
}