package com.borsch_team.calorycounter.ui.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borsch_team.calorycounter.R
import com.borsch_team.calorycounter.data.models.MealProduct
import com.borsch_team.calorycounter.utils.Calculators
import com.borsch_team.calorycounter.data.models.Product
import java.lang.Exception

class ProductEditAdapter(var products: List<Product>, private val onItemClicked: (position: Int) -> Unit,
    var mealProducts: List<MealProduct> = listOf(), private val onItemRemoved: (position: Int) -> Unit,):
    RecyclerView.Adapter<ProductEditAdapter.ViewHolder>() {
    var preInitialized = false
    class ViewHolder(itemView: View, val onSearchItemClicked: (position: Int) -> Unit,
                     val onSearchItemRemoved: (position: Int) -> Unit):
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val remove: ImageView = itemView.findViewById(R.id.remove)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val calories: TextView = itemView.findViewById(R.id.calories)
        val proteins: TextView = itemView.findViewById(R.id.Proteins)
        val fats: TextView = itemView.findViewById(R.id.fats)
        val carbohydrates: TextView = itemView.findViewById(R.id.carbohydrates)
        val productWeight: TextView = itemView.findViewById(R.id.productWeight)
        init {
            itemView.setOnClickListener(this)
            remove.setOnClickListener { onSearchItemRemoved(adapterPosition) }
        }
        override fun onClick(p0: View?) {
            onSearchItemClicked(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_edit_item, parent, false)
        return ViewHolder(view, onItemClicked, onItemRemoved)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.productName.text = products[position].Name
        holder.productWeight.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().toInt() >= 0) {
                        if (p0.toString().toInt() >= 10000) {
                            holder.productWeight.text = "9999"
                        }
                        calculateParameters(holder, position)
                    }
                }
            }
        })
        if (preInitialized) {
            holder.productWeight.text = mealProducts[position].foodWeight?.toString()
        }
        calculateParameters(holder, position)
    }

    @SuppressLint("SetTextI18n")
    private fun calculateParameters(holder: ViewHolder, position: Int) {
        holder.calories.text = "${Calculators.calculateCFC(holder.productWeight, products[position].Calories!!)} Ккал"
        holder.proteins.text = "${Calculators.calculateCFC(holder.productWeight, products[position].Proteins!!)} г"
        holder.fats.text = "${Calculators.calculateCFC(holder.productWeight, products[position].Fats!!)} г"
        holder.carbohydrates.text = "${Calculators.calculateCFC(holder.productWeight, products[position].Carbohydrates!!)} г"
    }

    override fun getItemCount(): Int =
        try {
            products.size
        } catch (e: Exception) {
            0
        }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductsList(productsList: List<Product>) {
        this.products = productsList
        this.notifyDataSetChanged()
    }

    fun addProduct(product: Product) {
        products = products.toMutableList().apply {
            this.add(product)
        }.toList()
        mealProducts = mealProducts.toMutableList().apply {
            this.add(
                MealProduct(
                    null, null, product.ID.toString(), 100, null
                )
            )
        }.toList()
        this.notifyItemInserted(products.size-1)
    }

    fun removeProduct(id: Int) {
        products = products.toMutableList().apply {
            this.removeAt(id)
        }.toList()
        mealProducts = mealProducts.toMutableList().apply {
            this.removeAt(id)
        }.toList()
        notifyItemRemoved(id)
    }

    fun getProductId(pos: Int): Int = products[pos].ID!!
    fun getProductsList(): List<MealProduct> = this.mealProducts
}