import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.FragmentFavouriteBinding
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.network.BookClient
import com.zacker.bookmaster.ui.favouriteBooks.BooksDiffCallback
import com.zacker.bookmaster.ui.favouriteBooks.FavouriteAdapter
import com.zacker.bookmaster.ui.favouriteBooks.FavouriteViewModel
import com.zacker.bookmaster.util.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteFragment : Fragment(), FavouriteAdapter.OnBookItemClickListener,
    FavouriteAdapter.DeleteItemFavourite {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var adapter: FavouriteAdapter
    private val listFavouriteBook = arrayListOf<BooksModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
        setupRecyclerView()
        viewModel.favouriteBooks.observe(viewLifecycleOwner, Observer { favouriteBooks ->
            updateFavouriteList(favouriteBooks)
        })
        viewModel.loadFavouriteBooks(userEmail)
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
        viewModel.loadFavouriteBooks(userEmail)
    }

    private fun loadFavouriteBooks(userEmail: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val temp = BookClient().getFavouriteByEmailWithCoroutine(userEmail)
                withContext(Dispatchers.Main) {
                    updateFavouriteList(temp)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to load cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = FavouriteAdapter(listFavouriteBook, this, this)
        binding.recyclerViewTabFavourite.adapter = adapter
    }

    private fun updateFavouriteList(newList: List<BooksModel>) {
        val diffCallback = BooksDiffCallback(listFavouriteBook, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listFavouriteBook.clear()
        listFavouriteBook.addAll(newList)
        diffResult.dispatchUpdatesTo(adapter)
    }

    override fun onClickBook(position: Int, book: BooksModel) {
        val selectedBook = listFavouriteBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_homeFragment_to_bookIntroductionFragment, bundle)
    }

    override fun onClickCancel(book: BooksModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                BookClient().deleteFavouriteItem(book._id.toString())
                withContext(Dispatchers.Main) {
                    val position = listFavouriteBook.indexOf(book)
                    if (position != -1) {
                        listFavouriteBook.removeAt(position)
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
