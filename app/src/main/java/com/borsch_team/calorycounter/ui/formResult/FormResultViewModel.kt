package com.borsch_team.calorycounter.ui.mealEditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.Meal
import com.borsch_team.calorycounter.data.models.MealProduct
import com.borsch_team.calorycounter.data.models.Result
import com.borsch_team.calorycounter.extensions.toProduct
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class FormResultViewModel: ViewModel() {
    val caloriesUpdateValue: MutableLiveData<Int> = MutableLiveData<Int>()
        .apply { this.value = 0 }

    fun startUpdate(calories: Int) {
        viewModelScope.launch {
            repeat(calories/10) {
                caloriesUpdateValue.value = it*10+10
                delay(1L)
            }
        }
    }
}