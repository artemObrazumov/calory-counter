package com.borsch_team.calorycounter.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils {
    companion object {
        private const val SHARED_PREFERENCES_FILE = "caloriesCounterData"
        private const val CALORIES = "calories"
        private const val GENDER = "gender"
        private const val ACTIVITY = "activity"
        private const val GOAL = "goal"
        private const val AGE = "age"
        private const val HEIGHT = "height"
        private const val WEIGHT = "weight"

        fun saveCalories(calories: Int, context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putInt(CALORIES, calories).apply()
        }
        fun getCalories(context: Context): Int {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt(CALORIES, 0)
        }

        fun saveGender(gender: Int, context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putInt(GENDER, gender).apply()
        }
        fun getGender(context: Context): Int {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt(GENDER, 0)
        }

        fun saveActivity(activity: Int, context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putInt(ACTIVITY, activity).apply()
        }
        fun getActivity(context: Context): Int {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt(ACTIVITY, 0)
        }

        fun saveGoal(goal: Int, context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putInt(GOAL, goal).apply()
        }
        fun getGoal(context: Context): Int {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt(GOAL, 0)
        }

        fun saveAge(age: Int, context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putInt(AGE, age).apply()
        }
        fun getAge(context: Context): Int {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt(AGE, 0)
        }

        fun saveHeight(height: Int, context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putInt(HEIGHT, height).apply()
        }
        fun getHeight(context: Context): Int {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt(HEIGHT, 0)
        }

        fun saveWeight(weight: Int, context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putInt(WEIGHT, weight).apply()
        }
        fun getWeight(context: Context): Int {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt(WEIGHT, 0)
        }
    }
}