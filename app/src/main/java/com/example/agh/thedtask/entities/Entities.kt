package com.example.agh.thedtask.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.io.Serializable

data class ProductResponse(
    val data: List<Product>
)
@Entity
data class Product(
    @field:NonNull
    @field:PrimaryKey
    val id: Int,
    val image: Image,
    val name: String,
    val price: Int,
    val productDescription: String
) : Serializable

data class Image(
    val height: String,
    val link: String,
    val width: String
):Serializable