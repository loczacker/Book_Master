package com.zacker.bookmaster.ui.home.homeBookCase.booksCart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zacker.bookmaster.model.CartsModel
import com.zacker.bookmaster.network.BookClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookCartViewModel : ViewModel() {

    private val _cart = MutableLiveData<CartsModel>()
    val cart: LiveData<CartsModel> get() = _cart

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchCartByEmail(email: String) {
        BookClient().getCartByEmail(email).enqueue(object : Callback<CartsModel> {
            override fun onResponse(call: Call<CartsModel>, response: Response<CartsModel>) {
                if (response.isSuccessful) {
                    _cart.value = response.body()
                } else {
                    _error.value = "Failed to load cart: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<CartsModel>, t: Throwable) {
                _error.value = t.message
            }
        })
    }
}
