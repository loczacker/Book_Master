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

    fun loadCartBooks(userEmail: String) {
        viewModelScope.launch {
            try {
                val temp = BookClient().getCartByEmailWithCoroutine(userEmail)
                _cartBooks.value = temp
            } catch (e: Exception) {
            }
        }
    }
}
