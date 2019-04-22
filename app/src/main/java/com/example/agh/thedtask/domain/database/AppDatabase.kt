package com.example.usecases.database

import android.arch.persistence.room.*
import com.example.agh.thedtask.domain.applicationLiveData
import com.example.agh.thedtask.domain.database.ImageTypeConverter
import com.example.agh.thedtask.domain.database.ProductsDao
import com.example.agh.thedtask.domain.getApplication
import com.example.agh.thedtask.entities.Product


val appDatabase by lazy {

    Room.databaseBuilder(
            applicationLiveData.getApplication(),
            AppDatabase::class.java, "database"
    ).build()

}

@Database(entities = [Product::class ]
, version = 1  , exportSchema = false)
@TypeConverters(  ImageTypeConverter::class)
abstract class AppDatabase : RoomDatabase(){

    abstract val productsDao: ProductsDao
}