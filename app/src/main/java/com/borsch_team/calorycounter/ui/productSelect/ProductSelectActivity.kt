package com.borsch_team.calorycounter.ui.productSelect

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.borsch_team.calorycounter.databinding.ActivityProductSelectBinding
import com.borsch_team.calorycounter.ui.adapters.ProductSearchAdapter

class ProductSelectActivity : AppCompatActivity() {
    private lateinit var viewModel: ProductSelectViewModel
    private lateinit var binding: ActivityProductSelectBinding
    private lateinit var adapter: ProductSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Выбор продукта"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[ProductSelectViewModel::class.java]
        viewModel.products.observe(this) { res ->
            adapter.updateProductsList(res)
        }
        setupAdapter()
        binding.queryInput.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().trim().isNotEmpty()) {
                    viewModel.findProducts(p0.toString().trim())
                }
            }
        })
    }

    private fun setupAdapter() {
        adapter = ProductSearchAdapter(listOf()) { pos ->
            val data = Intent().apply {
                this.putExtra("productId", adapter.getProductId(pos))
            }
            setResult(RESULT_OK, data)
            finish()
        }
        binding.productsList.layoutManager = LinearLayoutManager(this)
        binding.productsList.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}