package com.borsch_team.calorycounter.ui.formResult

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.borsch_team.calorycounter.utils.SharedPreferencesUtils
import com.borsch_team.calorycounter.databinding.ActivityFormResultBinding
import com.borsch_team.calorycounter.ui.MainActivity
import com.borsch_team.calorycounter.ui.mealEditor.FormResultViewModel
import com.borsch_team.calorycounter.ui.startForm.StartFormActivity
import com.borsch_team.calorycounter.utils.Calculators


class FormResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormResultBinding
    private lateinit var viewModel: FormResultViewModel
    private var calories: Int? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[FormResultViewModel::class.java]
        calories =
            Calculators.calculateCalories(
                intent.getIntExtra("gender", 0),
                intent.getIntExtra("activityMode", 0),
                intent.getIntExtra("goal", 0),
                intent.getIntExtra("age", 0),
                intent.getIntExtra("height", 0),
                intent.getIntExtra("weight", 0)
            )
        viewModel.startUpdate(calories!!)
        viewModel.caloriesUpdateValue.observe(this) { counter ->
            binding.res.text = "Рекомендовано: $counter Ккал"
            if (counter == calories!!) {
                binding.tip.animate()
                    .translationY(0f).alpha(1f).duration = 800L
                binding.linearLayout2.animate()
                    .translationY(0f).alpha(1f).duration = 800L
                binding.ContinueButton.animate()
                    .translationY(0f).alpha(1f).duration = 800L
            }
        }
        binding.res.animate()
            .translationY(0f).alpha(1f).duration = 400L

        binding.EditButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val text = EditText(this)
            text.setText(calories!!.toString())
            text.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setTitle("Редактирование калорий")
            builder.setMessage("Введите новое значение калорий")
            builder.setView(text)

            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                calories = text.text.toString().toInt()
                binding.res.text = "Рекомендовано: $calories Ккал"
            }

            builder.setNegativeButton(android.R.string.no) { _, _ -> }
            builder.show()
        }
        binding.RecalculateButton.setOnClickListener {
            startActivity(Intent(this, StartFormActivity::class.java).apply {
                this.putExtra("repeat", true)
            })
        }
        binding.ContinueButton.setOnClickListener {
            SharedPreferencesUtils.saveCalories(calories!!, this)
            SharedPreferencesUtils.saveGender(intent.getIntExtra("gender", 0), this)
            SharedPreferencesUtils.saveActivity(intent.getIntExtra("activityMode", 0), this)
            SharedPreferencesUtils.saveGoal(intent.getIntExtra("goal", 0), this)
            SharedPreferencesUtils.saveAge(intent.getIntExtra("age", 0), this)
            SharedPreferencesUtils.saveHeight(intent.getIntExtra("height", 0), this)
            SharedPreferencesUtils.saveWeight(intent.getIntExtra("weight", 0), this)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}