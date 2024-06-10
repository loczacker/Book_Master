package com.zacker.bookmaster.ui.searchBook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.network.BookClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SearchBookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<BooksModel>>()
    val books: LiveData<List<BooksModel>> get() = _books

    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                val allBooks = withContext(Dispatchers.IO) {
                    BookClient().getAllBookWithCoroutine()
                }
                val filteredBooks = allBooks.filter {
                    it.bookTitle?.lowercase(Locale.getDefault())?.contains(query.lowercase(Locale.getDefault())) == true
                }
                _books.postValue(filteredBooks)
            } catch (e: Exception) {
                // Handle the exception
            }
        }
    }
}
