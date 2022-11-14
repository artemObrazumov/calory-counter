package com.borsch_team.calorycounter.data.dbUtils

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class FoodDatabaseOpenHelper(context: Context) :
    SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "foodData"
        private const val DATABASE_VERSION = 1
    }
}