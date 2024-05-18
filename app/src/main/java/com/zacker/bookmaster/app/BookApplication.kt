package com.zacker.bookmaster.app

import android.app.Application
import com.google.firebase.FirebaseApp

class BookApplication : Application() {
    private var backPressedCount = 0
    private var backPressedTime = 0L

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}