package com.zacker.bookmaster.ui.homeProfile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zacker.bookmaster.util.Const

class HomeProfileViewModelFactory(
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeProfileViewModel::class.java)) {
            val email = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
            return HomeProfileViewModel(email, sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
