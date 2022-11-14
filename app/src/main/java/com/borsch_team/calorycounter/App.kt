package com.borsch_team.calorycounter

import android.app.Application
import androidx.room.Room
import com.borsch_team.calorycounter.data.MeallDatabase
import com.borsch_team.calorycounter.data.ProductsDatabase
import com.borsch_team.calorycounter.data.dbUtils.DatabaseAccess

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        foodDatabaseAccess = DatabaseAccess(applicationContext)
        meallDatabase = Room.databaseBuilder(applicationContext,
            MeallDatabase::class.java, "meal").build()
        productDatabase = Room.databaseBuilder(applicationContext,
            ProductsDatabase::class.java, "foodData")
            .createFromAsset("databases/foodDataPre.db").build()
    }

    companion object {
        lateinit var instance: App
            private set

        lateinit var foodDatabaseAccess: DatabaseAccess
            private set

        lateinit var meallDatabase: MeallDatabase
            private set

        lateinit var productDatabase: ProductsDatabase
            private set

        // Constants
        const val DATE_FORMAT = "dd.MM.yyyy"
        const val MEAL_DATE_FORMAT = "HH:mm"
        const val STATS_DATE_FORMAT = "dd.MM"
        const val CUSTOM_PRODUCTS_INDEX = 100000
    }
}