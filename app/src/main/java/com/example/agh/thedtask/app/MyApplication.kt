package com.example.agh.thedtask.app

import android.app.Application
import com.example.agh.thedtask.domain.Domain



class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Domain.integrateWith(this)
    }
}