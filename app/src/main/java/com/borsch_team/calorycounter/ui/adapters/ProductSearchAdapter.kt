package com.borsch_team.calorycounter.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borsch_team.calorycounter.R
import com.borsch_team.calorycounter.data.models.Product
import java.lang.Exception

class ProductSearchAdapter(private var products: List<Product>, private val onItemClicked: (position: Int) -> Unit):
    RecyclerView.Adapter<ProductSearchAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, val onSearchItemClicked: (position: Int) -> Unit):
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val calories: TextView = itemView.findViewById(R.id.calories)
        val proteins: TextView = itemView.findViewById(R.id.Proteins)
        val fats: TextView = itemView.findViewById(R.id.fats)
        val carbohydrates: TextView = itemView.findViewById(R.id.carbohydrates)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            onSearchItemClicked(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_search_item, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = products[position].Name
        holder.calories.text = "${products[position].Calories} Ккал"
        holder.proteins.text = "${products[position].Proteins} г"
        holder.fats.text = "${products[position].Fats} г"
        holder.carbohydrates.text = "${products[position].Carbohydrates} г"
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

    fun getProductId(pos: Int): Int = products[pos].ID!!
}