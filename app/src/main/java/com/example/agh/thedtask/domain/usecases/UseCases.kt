package com.example.agh.thedtask.domain.usecases

import android.arch.lifecycle.MutableLiveData
import com.example.agh.thedtask.domain.engine.logd
import com.example.agh.thedtask.domain.repositories.ProductsRepository
import com.example.agh.thedtask.domain.repositories.productsRepository
import com.example.agh.thedtask.entities.Product
import com.example.usecases.database.appDatabase
import io.reactivex.subjects.PublishSubject


typealias ProductsResult = MutableLiveData<List<Product>>

class RetrieveProductsUseCase(
        private val repository: ProductsRepository = productsRepository,
        private val retrieving: MutableLiveData<Boolean>,
        private val result: ProductsResult

) {

    operator fun invoke(connected: Boolean, isDBEmpty: Boolean = repository.isDatabaseEmpty()) {

        //choosing source of data based on network state and cached data
       if (connected and isDBEmpty) retrieveFromServer() else if (!(!connected and isDBEmpty)) retrieveFromDatabase()

    }

    private fun retrieveFromServer() {
        val retrievingState = retrieving.value ?: true
        // if not already retrieving retrieve data from server
        // and update cached data
        retrievingState
               .takeUnless {  it }
                ?.also { retrieving.postValue(true) }
                ?.let { repository.retrieveProductsListFromServer().blockingGet() }
                ?.also { result.postValue(it) }
                ?.also { repository.clearProducts() }
                ?.also { repository.saveProductsListToDatabase(it) }
                ?.also { retrieving.postValue(false) }
    }

    private fun retrieveFromDatabase() {
        val retrievingState = retrieving.value ?: true
        retrievingState
                .takeUnless { it }
                ?.also { retrieving.postValue(true) }
                ?.let { repository.retrieveProductsListFromDatabase().blockingGet() }
                ?.also { result.postValue(it) }
                ?.also { retrieving.postValue(false) }

    }
}


fun clearProductsTable() {
    appDatabase.productsDao.clearAll()
}
