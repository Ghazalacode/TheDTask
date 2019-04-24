package com.example.agh.thedtask.domain

import android.app.Application
import android.arch.lifecycle.MutableLiveData

// holding application context in Domain Layer
// after integrating it with presentation Layer
internal val applicationLiveData = MutableLiveData<Application>()

internal fun MutableLiveData<Application>.getApplication() = value!!
object Domain {
    fun integrateWith( application: Application){
        applicationLiveData.value =application
    }
}