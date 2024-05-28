package com.zacker.bookmaster.ui.home.homeDiscover

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zacker.bookmaster.adapter.BookAdapter
import com.zacker.bookmaster.databinding.FragmentHomeDiscoverBinding
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.network.BookClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeDiscoverFragment : Fragment() {
    private lateinit var binding: FragmentHomeDiscoverBinding
    private lateinit var viewModel: HomeDiscoverViewModel
    private val books = arrayListOf<BooksModel>()
    private val randomBooks = arrayListOf<BooksModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDiscoverBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeDiscoverViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateBook()
        randomBook()
    }

    private fun randomBook() {
        val adapter = RandomBookAdapter(randomBooks)
        binding.rvRandomBook.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            val temp = BookClient().getAllBookWithCoroutine().shuffled()
            withContext(Dispatchers.Main) {
                randomBooks.clear()
                randomBooks.addAll(temp)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun updateBook() {
        val adapter = BookAdapter(books)
        binding.rvNewBook.adapter = adapter

        val bookRequest = BookClient.invoke().getAllBook()
        bookRequest.enqueue(object : Callback<List<BooksModel>> {
            override fun onResponse(
                call: Call<List<BooksModel>>,
                response: Response<List<BooksModel>>
            ) {
                if (response.isSuccessful) {
                    val temp = response.body()
                    temp?.let {
                        books.clear()
                        books.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireActivity(), "Does not have data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<BooksModel>>, t: Throwable) {
                Toast.makeText(requireActivity(), "${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }
}