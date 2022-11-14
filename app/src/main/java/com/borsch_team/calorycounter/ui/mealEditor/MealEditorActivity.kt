package com.borsch_team.calorycounter.ui.mealEditor

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.Meal
import com.borsch_team.calorycounter.data.models.MealProduct
import com.borsch_team.calorycounter.data.models.Result
import com.borsch_team.calorycounter.ui.adapters.ProductEditAdapter
import com.borsch_team.calorycounter.ui.productSelect.ProductSelectActivity
import com.borsch_team.calorycounter.R
import com.borsch_team.calorycounter.databinding.ActivityMealEditorBinding
import java.text.SimpleDateFormat
import java.util.*

class MealEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealEditorBinding
    private lateinit var viewModel: MealEditorViewModel
    private lateinit var adapter: ProductEditAdapter
    private var imported = false
    private var mealProducts: List<MealProduct> = listOf()
    private var dayStamp: Long? = null
    private var mealId: Long? = null

    private lateinit var addProductLauncher: ActivityResultLauncher<Intent>

    private val currentTime: Calendar = Calendar.getInstance(Locale.ROOT)
    private var hour = currentTime.get(Calendar.HOUR_OF_DAY)
    private var minute = currentTime.get(Calendar.MINUTE)
    private var timePicker: TimePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealEditorBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MealEditorViewModel::class.java]
        setContentView(binding.root)
        supportActionBar?.title = "Приём пищи"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupAdapter()
        dayStamp = intent.getLongExtra("timestamp", 0)
        currentTime.timeInMillis = dayStamp!!
        currentTime.set(Calendar.HOUR_OF_DAY, hour)
        currentTime.set(Calendar.MINUTE, minute)
        mealId = intent.getLongExtra("mealID", -1)
        if (mealId != -1L) {
            viewModel.loadMeal(mealId!!.toInt())
            imported = true
        }

        viewModel.result.observe(this) { res ->
            if (res.resultCode == Result.RESULT_SUCCESS) {
                setResult(RESULT_OK)
                finish()
            }
        }

        viewModel.newProduct.observe(this) { product ->
            adapter.addProduct(product)
        }

        viewModel.meal.observe(this) { meal ->
            if (meal != null) {
                binding.mealName.setText(meal.meal.mealName)
                currentTime.timeInMillis = meal.meal.timestamp!!
                hour = currentTime.get(Calendar.HOUR_OF_DAY)
                minute = currentTime.get(Calendar.MINUTE)
                showTime()
                val ids = mutableListOf<Int>()
                meal.products.forEach {
                    ids.add(it.foodID!!.toInt())
                }
                viewModel.loadProducts(ids.toList())
                mealProducts = meal.products
            }
        }

        viewModel.productsData.observe(this) {
            if (mealProducts.size > 0) {
                adapter.mealProducts = this.mealProducts
                adapter.preInitialized = true
                adapter.updateProductsList(it)
            }
        }

        binding.time.setOnClickListener {
            if (timePicker == null) {
                timePicker = TimePickerDialog(this,
                    { _, hour, minute ->
                        currentTime.set(Calendar.HOUR_OF_DAY, hour)
                        currentTime.set(Calendar.MINUTE, minute)
                        showTime()
                    },
                    hour, minute, true)
            }
            timePicker?.show()
        }

        if (mealId != -1L) {
            viewModel.loadMeal(mealId!!.toInt())
        }

        binding.save.setOnClickListener {
            viewModel.saveMealToDatabase(createMealModel(),
                fixProductList(adapter.getProductsList()))
        }

        binding.addProduct.setOnClickListener {
            addProductLauncher.launch(Intent(this, ProductSelectActivity::class.java))
        }

        addProductLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                res ->
            if (res.resultCode == Activity.RESULT_OK) {
                viewModel.getProductData(res.data!!.getIntExtra("productId", 0))
            }
        }
        showTime()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun fixProductList(productsList: List<MealProduct>): List<MealProduct> {
        var pos = 0
        productsList.forEach {
            it.foodWeight = binding.productsList.findViewHolderForLayoutPosition(pos)!!.itemView
                .findViewById<EditText>(R.id.productWeight).text.toString().toInt()
            pos++
        }
        return productsList
    }

    private fun setupAdapter() {
        adapter = ProductEditAdapter(listOf(), {}, listOf(), { pos ->
            adapter.removeProduct(pos)
        })
        binding.productsList.layoutManager = LinearLayoutManager(this)
        binding.productsList.adapter = adapter
    }

    private fun showTime() {
        val format = SimpleDateFormat(App.MEAL_DATE_FORMAT, Locale.getDefault())
        binding.time.text = format.format(currentTime.time)
    }

    private fun createMealModel(): Meal = Meal(
        mealID = if (mealId != -1L) mealId else null,
        timestamp = currentTime.timeInMillis,
        mealName = binding.mealName.text.toString().trim()
    )
}