package com.borsch_team.calorycounter.ui.mealEditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.*
import com.borsch_team.calorycounter.extensions.toProduct
import kotlinx.coroutines.launch

class MealEditorViewModel: ViewModel() {
    val result: MutableLiveData<Result> = MutableLiveData()
    val newProduct: MutableLiveData<Product> = MutableLiveData()
    val meal: MutableLiveData<MealAndProducts> = MutableLiveData()
    val productsData: MutableLiveData<List<Product>> = MutableLiveData()

    fun saveMealToDatabase(meal: Meal, products: List<MealProduct>) {
        viewModelScope.launch {
            var mealID = meal.mealID
            val newmealID = App.meallDatabase.mealDao().upsert(meal)
            if (mealID == null) mealID = newmealID
            App.meallDatabase.mealProductDao().deleteInMeal(mealID.toInt())
            products.forEach {
                it.mealID = mealID
                it.foodTimestamp = meal.timestamp
                App.meallDatabase.mealProductDao().upsert(it)
            }
            result.value = Result(Result.RESULT_SUCCESS)
        }
    }

    fun getProductData(id: Int) {
        viewModelScope.launch {
            App.foodDatabaseAccess.open()
            newProduct.value = App.foodDatabaseAccess.getProduct(id).toProduct()
            App.foodDatabaseAccess.close()
        }
    }

    fun loadMeal(id: Int) {
        viewModelScope.launch {
            meal.value = App.meallDatabase.mealDao().getMeal(id)
        }
    }

    fun loadProducts(ids: List<Int>) {
        val productsData = mutableListOf<Product>()
        App.foodDatabaseAccess.open()
        viewModelScope.launch {
            ids.forEach { id ->
                productsData.add(App.foodDatabaseAccess.getProduct(id).toProduct())
            }
        }
        App.foodDatabaseAccess.close()
        this.productsData.value = productsData.toList()
    }
}