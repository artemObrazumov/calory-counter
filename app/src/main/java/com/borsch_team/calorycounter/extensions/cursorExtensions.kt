package com.borsch_team.calorycounter.extensions

import android.database.Cursor
import com.borsch_team.calorycounter.data.dbUtils.DatabaseConstants
import com.borsch_team.calorycounter.data.models.Product

fun Cursor.toProductsArray(): ArrayList<Product> {
    val array = ArrayList<Product>()
    while (this.moveToNext()) {
        array.add(
            Product(
            this.getInt(DatabaseConstants.ID_INDEX),
            this.getString(DatabaseConstants.NAME_INDEX),
            this.getInt(DatabaseConstants.CALORIES_INDEX),
            this.getFloat(DatabaseConstants.PROTEINS_INDEX),
            this.getFloat(DatabaseConstants.FATS_INDEX),
            this.getInt(DatabaseConstants.CARBOHYDRATES_INDEX)
        ))
    }
    return array
}

fun Cursor.toProduct(): Product {
    lateinit var product: Product
    this.moveToFirst()
    product = Product(
        this.getInt(DatabaseConstants.ID_INDEX),
        this.getString(DatabaseConstants.NAME_INDEX),
        this.getInt(DatabaseConstants.CALORIES_INDEX),
        this.getFloat(DatabaseConstants.PROTEINS_INDEX),
        this.getFloat(DatabaseConstants.FATS_INDEX),
        this.getInt(DatabaseConstants.CARBOHYDRATES_INDEX)
    )
    return product
}

fun Cursor.toCaloriesInt(): Int {
    this.moveToFirst()
    return this.getInt(0)
}