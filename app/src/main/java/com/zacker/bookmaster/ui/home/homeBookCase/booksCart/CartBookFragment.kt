package com.zacker.bookmaster.ui.home.homeBookCase.booksCart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zacker.bookmaster.databinding.FragmentCartBookBinding
import com.zacker.bookmaster.model.CartsModel

class CartBookFragment : Fragment(), BookCartAdapter.OnBookItemClickListener {

    private lateinit var binding: FragmentCartBookBinding
    private val listCartBook: ArrayList<CartsModel> = arrayListOf()
    private lateinit var booksCartAdapter: BookCartAdapter
    private val viewModel: BookCartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

        // Fetch cart data for a specific user (example email)
        viewModel.fetchCartByEmail("user@example.com")
    }



    private fun observeViewModel() {

    }

    override fun onClickBook(position: Int) {
        // Xử lý khi người dùng chọn một cuốn sách từ giỏ hàng
    }
}
