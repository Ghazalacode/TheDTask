package com.example.agh.thedtask.app.features.products

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.agh.thedtask.domain.engine.hasNetwork
import com.example.agh.thedtask.domain.engine.logd
import com.example.agh.thedtask.domain.engine.toMutableLiveData
import com.example.agh.thedtask.domain.repositories.ProductsRepository
import com.example.agh.thedtask.domain.repositories.productsRepository
import com.example.agh.thedtask.domain.usecases.ProductsResult
import com.example.agh.thedtask.domain.usecases.RetrieveProductsUseCase
import com.example.agh.thedtask.domain.usecases.clearProductsTable
import com.example.agh.thedtask.entities.Product
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class ProductsViewModel(

        val retrieveProgress: MutableLiveData<Boolean> = false.toMutableLiveData(),
        val productsResult: ProductsResult = ArrayList<Product>().toMutableLiveData(),
        val toastText: PublishSubject<String> = PublishSubject.create(),
        private val disposables: CompositeDisposable = CompositeDisposable(),
        private val repository: ProductsRepository = productsRepository,
        private val retrieveProductsUseCase: RetrieveProductsUseCase = RetrieveProductsUseCase(
               repository,
                retrieveProgress,
                productsResult
        )) : ViewModel(){

    fun retrieveProducts(context: Context) {
        val networkState = hasNetwork(context)?:false

      if ( ! networkState)  toastText.onNext("Please Check Your Internet Connection")
        Observable.fromCallable { retrieveProductsUseCase(networkState) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({"retrieved successfully"} , {
                  it.message?.apply { logd()
                      if (this.contains("Timeout")) toastText.onNext("Couldn't Retrieve New Data")
                  }
                    retrieveProgress.postValue(false)})
                .also { disposables.add(it) }
    }

    fun clearProducts(){
       Observable.fromCallable { clearProductsTable() }
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe({"cleared successfully".logd()} , { it.message?.logd()
                   retrieveProgress.postValue(false)})
               .also { disposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()

    }


}