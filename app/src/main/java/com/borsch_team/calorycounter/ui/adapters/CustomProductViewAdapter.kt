package com.borsch_team.calorycounter.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.data.models.MealProduct
import com.borsch_team.calorycounter.data.models.Product
import com.borsch_team.calorycounter.extensions.toProductsArray
import com.borsch_team.calorycounter.utils.Calculators
import com.borsch_team.calorycounter.R
import java.lang.Exception

class CustomProductViewAdapter(private var products: List<Product>,
                               private val onItemRemove: (position: Int) -> Unit):
    RecyclerView.Adapter<CustomProductViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, onItemRemove: (position: Int) -> Unit):
        RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val calories: TextView = itemView.findViewById(R.id.calories)
        val proteins: TextView = itemView.findViewById(R.id.Proteins)
        val fats: TextView = itemView.findViewById(R.id.fats)
        val carbohydrates: TextView = itemView.findViewById(R.id.carbohydrates)
        private val remove: ImageView = itemView.findViewById(R.id.remove)
        init {
            remove.setOnClickListener {
                onItemRemove(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_product_view_item, parent, false)
        return ViewHolder(view, onItemRemove)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = products[position].Name
        holder.calories.text = "${Calculators.calculateCFC(100, products[position].Calories!!)} Ккал"
        holder.proteins.text = "${Calculators.calculateCFC(100, products[position].Proteins!!)} г"
        holder.fats.text = "${Calculators.calculateCFC(100, products[position].Fats!!)} г"
        holder.carbohydrates.text = "${Calculators.calculateCFC(100, products[position].Carbohydrates!!)} г"
    }

    override fun getItemCount(): Int =
        try {
            products.size
        } catch (e: Exception) {
            0
        }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductsList(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

    fun removeItem(id: Int) {
        this.products = products.toMutableList().apply {
            this.removeAt(id)
        }
        this.notifyItemRemoved(id)
    }

    fun getProductID(pos: Int): Int = products[pos].ID!!
}