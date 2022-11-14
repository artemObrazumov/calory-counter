package com.borsch_team.calorycounter.utils

import android.widget.TextView
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.roundToInt

class Calculators {
    companion object {
        fun calculateCFC(productWeight: TextView, value: Int, toInt: Boolean = false): String {
            val answer = value * (productWeight.text.toString().toFloat()/100)
            val decimalFormat = DecimalFormat("#.##")
            return if (toInt) {
                decimalFormat.format(answer)
            } else {
                answer.toInt().toString()
            }
        }
        fun calculateCFC(productWeight: TextView, value: Float): String {
            val answer = value * (productWeight.text.toString().toFloat()/100)
            val decimalFormat = DecimalFormat("#.##")
            return decimalFormat.format(answer)
        }

        fun calculateCFC(g: Int, value: Int, toInt: Boolean = false): String {
            val answer = value * (g.toFloat()/100)
            val decimalFormat = DecimalFormat("#.##")
            return if (toInt) {
                decimalFormat.format(answer)
            } else {
                answer.toInt().toString()
            }
        }
        fun calculateCFC(g: Int, value: Float): String {
            val answer = value * (g.toFloat()/100)
            val decimalFormat = DecimalFormat("#.##")
            return decimalFormat.format(answer)
        }

        const val GENDER_MALE = 1
        const val GENDER_FEMALE = 2

        const val ACTIVITY_MODE_VERY_LOW = 1
        const val ACTIVITY_MODE_LOW = 2
        const val ACTIVITY_MODE_ACTIVE = 3

        const val GOAL_LOSE_WEIGHT = 1
        const val GOAL_SAVE_WEIGHT = 2
        const val GOAL_GET_WEIGHT = 3

        fun calculateCalories(gender: Int, activityMode: Int, goal: Int,
                              age: Int, height: Int, weight: Int, floor: Boolean = true): Int {
            val activityKoeff: Float = when(activityMode) {
                ACTIVITY_MODE_VERY_LOW -> 1.2f
                ACTIVITY_MODE_LOW -> 1.55f
                ACTIVITY_MODE_ACTIVE -> 1.7f
                else -> 1f
            }
            val goalKoeff: Float = when(goal) {
                GOAL_LOSE_WEIGHT -> 0.85f
                GOAL_SAVE_WEIGHT -> 1f
                GOAL_GET_WEIGHT -> 1.15f
                else -> 1f
            }
            val calories: Int = when(gender) {
                GENDER_MALE ->
                    ((((10 * weight) + (6.25 * height) - (5 * age)+5) * activityKoeff).roundToInt() * goalKoeff ).toInt()
                GENDER_FEMALE ->
                    ((((10 * weight) + (6.25 * height) - (5 * age)-161) * activityKoeff).roundToInt() * goalKoeff).toInt()
                else -> {0}
            }
            return max(if (floor) calories/10*10 else calories, 1200)
        }
    }
}