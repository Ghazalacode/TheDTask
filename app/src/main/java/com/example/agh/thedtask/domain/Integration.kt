package com.example.agh.thedtask.domain

import android.app.Application
import android.arch.lifecycle.MutableLiveData


internal val applicationLiveData = MutableLiveData<Application>() // Module privte

internal fun MutableLiveData<Application>.getApplication() = value!!
object Domain {
    fun integrateWith( application: Application){
        applicationLiveData.value =application
    }
}