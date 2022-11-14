package com.borsch_team.calorycounter.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.borsch_team.calorycounter.ui.adapters.DividerItemDecorator
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.R
import com.borsch_team.calorycounter.data.models.Meal
import com.borsch_team.calorycounter.data.models.MealProduct
import java.text.SimpleDateFormat
import java.util.*


class MealsAdapter(private var meals: List<Meal>, private var mealProducts: List<List<MealProduct>>,
    private val onItemClicked: (position: Int) -> Unit, private val onItemRemoved: (position: Int) -> Unit):
    RecyclerView.Adapter<MealsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, val onMealItemClicked: (position: Int) -> Unit,
                     val onMealItemRemoved: (position: Int) -> Unit):
        RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        private val remove: ImageView = itemView.findViewById(R.id.remove)
        val mealTime: TextView = itemView.findViewById(R.id.mealTime)
        val mealName: TextView = itemView.findViewById(R.id.mealName)
        val productsList: RecyclerView = itemView.findViewById(R.id.productsList)
        private val edit: ImageView = itemView.findViewById(R.id.edit)
        lateinit var productsAdapter: ProductViewAdapter
        init {
            itemView.setOnClickListener(this)
            edit.setOnClickListener(this)
            remove.setOnClickListener {onMealItemRemoved(adapterPosition)}
        }
        override fun onClick(p0: View?) {
            onMealItemClicked(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_item, parent, false)
        return ViewHolder(view, onItemClicked, onItemRemoved)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val format = SimpleDateFormat(App.MEAL_DATE_FORMAT, Locale.getDefault())
        holder.mealTime.text = format.format(meals[position].timestamp)
        holder.mealName.text = meals[position].mealName
        holder.productsAdapter = ProductViewAdapter(listOf())
        holder.productsAdapter.updateProductsList(mealProducts[position])
        holder.productsList.adapter = holder.productsAdapter
        holder.productsList.layoutManager = LinearLayoutManager(holder.itemView.context)
        val dividerItemDecoration = DividerItemDecorator(
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_separator)!!)
        holder.productsList.addItemDecoration(dividerItemDecoration)
    }

    override fun getItemCount(): Int =
        try {
            meals.size
        } catch (e: Exception) {
            0
        }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMealsList(mealsList: MutableList<Meal>) {
        this.meals = mealsList
        this.notifyDataSetChanged()
    }

    fun updateMealsProductList(products: List<List<MealProduct>>) {
        this.mealProducts = products
        this.notifyDataSetChanged()
    }

    fun removeItem(id: Int) {
        this.meals.toMutableList().removeAt(id)
        this.mealProducts.toMutableList().removeAt(id)
        this.notifyItemRemoved(id)
    }

    fun getMealId(pos: Int): Long = meals[pos].mealID!!
}