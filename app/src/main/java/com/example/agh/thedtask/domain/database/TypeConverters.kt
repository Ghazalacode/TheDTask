package com.example.agh.thedtask.domain.database

import android.arch.persistence.room.TypeConverter
import com.example.agh.thedtask.entities.Image
import com.google.gson.Gson

class ImageTypeConverter {

    @TypeConverter
    fun toJson(image : Image) = Gson().toJson(image)

    @TypeConverter
    fun fromJson(string: String) = Gson().fromJson(string, Image::class.java)
}