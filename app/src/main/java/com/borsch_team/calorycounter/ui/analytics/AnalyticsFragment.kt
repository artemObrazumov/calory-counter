package com.borsch_team.calorycounter.ui.slideshow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.borsch_team.calorycounter.data.models.DayStatsModel
import com.borsch_team.calorycounter.ui.adapters.CalendarStatsAdapter
import com.borsch_team.calorycounter.App
import com.borsch_team.calorycounter.databinding.FragmentSlideshowBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentSlideshowBinding
    private lateinit var slideshowViewModel: AnalyticsViewModel
    private lateinit var adapter: CalendarStatsAdapter
    private val currentDate: Calendar = Calendar.getInstance(Locale.ROOT)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        slideshowViewModel =
            ViewModelProvider(this)[AnalyticsViewModel::class.java]
        binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        setupAdapter()
        slideshowViewModel.prodStats.observe(viewLifecycleOwner) {
            adapter.updateStats(it)
            setStats(it)
            createGraphs(it)
        }
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
            listOf("За неделю", "За месяц"))
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                slideshowViewModel.getStats(currentDate.timeInMillis, pos)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setStats(it: List<DayStatsModel>) {
        var caloriesSum = 0
        var proteinsSum = 0f
        var fatsSum = 0f
        var carbohydratesSum = 0
        it.forEach { model ->
            caloriesSum += model.caloriesInDay
            proteinsSum += model.proteinsInDay
            fatsSum += model.fatsInDay
            carbohydratesSum += model.carbohydratesInDay
        }
        binding.caloriesSum.text = "$caloriesSum Ккал"
        binding.ProteinsSum.text = "${DecimalFormat("#.##").format(proteinsSum)} г"
        binding.fatsSum.text = "${DecimalFormat("#.##").format(fatsSum)} г"
        binding.carbohydratesSum.text = "$carbohydratesSum"
        val days = it.size
        binding.caloriesAverage.text =
            "${DecimalFormat("#.##").format(caloriesSum.toFloat()/days)} Ккал"
        binding.ProteinsAverage.text =
            "${DecimalFormat("#.##").format(proteinsSum/days)} г"
        binding.fatsAverage.text =
            "${DecimalFormat("#.##").format(fatsSum/days)} г"
        binding.carbohydratesAverage.text =
            "${DecimalFormat("#.##").format(carbohydratesSum.toFloat()/days)} г"
    }

    private fun setupAdapter() {
        adapter = CalendarStatsAdapter(listOf())
        binding.statsList.layoutManager = LinearLayoutManager(requireContext())
        binding.statsList.adapter = adapter
    }

    private fun createGraphs(it: List<DayStatsModel>) {
        createCaloriesGraph(it)
        createProteinsGraph(it)
        createFatsGraph(it)
        createCarbohydratesGraph(it)
    }

    private fun createCaloriesGraph(data: List<DayStatsModel>) {
        val datesLabels = mutableListOf<String>()
        val caloriesData = mutableListOf<Int>()
        val format = SimpleDateFormat(App.STATS_DATE_FORMAT, Locale.getDefault())
        data.forEach { model ->
            datesLabels.add(format.format(model.timestamp))
            caloriesData.add(model.caloriesInDay)
        }
        var i = 0
        val entries = mutableListOf<Entry>()
        caloriesData.forEach{
            entries.add(
                Entry(i.toFloat(), it.toFloat())
            )
            i++
        }
        binding.caloriesGraph.data = LineData(
            LineDataSet(entries, "а").apply {
                this.setDrawFilled(true)
            }
        )
        binding.caloriesGraph.setScaleEnabled(false)
        binding.caloriesGraph.description.isEnabled = false
        binding.caloriesGraph.setDrawGridBackground(false)
        binding.caloriesGraph.axisRight.isEnabled = false
        binding.caloriesGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.caloriesGraph.xAxis.setDrawLabels(true)
        binding.caloriesGraph.xAxis.setDrawAxisLine(false)
        binding.caloriesGraph.xAxis.valueFormatter = IndexAxisValueFormatter(datesLabels)
        binding.caloriesGraph.legend.isEnabled = false
        binding.caloriesGraph.animateY(500)
    }

    private fun createProteinsGraph(data: List<DayStatsModel>) {
        val datesLabels = mutableListOf<String>()
        val proteinsData = mutableListOf<Float>()
        val format = SimpleDateFormat(App.STATS_DATE_FORMAT, Locale.getDefault())
        data.forEach { model ->
            datesLabels.add(format.format(model.timestamp))
            proteinsData.add(model.proteinsInDay)
        }
        var i = 0
        val entries = mutableListOf<Entry>()
        proteinsData.forEach{
            entries.add(
                Entry(i.toFloat(), it)
            )
            i++
        }
        binding.proteinsGraph.data = LineData(
            LineDataSet(entries, "а").apply {
                this.setDrawFilled(true)
            }
        )
        binding.proteinsGraph.setScaleEnabled(false)
        binding.proteinsGraph.description.isEnabled = false
        binding.proteinsGraph.setDrawGridBackground(false)
        binding.proteinsGraph.axisRight.isEnabled = false
        binding.proteinsGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.proteinsGraph.xAxis.setDrawLabels(true)
        binding.proteinsGraph.xAxis.setDrawAxisLine(false)
        binding.proteinsGraph.xAxis.valueFormatter = IndexAxisValueFormatter(datesLabels)
        binding.proteinsGraph.legend.isEnabled = false
        binding.proteinsGraph.animateY(500)
    }

    private fun createFatsGraph(data: List<DayStatsModel>) {
        val datesLabels = mutableListOf<String>()
        val fatsData = mutableListOf<Float>()
        val format = SimpleDateFormat(App.STATS_DATE_FORMAT, Locale.getDefault())
        data.forEach { model ->
            datesLabels.add(format.format(model.timestamp))
            fatsData.add(model.fatsInDay)
        }
        var i = 0
        val entries = mutableListOf<Entry>()
        fatsData.forEach{
            entries.add(
                Entry(i.toFloat(), it)
            )
            i++
        }
        binding.fatsGraph.data = LineData(
            LineDataSet(entries, "а").apply {
                this.setDrawFilled(true)
            }
        )
        binding.fatsGraph.setScaleEnabled(false)
        binding.fatsGraph.description.isEnabled = false
        binding.fatsGraph.setDrawGridBackground(false)
        binding.fatsGraph.axisRight.isEnabled = false
        binding.fatsGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.fatsGraph.xAxis.setDrawLabels(true)
        binding.fatsGraph.xAxis.setDrawAxisLine(false)
        binding.fatsGraph.xAxis.valueFormatter = IndexAxisValueFormatter(datesLabels)
        binding.fatsGraph.legend.isEnabled = false
        binding.fatsGraph.animateY(500)
    }

    private fun createCarbohydratesGraph(data: List<DayStatsModel>) {
        val datesLabels = mutableListOf<String>()
        val carbohydratesData = mutableListOf<Int>()
        val format = SimpleDateFormat(App.STATS_DATE_FORMAT, Locale.getDefault())
        data.forEach { model ->
            datesLabels.add(format.format(model.timestamp))
            carbohydratesData.add(model.carbohydratesInDay)
        }
        var i = 0
        val entries = mutableListOf<Entry>()
        carbohydratesData.forEach{
            entries.add(
                Entry(i.toFloat(), it.toFloat())
            )
            i++
        }
        binding.carbohydratesGraph.data = LineData(
            LineDataSet(entries, "а").apply {
                this.setDrawFilled(true)
            }
        )
        binding.carbohydratesGraph.setScaleEnabled(false)
        binding.carbohydratesGraph.description.isEnabled = false
        binding.carbohydratesGraph.setDrawGridBackground(false)
        binding.carbohydratesGraph.axisRight.isEnabled = false
        binding.carbohydratesGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.carbohydratesGraph.xAxis.setDrawLabels(true)
        binding.carbohydratesGraph.xAxis.setDrawAxisLine(false)
        binding.carbohydratesGraph.xAxis.valueFormatter = IndexAxisValueFormatter(datesLabels)
        binding.carbohydratesGraph.legend.isEnabled = false
        binding.carbohydratesGraph.animateY(500)
    }
}