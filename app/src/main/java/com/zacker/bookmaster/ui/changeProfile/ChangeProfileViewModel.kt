package com.zacker.bookmaster.ui.changeProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zacker.bookmaster.model.UsersModel
import com.zacker.bookmaster.network.BookClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeProfileViewModel : ViewModel() {
    private val _resultUser = MutableLiveData<UsersModel?>()
    val resultUser: LiveData<UsersModel?> = _resultUser

    fun loadUserInfo(email: String) {
        BookClient().getUserByEmail(email).enqueue(object : Callback<UsersModel> {
            override fun onResponse(call: Call<UsersModel>, response: Response<UsersModel>) {
                if (response.isSuccessful) {
                    _resultUser.value = response.body()
                } else {
                    _resultUser.value = null
                }
            }

            override fun onFailure(call: Call<UsersModel>, t: Throwable) {
                _resultUser.value = null
            }
        })
    }

    fun updateUser(id: String, updatedUser: UsersModel) {
        BookClient().updateUser(id, updatedUser).enqueue(object : Callback<UsersModel> {
            override fun onResponse(call: Call<UsersModel>, response: Response<UsersModel>) {
                if (response.isSuccessful) {
                    _resultUser.value = updatedUser
                } else {
                }
            }

            override fun onFailure(call: Call<UsersModel>, t: Throwable) {
            }
        })
    }


}
