package com.borsch_team.calorycounter.data.dbUtils

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.borsch_team.calorycounter.App

class DatabaseAccess(context: Context) {
    private var openHelper: FoodDatabaseOpenHelper = FoodDatabaseOpenHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private lateinit var instance: DatabaseAccess
    }

    init {
        instance = this
    }

    fun open() {
        database = openHelper.writableDatabase
    }

    fun close() {
        database.close()
    }

    fun findProducts(query: String): Cursor =
        database.rawQuery("SELECT ID, Name, Calories, Proteins, Fats, Carbohydrates FROM data WHERE Name LIKE '%${query}%' LIMIT 30", null)

    fun getProduct(id: Int): Cursor =
        database.rawQuery("SELECT ID, Name, Calories, Proteins, Fats, Carbohydrates FROM data WHERE ID = $id", null)

    fun getProductCalories(id: Int): Cursor =
        database.rawQuery("SELECT Calories FROM data WHERE ID = $id", null)

    fun getProductsData(ids: List<Int>): Cursor {
        var searchIn = "("
        ids.forEach {
            searchIn += "${it},"
        }
        searchIn = searchIn.dropLast(1)
        searchIn += ")"
        return database.rawQuery("SELECT ID, Name, Calories, Proteins, Fats, Carbohydrates FROM data WHERE ID IN $searchIn", null)
    }

    fun getCustomProducts(): Cursor =
        database.rawQuery("SELECT ID, Name, Calories, Proteins, Fats, Carbohydrates FROM data WHERE ID > ${App.CUSTOM_PRODUCTS_INDEX}", null)
}