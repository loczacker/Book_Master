package com.zacker.bookmaster.ui.home.homeProfile

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

class HomeProfileViewModel:ViewModel() {

    private val _user = MutableLiveData<UsersModel>()
    val user: LiveData<UsersModel>
        get() = _user

    private fun getUserById(userId: String) {
        val call = BookClient.invoke().getUserById(userId)
        call.enqueue(object : Callback<UsersModel> {
            override fun onResponse(call: Call<UsersModel>, response: Response<UsersModel>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let {
                        val userModel = UsersModel(
                            id = it.id,
                            name = it.name,
                            email = it.email,
                            photoURL = it.photoURL,
                            role = it.role,
                            about = it.about
                        )
                        _user.value= userModel
                    }
                } else {
                    Log.e("MyTag", "API was not called.")
                }
            }

            override fun onFailure(call: Call<UsersModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }
}