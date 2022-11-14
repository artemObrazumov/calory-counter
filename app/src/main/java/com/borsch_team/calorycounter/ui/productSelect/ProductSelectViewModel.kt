package com.borsch_team.calorycounter.ui.productSelect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.Product
import com.borsch_team.calorycounter.extensions.toProductsArray
import kotlinx.coroutines.launch

class ProductSelectViewModel: ViewModel() {
    val products: MutableLiveData<List<Product>> = MutableLiveData()

    fun findProducts(query: String) {
        findDatabaseProducts(query)
    }

    private fun findDatabaseProducts(query: String) {
        viewModelScope.launch {
            App.foodDatabaseAccess.open()
            products.value =
                App.foodDatabaseAccess.findProducts(query).toProductsArray().toList()
            App.foodDatabaseAccess.close()
        }
    }
}