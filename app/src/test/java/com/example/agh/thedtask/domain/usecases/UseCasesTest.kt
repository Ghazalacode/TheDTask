package com.example.agh.thedtask.domain.usecases

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.hardware.Camera
import com.example.agh.thedtask.app.features.products.ProductsViewModel
import com.example.agh.thedtask.domain.engine.toMutableLiveData
import com.example.agh.thedtask.domain.repositories.ProductsRepository
import com.example.agh.thedtask.entities.Image
import com.example.agh.thedtask.entities.Product
import com.example.usecases.database.AppDatabase
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class UseCasesTest{
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    val repository =  mock<ProductsRepository>{

        on { retrieveProductsListFromDatabase() } doReturn
                Single.just(listOf<Product>(Product(1 , Image("" ,"" ,"") , "a" ,1 , "")))

        on { retrieveProductsListFromServer() } doReturn
                Single.just(listOf<Product>(Product(1 , Image("" ,"" ,"") , "a" ,1 , ""),
                        Product(1 , Image("" ,"" ,"") , "a" ,1 , "")
                ))
    }

 @Test
    fun  `  When Database is Empty and connected retrieve result From Server` () {
        //Arrange ( initialize needed paramaters  )
 val retrieving = false.toMutableLiveData()
        val productsResult = listOf<Product>().toMutableLiveData()

        // Act (  Action  )
        val useCase =  RetrieveProductsUseCase(repository,retrieving,productsResult).invoke(true ,true)

        //Assert
        val newValue =    productsResult.value

      //  verify(useCase,times(0)).retrieveFromDatabase()

        Assert.assertEquals(true ,productsResult.value?.size==2 )
    }

    @Test
    fun  `  When Database is Not Empty and connected retrieve result From Database` () {
        //Arrange ( initialize needed paramaters  )
        val retrieving = false.toMutableLiveData()
        val productsResult = listOf<Product>().toMutableLiveData()

        // Act (  Action  )
        val useCase =  RetrieveProductsUseCase(repository,retrieving,productsResult)
                .invoke(true ,false)

        //Assert
        val newValue =    productsResult.value

        //  verify(useCase,times(0)).retrieveFromDatabase()

        Assert.assertEquals(true ,productsResult.value?.size==1 )
    }
    @Test
    fun  `  When Database is Not Empty and notConnected retrieve result From Database` () {
        //Arrange ( initialize needed paramaters  )
        val retrieving = false.toMutableLiveData()
        val productsResult = listOf<Product>().toMutableLiveData()

        // Act (  Action  )
        val useCase =  RetrieveProductsUseCase(repository,retrieving,productsResult)
                .invoke(false ,false)

        //Assert
        val newValue =    productsResult.value

        //  verify(useCase,times(0)).retrieveFromDatabase()

        Assert.assertEquals(true ,productsResult.value?.size==1 )
    }
    @Test
    fun  `  When Database is Empty and notConnected Don't update result` () {
        //Arrange ( initialize needed paramaters  )
        val retrieving = false.toMutableLiveData()
        val productsResult = listOf<Product>().toMutableLiveData()

        // Act (  Action  )
        val useCase =  RetrieveProductsUseCase(repository,retrieving,productsResult)
                .invoke(false ,true)

        //Assert
        val newValue =    productsResult.value

        //  verify(useCase,times(0)).retrieveFromDatabase()

        Assert.assertEquals(true ,productsResult.value?.size==0 )
    }
    @Test
    fun  `  When already retrieving don't update resultLiveData value` () {
        //Arrange ( initialize needed paramaters  )
        val productsResult = listOf<Product>().toMutableLiveData()
        val oldValue = productsResult.value
        val retrieving = true.toMutableLiveData()


        // Act (  Action  )
        RetrieveProductsUseCase(repository,retrieving,productsResult).invoke(false ,false)

        //Assert
        val newValue =    productsResult.value

        Assert.assertEquals(oldValue ,newValue )
    }



}