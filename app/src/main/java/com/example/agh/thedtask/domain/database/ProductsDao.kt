package com.example.agh.thedtask.domain.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.agh.thedtask.entities.Product
import io.reactivex.Single


@Dao
interface ProductsDao{
    @Query( "  select * from Product ")
    fun queryAll(): Single<List<Product>>

    @Query( "  select count(*) from Product ")
    fun retrieveProductCount(): Int

    @Insert
    @JvmSuppressWildcards
    fun saveAll(objects: List<Product>)


    @Query("DELETE FROM Product ")
    fun clearAll()


}