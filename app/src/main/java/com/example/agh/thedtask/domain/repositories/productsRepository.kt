package com.example.agh.thedtask.domain.repositories

import com.example.agh.thedtask.domain.ProductsApis
import com.example.agh.thedtask.domain.productsApis
import com.example.agh.thedtask.entities.Product
import com.example.usecases.database.AppDatabase
import com.example.usecases.database.appDatabase


val productsRepository: ProductsRepository by lazy { ProductsRepository() }
class ProductsRepository(
        private val productsApi: ProductsApis = productsApis ,
        private val database: AppDatabase = appDatabase
)   {

     fun retrieveProductsListFromServer() = productsApi.retrieveProductsList().map {it.data  }
     fun retrieveProductsListFromDatabase() = database.productsDao.queryAll()
     fun isDatabaseEmpty()= database.productsDao.retrieveProductCount() <= 0
     fun saveProductsListToDatabase(products: List<Product>) = database.productsDao.saveAll(products)
     fun clearProducts() = database.productsDao.clearAll()

}