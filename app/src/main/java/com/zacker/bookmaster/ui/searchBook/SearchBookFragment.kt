package com.zacker.bookmaster.ui.searchBook

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.FragmentSearchBookBinding
import com.zacker.bookmaster.model.BooksModel

class SearchBookFragment : Fragment(), BookSearchAdapter.OnBookItemClickListener {

    private lateinit var binding: FragmentSearchBookBinding
    private val listSearchBook: ArrayList<BooksModel> = arrayListOf()
    private lateinit var bookSearchAdapter: BookSearchAdapter
    private val viewModel: SearchBookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookSearchAdapter = BookSearchAdapter(listSearchBook, this)
        binding.rvSearch.adapter = bookSearchAdapter
        setListener()
        setupObservers()
        setupSearchBook()
    }

    private fun setupObservers() {
        viewModel.books.observe(viewLifecycleOwner, Observer { books ->
            listSearchBook.clear()
            listSearchBook.addAll(books)
            bookSearchAdapter.notifyDataSetChanged()
        })
    }

    private fun setupSearchBook() {
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listSearchBook.clear()
                bookSearchAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString()
                if (searchQuery.isNotEmpty()) {
                    viewModel.searchBooks(searchQuery)
                }
            }
        })
    }

    private fun setListener() {
        binding.ibBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onClickBook(position: Int) {
        val selectedBook = listSearchBook[position]
        val bundle = Bundle().apply {
            putSerializable("selectedBook", selectedBook)
        }
        NavHostFragment.findNavController(this).navigate(R.id.action_searchBookFragment_to_bookIntroductionFragment, bundle)
        view?.hideKeyboard()
    }
}
