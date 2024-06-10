package com.zacker.bookmaster.ui.allBook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.FragmentAllBookBinding
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.network.BookClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllBookFragment : Fragment(), AllBookAdapter.OnBookItemClickListener {

    private lateinit var binding: FragmentAllBookBinding
    private val listBook: ArrayList<BooksModel> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        getAllBook()
    }

    private fun setListeners() {
        binding.imgBackHomeProfile.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }
    private fun getAllBook() {
        val adapter = AllBookAdapter(listBook, this)
        binding.recyclerViewAllBook.adapter = adapter
        lifecycleScope.launch(Dispatchers.IO) {
            val temp = BookClient().getAllBookWithCoroutine()
            withContext(Dispatchers.Main) {
                listBook.clear()
                listBook.addAll(temp)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClickBook(position: Int) {
        val selectedBook = listBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this).navigate(R.id.action_allBookFragment_to_bookIntroductionFragment, bundle)
    }
}