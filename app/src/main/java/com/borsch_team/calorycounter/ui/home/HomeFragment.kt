package com.borsch_team.calorycounter.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.Meal
import com.borsch_team.calorycounter.data.models.MealProduct
import com.borsch_team.calorycounter.databinding.FragmentHomeBinding
import com.borsch_team.calorycounter.extensions.toProduct
import com.borsch_team.calorycounter.ui.adapters.MealsAdapter
import com.borsch_team.calorycounter.ui.mealEditor.MealEditorActivity
import com.borsch_team.calorycounter.utils.Calculators
import com.borsch_team.calorycounter.utils.SharedPreferencesUtils
import com.borsch_team.calorycounter.R
import java.lang.Integer.min
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mealsAdapter: MealsAdapter
    private var caloriesOnDay: Int = 0
    private var proteinsOnDay: Float = 0f
    private var fatsOnDay: Float = 0f
    private var carbohydratesOnDay: Int = 0
    private var maxCalories: Int = 0

    private lateinit var addMealLauncher: ActivityResultLauncher<Intent>

    // Calendar
    private val currentDate: Calendar = Calendar.getInstance(Locale.ROOT)
    private var year = currentDate[Calendar.YEAR]
    private var month = currentDate[Calendar.MONTH]
    private var day = currentDate[Calendar.DAY_OF_MONTH]

    private var timestamp = currentDate.timeInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMealLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                res ->
            if (res.resultCode == Activity.RESULT_OK) {
                homeViewModel.getMeals(timestamp)
                homeViewModel.getProducts(timestamp)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.meals.observe(viewLifecycleOwner) { meals ->
            val mealsList = mutableListOf<Meal>()
            val mealProductsList = mutableListOf<List<MealProduct>>()
            meals.forEach {
                mealsList.add(it.meal)
                mealProductsList.add(it.products)
            }
            mealsAdapter.updateMealsList(mealsList)
            mealsAdapter.updateMealsProductList(mealProductsList)
        }
        homeViewModel.products.observe(viewLifecycleOwner) { products ->
            getProductsSum(products)
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupCalendar()
        setupMealsList()

        maxCalories = SharedPreferencesUtils.getCalories(requireContext())
        binding.maxCalories.text = "Рекомендовано: $maxCalories калорий"

        binding.addMeal.setOnClickListener {
            addMealLauncher.launch(Intent(context, MealEditorActivity::class.java).apply {
                this.putExtra("timestamp", timestamp)
            })
        }
        binding.imageView5.setOnClickListener {
            addMealLauncher.launch(Intent(context, MealEditorActivity::class.java).apply {
                this.putExtra("timestamp", timestamp)
            })
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun getProductsSum(products: List<MealProduct>) {
        caloriesOnDay = 0
        proteinsOnDay = 0f
        fatsOnDay = 0f
        carbohydratesOnDay = 0
        try {
            App.foodDatabaseAccess.open()
            products.forEach {
                val product = App.foodDatabaseAccess.getProduct(it.foodID!!.toInt())
                    .toProduct()
                caloriesOnDay += Calculators.calculateCFC(it.foodWeight!!, product.Calories!!).toInt()
                proteinsOnDay += Calculators.calculateCFC(it.foodWeight!!, product.Proteins!!)
                    .replace(',', '.').toFloat()
                fatsOnDay += Calculators.calculateCFC(it.foodWeight!!, product.Fats!!)
                    .replace(',', '.').toFloat()
                carbohydratesOnDay += Calculators.calculateCFC(it.foodWeight!!, product.Carbohydrates!!).toInt()
            }
            App.foodDatabaseAccess.close()
        } catch (e: Exception) {homeViewModel.initializeDB()}

        binding.Proteins.text =
            "${DecimalFormat("#.##").format(proteinsOnDay)} г"
        binding.Fats.text =
            "${DecimalFormat("#.##").format(fatsOnDay)} г"
        binding.Carbohydrates.text = "$carbohydratesOnDay г"
        binding.calories.text = "$caloriesOnDay калорий"
        if (caloriesOnDay > maxCalories) {
            binding.caloriesbar.unfinishedStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.orange_500)
            binding.caloriesbar.finishedStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.orange_500)
            binding.calories.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.orange_500)
            )
            binding.maxCalories.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.orange_500)
            )
        } else {
            binding.caloriesbar.unfinishedStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.purple_500)
            binding.caloriesbar.finishedStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.purple_700)
            binding.calories.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.purple_700)
            )
            binding.maxCalories.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.purple_700)
            )
        }
        val progress = caloriesOnDay*100/maxCalories
        binding.caloriesbar.progress = min(progress, 100)
    }

    private fun setupCalendar() {
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)

        binding.calendarBlock.setOnClickListener {
            selectDate()
        }
        showDate(day, month, year)
    }

    private fun selectDate() {
        val mDatePicker = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                showDate(selectedDay, selectedMonth, selectedYear)
            }, year, month, day
        )

        mDatePicker.show()
        mDatePicker.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_700))

        mDatePicker.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_700))
    }

    private fun showDate(day: Int, month: Int, year: Int) {
        this.day = day
        this.month = month
        this.year = year
        val format = SimpleDateFormat(App.DATE_FORMAT, Locale.getDefault())
        currentDate.set(year, month, day)
        timestamp = currentDate.timeInMillis
        binding.calendar.text = format.format(currentDate.time)
        homeViewModel.getMeals(timestamp)
        homeViewModel.getProducts(timestamp)
    }

    private fun setupMealsList() {
        mealsAdapter = MealsAdapter(listOf(), listOf(), { pos ->
            addMealLauncher.launch(Intent(context, MealEditorActivity::class.java).apply {
                this.putExtra("mealID", mealsAdapter.getMealId(pos))
                this.putExtra("timestamp", timestamp)
            })
        }, { pos ->
            homeViewModel.removeMeal(mealsAdapter.getMealId(pos)) {
                homeViewModel.getMeals(timestamp)
                homeViewModel.getProducts(timestamp)
            }
        })
        binding.mealList.layoutManager = LinearLayoutManager(context)
        binding.mealList.adapter = mealsAdapter
    }
}