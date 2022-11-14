package com.borsch_team.calorycounter.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.R
import com.borsch_team.calorycounter.data.models.MealProduct
import com.borsch_team.calorycounter.data.models.Product
import com.borsch_team.calorycounter.extensions.toProduct
import com.borsch_team.calorycounter.extensions.toProductsArray
import com.borsch_team.calorycounter.utils.Calculators
import java.lang.Exception

class ProductViewAdapter(private var products: List<MealProduct>):
    RecyclerView.Adapter<ProductViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        val foodWeight: TextView = itemView.findViewById(R.id.foodWeight)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val calories: TextView = itemView.findViewById(R.id.calories)
        val proteins: TextView = itemView.findViewById(R.id.Proteins)
        val fats: TextView = itemView.findViewById(R.id.fats)
        val carbohydrates: TextView = itemView.findViewById(R.id.carbohydrates)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_view_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = findProductData(products[position].foodID!!.toInt())
        holder.foodWeight.text = "${products[position].foodWeight} г"
        holder.productName.text = product.Name
        holder.calories.text = "${Calculators.calculateCFC(products[position].foodWeight!!, product.Calories!!)} Ккал"
        holder.proteins.text = "${Calculators.calculateCFC(products[position].foodWeight!!, product.Proteins!!)} г"
        holder.fats.text = "${Calculators.calculateCFC(products[position].foodWeight!!, product.Fats!!)} г"
        holder.carbohydrates.text = "${Calculators.calculateCFC(products[position].foodWeight!!, product.Carbohydrates!!)} г"
    }

    override fun getItemCount(): Int =
        try {
            products.size
        } catch (e: Exception) {
            0
        }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductsList(products: List<MealProduct>) {
        this.products = products
        this.notifyDataSetChanged()
    }

    private fun findProductData(id: Int): Product {
        App.foodDatabaseAccess.open()
        val product = App.foodDatabaseAccess.getProduct(id).toProduct()
        App.foodDatabaseAccess.close()
        return product
    }
}