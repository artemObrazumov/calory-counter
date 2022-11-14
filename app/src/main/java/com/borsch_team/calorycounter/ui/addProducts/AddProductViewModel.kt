package com.borsch_team.calorycounter.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.Product
import com.borsch_team.calorycounter.extensions.toProductsArray
import kotlinx.coroutines.launch

class AddProductViewModel : ViewModel() {
    val customProductsList: MutableLiveData<List<Product>> = MutableLiveData()

    fun getCustomProducts() {
        viewModelScope.launch {
            App.foodDatabaseAccess.open()
            customProductsList.value = App.foodDatabaseAccess.getCustomProducts().toProductsArray()
            App.foodDatabaseAccess.close()
        }
    }

    fun removeProduct(id: Int) {
        viewModelScope.launch {
            val productDao = App.productDatabase.productDao()
            productDao.deleteProduct(id)
            val mealProductDao = App.meallDatabase.mealProductDao()
            mealProductDao.deleteProduct(id)
        }
    }
}