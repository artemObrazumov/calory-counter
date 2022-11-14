package com.borsch_team.calorycounter.ui.profile

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.borsch_team.calorycounter.R
import com.borsch_team.calorycounter.databinding.ActivityProfileBinding
import com.borsch_team.calorycounter.utils.Calculators
import com.borsch_team.calorycounter.utils.SharedPreferencesUtils

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Редактирование данных"
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            listOf("Мужской", "Женский"))
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.gender.adapter = arrayAdapter
        loadUserData()

        binding.recalculate.setOnClickListener {
            val calories = Calculators.calculateCalories(
                binding.gender.selectedItemPosition+1,
                when(binding.radioGroupActivity.checkedRadioButtonId) {
                    R.id.radioButton_activity_sitting -> 1
                    R.id.radioButton_activity_someActivity -> 2
                    R.id.radioButton_activtiy_veryActivity -> 3
                    else -> 0
                },
                when(binding.radioGroupGoal.checkedRadioButtonId) {
                    R.id.radioButton_goal2 -> 1
                    R.id.radioButton_activity_goal2 -> 2
                    R.id.radioButton_activtiy_goal_3 -> 3
                    else -> 0
                },
                binding.age.text.toString().toInt(),
                binding.height.text.toString().toInt(),
                binding.weight.text.toString().toInt()
            )
            binding.calories.setText(calories.toString())
        }
        binding.save.setOnClickListener {
            SharedPreferencesUtils.saveCalories(binding.calories.text.toString().toInt(), this)
            SharedPreferencesUtils.saveGender(binding.gender.selectedItemPosition+1, this)
            SharedPreferencesUtils.saveActivity(when(binding.radioGroupActivity.checkedRadioButtonId) {
                R.id.radioButton_activity_sitting -> 1
                R.id.radioButton_activity_someActivity -> 2
                R.id.radioButton_activtiy_veryActivity -> 3
                else -> 0
            }, this)
            SharedPreferencesUtils.saveGoal(when(binding.radioGroupGoal.checkedRadioButtonId) {
                R.id.radioButton_goal2 -> 1
                R.id.radioButton_activity_goal2 -> 2
                R.id.radioButton_activtiy_goal_3 -> 3
                else -> 0
            }, this)
            SharedPreferencesUtils.saveAge(binding.age.text.toString().toInt(), this)
            SharedPreferencesUtils.saveHeight(binding.height.text.toString().toInt(), this)
            SharedPreferencesUtils.saveWeight(binding.weight.text.toString().toInt(), this)
            onBackPressed()
        }
    }

    private fun loadUserData() {
        binding.calories.setText(SharedPreferencesUtils.getCalories(this).toString())
        binding.age.setText(SharedPreferencesUtils.getAge(this).toString())
        binding.weight.setText(SharedPreferencesUtils.getWeight(this).toString())
        binding.height.setText(SharedPreferencesUtils.getHeight(this).toString())
        binding.gender.setSelection(SharedPreferencesUtils.getGender(this)-1)
        binding.radioGroupActivity.getChildAt(SharedPreferencesUtils.getActivity(this)-1)
            .apply {
                val button = this as RadioButton
                button.isChecked = true
            }
        binding.radioGroupGoal.getChildAt(SharedPreferencesUtils.getGoal(this)-1)
            .apply {
                val button = this as RadioButton
                button.isChecked = true
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}