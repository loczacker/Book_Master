import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.FragmentCartBookBinding
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.network.BookClient
import com.zacker.bookmaster.ui.home.homeBookCase.booksCart.BookCartAdapter
import com.zacker.bookmaster.util.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartBookFragment : Fragment(), BookCartAdapter.OnBookItemClickListener, BookCartAdapter.DeleteItemFavourite {

    private lateinit var binding: FragmentCartBookBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: CartBookViewModel by viewModels()
    private lateinit var adapter: BookCartAdapter
    private val listCartBook =  arrayListOf<BooksModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
        setupRecyclerView()
        viewModel.cartBooks.observe(viewLifecycleOwner, Observer { cartBooks ->
            listCartBook.clear()
            listCartBook.addAll(cartBooks)
            adapter.notifyDataSetChanged()
        })

        viewModel.loadCartBooks(userEmail)
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
        loadCartBooks(userEmail)
    }

    private fun loadCartBooks(userEmail: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val temp = BookClient().getCartByEmailWithCoroutine(userEmail)
                withContext(Dispatchers.Main) {
                    listCartBook.clear()
                    listCartBook.addAll(temp)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to load cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = BookCartAdapter(listCartBook, this, this)
        binding.recyclerViewTabViewed.adapter = adapter
    }

    override fun onClickBook(position: Int, book: BooksModel) {
        val selectedBook = listCartBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_homeFragment_to_bookIntroductionFragment, bundle)
    }

    override fun onClickCancel(book: BooksModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                BookClient().deleteCartItem(book._id.toString())
                withContext(Dispatchers.Main) {
                    val position = listCartBook.indexOf(book)
                    if (position != -1) {
                        listCartBook.removeAt(position)
                        adapter.notifyItemRemoved(position)
                    }
                    Toast.makeText(requireContext(), "Book removed from favourites", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to remove book", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
