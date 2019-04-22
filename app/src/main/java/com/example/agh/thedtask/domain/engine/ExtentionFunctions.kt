package com.example.agh.thedtask.domain.engine

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.annotation.GlideModule

fun <T> T.toMutableLiveData(): MutableLiveData<T> {
    return MutableLiveData<T>()
            .also { it.value = this }
}

fun Any.logd(){
    Log.d(this::class.java.simpleName, this.toString())
}

fun Any.toast(context: Context , length:Int = Toast.LENGTH_LONG) =
        Toast.makeText(context, this.toString()
                , length).show()



