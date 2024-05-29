package com.zacker.bookmaster.ui.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zacker.bookmaster.util.Const
import com.zacker.bookmaster.util.Event
import com.zacker.bookmaster.util.SingleLiveEvent

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val resultData = MutableLiveData(false)
    private val prefs: SharedPreferences = application.getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)

    private val _startRegister = SingleLiveEvent<Boolean>()
    val startRegister: SingleLiveEvent<Boolean>
        get() = _startRegister

    private val _showToast = MutableLiveData<Event<Boolean>>()
    val showToast: LiveData<Event<Boolean>>
        get() = _showToast

    private fun showToast() {
        _showToast.value = Event(true) // Chạy ngay lập tức
        _showToast.postValue(Event(true)) // Handler.post chạy sau một chút nhưng không ảnh hưởng đến hiệu năng
    }

    fun login(email: String, pass: String) {
        val auth: FirebaseAuth = Firebase.auth
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserEmail(email)
                    resultData.postValue(true)
                    showToast()
                } else {
                    resultData.postValue(false)
                }
            }
    }

    fun showRegister() {
        _startRegister.value = true
    }

    private fun saveUserEmail(email: String) {
        val editor = prefs.edit()
        editor.putBoolean(Const.KEY_LOGIN, true)
        editor.putString(Const.KEY_EMAIL_USER, email)
        editor.apply()
        Log.d("LoginViewModel", "Saved email: $email")
    }
}