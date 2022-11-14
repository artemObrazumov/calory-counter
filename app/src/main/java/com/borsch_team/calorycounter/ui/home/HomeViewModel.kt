package com.borsch_team.calorycounter.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.Meal
import com.borsch_team.calorycounter.data.models.MealAndProducts
import com.borsch_team.calorycounter.data.models.MealProduct
import com.borsch_team.calorycounter.data.models.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val meals: MutableLiveData<List<MealAndProducts>> = MutableLiveData()
    val products: MutableLiveData<List<MealProduct>> = MutableLiveData()

    fun getMeals(timestamp: Long) {
        viewModelScope.launch {
            val mealDao = App.meallDatabase.mealDao()
            meals.value = mealDao.getMealsWithProducts(timestamp)
        }
    }
    fun getProducts(timestamp: Long) {
        viewModelScope.launch {
            val productsDao = App.meallDatabase.mealProductDao()
            products.value = productsDao.getProductsInDay(timestamp)
        }
    }

    fun removeMeal(mealId: Long, onFinished: () -> Unit) {
        viewModelScope.launch {
            val mealDao = App.meallDatabase.mealDao()
            mealDao.delete(mealId.toInt())
            val productsDao = App.meallDatabase.mealProductDao()
            productsDao.deleteInMeal(mealId.toInt())
            onFinished()
        }
    }

    fun initializeDB() {
        viewModelScope.launch {
            val productsDao = App.productDatabase.productDao()
            productsDao.initialize()
        }
    }
}