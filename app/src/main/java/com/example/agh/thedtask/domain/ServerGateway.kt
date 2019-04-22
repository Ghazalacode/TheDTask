package com.example.agh.thedtask.domain

import com.example.agh.thedtask.entities.ProductResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


private const val SERVER_BASE_URL = "https://limitless-forest-98976.herokuapp.com"

private val retrofit: Retrofit by lazy {
    Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}

val productsApis: ProductsApis by lazy {
    retrofit.create(ProductsApis::class.java)
}


interface ProductsApis {

    @GET("https://limitless-forest-98976.herokuapp.com")
    fun retrieveProductsList(): Single<ProductResponse>
}