package com.borsch_team.calorycounter.ui.productEditor

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModelProvider
import com.borsch_team.calorycounter.R
import com.borsch_team.calorycounter.data.models.Product
import com.borsch_team.calorycounter.databinding.ActivityProductEditorBinding
import com.borsch_team.calorycounter.ui.productSelect.ProductSelectViewModel
import java.text.DecimalFormat

class ProductEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductEditorBinding
    private lateinit var viewModel: ProductEditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ProductEditorViewModel::class.java]

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Редактирование продукта"

        binding.save.setOnClickListener {
            if (binding.mealName.text.toString().trim().isNotEmpty() &&
                binding.calories.text.toString().trim().isNotEmpty() &&
                binding.proteins.text.toString().trim().isNotEmpty() &&
                binding.fats.text.toString().trim().isNotEmpty() &&
                binding.carbohydrates.text.toString().trim().isNotEmpty()) {
                viewModel.addCustomProduct(generateProduct()) {
                    setResult(RESULT_OK)
                    finish()
                }
            } else {
                Toast.makeText(this, "Заполните все поля для продолжения!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.calories.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().toInt() >= 0) {
                        if (p0.toString().toInt() > 1000) {
                            binding.calories.setText("1000")
                        }
                    }
                }
            }
        })
        binding.proteins.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().toFloat() >= 0) {
                        if (p0.toString().toFloat() > 100) {
                            binding.proteins.setText("100")
                        }
                    }
                }
            }
        })
        binding.fats.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().toFloat() >= 0) {
                        if (p0.toString().toFloat() > 100) {
                            binding.fats.setText("100")
                        }
                    }
                }
            }
        })
        binding.carbohydrates.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().toInt() >= 0) {
                        if (p0.toString().toInt() > 100) {
                            binding.carbohydrates.setText("100")
                        }
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun generateProduct(): Product = Product(
        null,
        binding.mealName.text.toString().trim(),
        binding.calories.text.toString().toInt(),
        DecimalFormat("#.##")
            .format(binding.proteins.text.toString().replace(',', '.').toFloat()).toFloat(),
        DecimalFormat("#.##")
            .format(binding.fats.text.toString().replace(',', '.').toFloat()).toFloat(),
        binding.carbohydrates.text.toString().toInt()
    )
}