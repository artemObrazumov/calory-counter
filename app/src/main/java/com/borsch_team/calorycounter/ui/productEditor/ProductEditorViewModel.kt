package com.borsch_team.calorycounter.ui.productEditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.Product
import kotlinx.coroutines.launch

class ProductEditorViewModel: ViewModel() {
    fun addCustomProduct(product: Product, onFinished: () -> Unit) {
        viewModelScope.launch {
            val productDao = App.productDatabase.productDao()
            productDao.insert(product)
            onFinished()
        }
    }
}