package com.borsch_team.calorycounter.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borsch_team.calorycounter.data.models.DayStatsModel
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.R
import java.text.SimpleDateFormat
import java.util.*

class CalendarStatsAdapter(private var stats: List<DayStatsModel>):
    RecyclerView.Adapter<CalendarStatsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val calories: TextView = itemView.findViewById(R.id.calories)
        val proteins: TextView = itemView.findViewById(R.id.Proteins)
        val fats: TextView = itemView.findViewById(R.id.fats)
        val carbohydrates: TextView = itemView.findViewById(R.id.carbohydrates)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stats_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val format = SimpleDateFormat(App.STATS_DATE_FORMAT, Locale.getDefault())
        holder.date.text = format.format(stats[position].timestamp)
        holder.calories.text = stats[position].caloriesInDay.toString()
        holder.proteins.text = stats[position].proteinsInDay.toString()
        holder.fats.text = stats[position].fatsInDay.toString()
        holder.carbohydrates.text = stats[position].carbohydratesInDay.toString()
    }

    override fun getItemCount(): Int = stats.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateStats(stats: List<DayStatsModel>) {
        this.stats = stats
        notifyDataSetChanged()
    }
}