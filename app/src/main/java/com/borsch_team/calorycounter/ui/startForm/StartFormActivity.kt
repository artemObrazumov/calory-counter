package com.borsch_team.calorycounter.ui.startForm

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.borsch_team.calorycounter.ui.formResult.FormResultActivity
import com.borsch_team.calorycounter.utils.SharedPreferencesUtils
import com.borsch_team.calorycounter.R
import com.borsch_team.calorycounter.ui.MainActivity
import com.borsch_team.calorycounter.databinding.ActivityStartFormBinding
import com.borsch_team.calorycounter.utils.Calculators

class StartFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartFormBinding
    private var activeFormID = 0
    private var activeGender = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (SharedPreferencesUtils.getCalories(this) != 0) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val repeat = intent.getBooleanExtra("repeat", false)
        if (repeat) {
            showForm(1, 0)
        } else {
            startHelloAnimation()
        }

        binding.maleBlock.setOnClickListener {
            if (activeFormID == 1) {
                it.animate()
                    .translationY(0f).alpha(1f).duration = 100L
                binding.femaleBlock.animate()
                    .translationY(10f).alpha(0.5f).duration = 300L
                activeGender = Calculators.GENDER_MALE

                binding.continue1.isEnabled = true
            }
        }
        binding.femaleBlock.setOnClickListener {
            if (activeFormID == 1) {
                it.animate()
                    .translationY(0f).alpha(1f).duration = 100L
                binding.maleBlock.animate()
                    .translationY(10f).alpha(0.5f).duration = 300L
                activeGender = Calculators.GENDER_FEMALE

                binding.continue1.isEnabled = true
            }
        }

        // Continue Buttons
        binding.continue1.setOnClickListener{
            if (activeGender != 0  && activeFormID == 1 &&
                binding.activityGroup.checkedRadioButtonId != -1) {
                showForm(2, 1)
            } else {
                Toast.makeText(this, "Заполните все поля для продолжения!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.continue2.setOnClickListener{
            if (binding.editTextWeight.text.toString().isNotEmpty() &&
                binding.editTextHeight.text.toString().isNotEmpty() &&
                binding.editTextAge.text.toString().isNotEmpty() &&
                binding.goalGroup.checkedRadioButtonId != -1) {

                if (binding.editTextWeight.text.toString().toInt() > 0 &&
                    binding.editTextHeight.text.toString().toInt() > 0 &&
                    binding.editTextAge.text.toString().toInt() > 0) {
                    showForm(3, 2)
                } else {
                    Toast.makeText(this, "Заполните все поля для продолжения!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Заполните все поля для продолжения!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.editTextAge.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().toInt() >= 0) {
                        if (p0.toString().toInt() > 1000) {
                            binding.editTextAge.setText("1000")
                        }
                    }
                }
            }
        })
        binding.editTextHeight.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().toInt() >= 0) {
                        if (p0.toString().toInt() > 1000) {
                            binding.editTextHeight.setText("1000")
                        }
                    }
                }
            }
        })
        binding.editTextWeight.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().toInt() >= 0) {
                        if (p0.toString().toInt() > 1000) {
                            binding.editTextWeight.setText("1000")
                        }
                    }
                }
            }
        })
    }

    // Animations
    private fun startHelloAnimation() {
        binding.helloBlock.animate()
            .translationY(0f).alpha(1f).duration = 800L

        val timer = object: CountDownTimer(1200, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                startHelloExtraAnimation()
            }
        }
        timer.start()
    }

    private fun startHelloExtraAnimation() {
        binding.helloExtraText.animate()
            .translationY(0f).alpha(1f).duration = 1000L

        val timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                hideHello()
                showForm(1)
            }
        }
        timer.start()
    }

    private fun hideHello() {
        binding.helloBlock.animate()
            .translationY(-20f).alpha(0f).duration = 500L
    }

    private fun showForm(formId: Int, prevFormID: Int = 0) {
        activeFormID = formId
        if (formId == 1 && prevFormID == 0) {
            binding.form1.animate()
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator?) {
                        binding.form1.visibility = View.VISIBLE
                    }
                    override fun onAnimationEnd(p0: Animator?) {}
                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationRepeat(p0: Animator?) {}
                })
                .translationY(0f).alpha(1f).duration = 500L
            binding.form1.z = 10f
        }
        if (formId == 2 && prevFormID == 1) {
            binding.form1.animate()
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator?) {
                        binding.form2.visibility = View.VISIBLE
                    }
                    override fun onAnimationEnd(p0: Animator?) {
                        binding.form1.visibility = View.GONE
                    }
                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationRepeat(p0: Animator?) {}
                })
                .translationY(20f).alpha(0f).duration = 500L
            binding.form2.animate()
                .translationY(0f).alpha(1f).duration = 800L
            binding.form2.z = 10f
            binding.form1.z = 1f
        }
        if (formId == 3 && prevFormID == 2) {
            binding.form1.animate()
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator?) {}
                    override fun onAnimationEnd(p0: Animator?) {
                        binding.form2.visibility = View.GONE
                    }
                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationRepeat(p0: Animator?) {}
                })
                .translationY(20f).alpha(0f).duration = 500L
            val intent = prepareIntent()
            startActivity(intent)
        }
    }

    private fun prepareIntent() =
        Intent(this, FormResultActivity::class.java).apply {
        this.putExtra("gender", activeGender)
        this.putExtra("activityMode", when(binding.activityGroup.checkedRadioButtonId) {
            R.id.radioVeryLow -> Calculators.ACTIVITY_MODE_VERY_LOW
            R.id.radioLow -> Calculators.ACTIVITY_MODE_LOW
            R.id.radioActive -> Calculators.ACTIVITY_MODE_ACTIVE
            else -> {0}
        })
        this.putExtra("goal", when(binding.goalGroup.checkedRadioButtonId) {
            R.id.radioLoseWeight -> Calculators.GOAL_LOSE_WEIGHT
            R.id.radioSaveWeight -> Calculators.GOAL_SAVE_WEIGHT
            R.id.radioGetWeight -> Calculators.GOAL_GET_WEIGHT
            else -> {0}
        })
        this.putExtra("age", binding.editTextAge.text.toString().toInt())
        this.putExtra("height", binding.editTextHeight.text.toString().toInt())
        this.putExtra("weight", binding.editTextWeight.text.toString().toInt())
    }
}