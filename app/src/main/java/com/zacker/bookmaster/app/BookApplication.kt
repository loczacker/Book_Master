package com.zacker.bookmaster.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.stripe.android.PaymentConfiguration

class BookApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51PBwbkGLkrLd9UE1xdqBdETAWtx8hlKmjVLuW37ojuhpXtI74mQuwUHtJley8XZKNEeCL0ymcCNJe0JeSoViV3LG004pwoepwz"
        )
    }
}