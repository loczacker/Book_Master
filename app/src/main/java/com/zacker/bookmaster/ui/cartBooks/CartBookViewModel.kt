import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.network.BookClient
import kotlinx.coroutines.launch

class CartBookViewModel : ViewModel() {

    private val _cartBooks = MutableLiveData<List<BooksModel>>()
    val cartBooks: LiveData<List<BooksModel>>
        get() = _cartBooks

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double>
        get() = _totalPrice

    private val bookClient = BookClient()

    fun loadCartBooks(userEmail: String) {
        viewModelScope.launch {
            try {
                val cartBooks = bookClient.getCartByEmailWithCoroutine(userEmail)
                _cartBooks.value = cartBooks
                calculateTotalPrice(cartBooks)
            } catch (e: Exception) {
            }
        }
    }

    private fun calculateTotalPrice(books: List<BooksModel>) {
        viewModelScope.launch {
            var total = 0.0
            books.forEach { book ->
                try {
                    val bookDetail = bookClient.getBookWithIdCoroutine(book._id ?: "")
                    val price = bookDetail.price?.toDoubleOrNull() ?: 0.0
                    total += price
                } catch (e: Exception) {
                }
            }
            _totalPrice.value = total
        }
    }

    fun updateTotalPrice(newTotalPrice: Double) {
        _totalPrice.value = newTotalPrice
    }
}
