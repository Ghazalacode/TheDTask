package com.example.agh.thedtask.domain

import com.example.agh.thedtask.entities.ProductResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


private const val SERVER_BASE_URL = "https://limitless-forest-98976.herokuapp.com"

private val retrofit: Retrofit by lazy {

    val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
}

val productsApis: ProductsApis by lazy {
    retrofit.create(ProductsApis::class.java)
}


interface ProductsApis {

    @GET("https://limitless-forest-98976.herokuapp.com")
    fun retrieveProductsList(): Single<ProductResponse>
}