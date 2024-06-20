package com.zacker.bookmaster.ui.favouriteBooks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.network.BookClient
import kotlinx.coroutines.launch

class FavouriteViewModel: ViewModel() {

    private val _favouriteBooks = MutableLiveData<List<BooksModel>>()
    val favouriteBooks: LiveData<List<BooksModel>>
        get() = _favouriteBooks



    fun loadFavouriteBooks(userEmail: String) {
        viewModelScope.launch {
            try {
                val temp = BookClient().getFavouriteByEmailWithCoroutine(userEmail)
                _favouriteBooks.value = temp
            } catch (e: Exception) {
            }
        }
    }
}