package com.zacker.bookmaster.ui.bookIntroduction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zacker.bookmaster.model.CartsModel
import com.zacker.bookmaster.model.FavouriteModel
import com.zacker.bookmaster.network.BookClient
import com.zacker.bookmaster.util.Event
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookIntroductionViewModel : ViewModel() {

    private var _nameBook = MutableLiveData<String>("")
    val nameBook: LiveData<String>
        get() = _nameBook

    private var _idBook = MutableLiveData<String>("")
    val id: LiveData<String>
        get() = _idBook

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage


    private val _isFavourite = MutableLiveData<Boolean>(false)
    val isFavourite: LiveData<Boolean>
        get() = _isFavourite

    fun setNameBook(newNameBook: String) {
        _nameBook.value = newNameBook
    }

    fun setIdBook(newIdBook: String) {
        _idBook.value = newIdBook
    }

    fun checkIfFavourite(email: String) {
        viewModelScope.launch {
            try {
                val favouriteBooks = BookClient().getFavouriteByEmailWithCoroutine(email)
                _isFavourite.value = favouriteBooks.any { it._id == _idBook.value }
            } catch (e: Exception) {
                _isFavourite.value = false
            }
        }
    }

    fun updateFavouriteStatus(email: String, isFavourite: Boolean) {
        viewModelScope.launch {
            try {
                if (isFavourite) {
                    val currentDateTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val formattedDateTime = currentDateTime.format(formatter)

                    val favouriteModel = FavouriteModel(
                        bookId = _idBook.value,
                        bookTitle = _nameBook.value,
                        userMail = email,
                        data = formattedDateTime
                    )
                    val addedFavourite = BookClient().postNewFavourite(favouriteModel)
                    if (addedFavourite != null) {
                        _isFavourite.value = true
                        _toastMessage.value = Event("Added to favourites successfully")
                    }
                } else {
                    val deletedFavourite = _idBook.value?.let { BookClient().deleteFavouriteItem(it) }
                    if (deletedFavourite != null) {
                        _isFavourite.value = false
                        _toastMessage.value = Event("Removed from favourites successfully")
                    }
                }
            } catch (e: Exception) {
                _toastMessage.value = Event("Error occurred: ${e.message}")
            }
        }
    }

    fun checkAndAddToCart(email: String) {
        viewModelScope.launch {
            try {
                val cartBooks = BookClient().getCartByEmailWithCoroutine(email)
                val isBookInCart = cartBooks.any { it._id == _idBook.value }
                if (isBookInCart) {
                    _toastMessage.value = Event("This book is already in your cart")
                } else {
                    val currentDateTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val formattedDateTime = currentDateTime.format(formatter)

                    val cartModel = CartsModel(
                        bookId = _idBook.value,
                        bookTitle = _nameBook.value,
                        userMail = email,
                        data = formattedDateTime
                    )
                    BookClient().postNewCart(cartModel)
                    _toastMessage.value = Event("Added to cart successfully")
                }
            } catch (e: Exception) {
                _toastMessage.value = Event("Error occurred: ${e.message}")
            }
        }
    }

}
