package com.zacker.bookmaster.ui.home.homeProfile

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.zacker.bookmaster.model.UsersModel
import com.zacker.bookmaster.network.BookClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeProfileViewModel(
    private val email: String,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _user = MutableLiveData<UsersModel>()
    val user: LiveData<UsersModel>
        get() = _user

    init {
        getUserByEmail(email)
    }

    private fun getUserByEmail(email: String) {
        val call = BookClient.invoke().getUserByEmail(email)
        call.enqueue(object : Callback<UsersModel> {
            override fun onResponse(call: Call<UsersModel>, response: Response<UsersModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _user.value = it
                    }
                } else {
                    Log.e("HomeProfileViewModel", "API response not successful")
                }
            }

            override fun onFailure(call: Call<UsersModel>, t: Throwable) {
                Log.e("HomeProfileViewModel", "API call failed", t)
            }
        })
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        clearSharedPreferences()
    }

    private fun clearSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
