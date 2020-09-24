package com.rocappdev.commercelist

import android.app.Application

class CommerceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CommerceApplication
    }
}