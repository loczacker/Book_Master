package com.zacker.bookmaster.ui.register

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zacker.bookmaster.model.UsersModel
import com.zacker.bookmaster.network.BookClient
import com.zacker.bookmaster.util.Const
import com.zacker.bookmaster.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    val resultData = MutableLiveData(false)
    private val prefs: SharedPreferences = application.getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)

    private val _showToast = MutableLiveData<Event<Boolean>>()
    val showToast: LiveData<Event<Boolean>>
        get() = _showToast

    private val _showToast1 = MutableLiveData<Event<Boolean>>()
    val showToast1: LiveData<Event<Boolean>>
        get() = _showToast1

    private val apiService = BookClient.invoke()

    fun register(email: String, pass: String, userName: String) {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    postNewUserToApi(email, userName)
                } else {
                    showToastUnsuccessful()
                }
            }
            .addOnFailureListener { e ->
                showToastUnsuccessful()
            }
    }

    private fun postNewUserToApi(email: String, userName: String) {
        val auth = Firebase.auth
        val newUser =
            UsersModel(_id = auth.currentUser!!.uid , email = email, name = userName, role = "user",
                photoURL = "", phone = "",
                address = "", about = "")
        apiService.postNewUser(newUser).enqueue(object : Callback<UsersModel> {
            override fun onResponse(call: Call<UsersModel>, response: Response<UsersModel>) {
                if (response.isSuccessful) {
                    saveUserEmail(email, userName)
                    showToastSuccessful()
                    resultData.postValue(true)
                } else {
                    showToastUnsuccessful()
                }
            }

            override fun onFailure(call: Call<UsersModel>, t: Throwable) {
                showToastUnsuccessful()
            }
        })
    }

    private fun saveUserEmail(email: String,  userName: String) {
        val editor = prefs.edit()
        editor.putBoolean(Const.KEY_LOGIN, true)
        editor.putString(Const.KEY_EMAIL_USER, email)
        editor.putString(Const.KEY_NAME_USER, userName)
        editor.apply()
        Log.d("RegisterViewModel", "Saved email: $email, $userName")
    }

    private fun showToastSuccessful() {
        _showToast.postValue(Event(true))
    }

    private fun showToastUnsuccessful() {
        _showToast1.postValue(Event(true))
    }
}
