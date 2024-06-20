package com.zacker.bookmaster.ui.bookIntroduction

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.zacker.bookmaster.databinding.FragmentBookIntroductionBinding
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.util.Const

class BookIntroductionFragment : Fragment() {
    private lateinit var binding: FragmentBookIntroductionBinding
    private lateinit var viewModel: BookIntroductionViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookIntroductionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BookIntroductionViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
        val args = arguments
        if (args != null) {
            val selectedBook = args.getSerializable("selectedBook") as? BooksModel
            if (selectedBook != null) {
                viewModel.setIdBook(("${selectedBook._id}"))
                viewModel.setNameBook("${selectedBook.bookTitle}")
                binding.tvTitle.text = selectedBook.bookTitle
                binding.tvAuthor.text = selectedBook.authorName
                binding.tvIntroduction.text = selectedBook.bookDescription
                binding.tvPrice.text = "$" + selectedBook.price
                Glide.with(binding.imgBook.context)
                    .load(selectedBook.imageURL)
                    .into(binding.imgBook)
                viewModel.checkIfFavourite(userEmail)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
        setListener()
        setUpToastObserver()
    }

    private fun setListener() {
        sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
        binding.ibBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
        binding.cbHeart.setOnClickListener {
            val isChecked = binding.cbHeart.isChecked
            viewModel.updateFavouriteStatus(userEmail, isChecked)
        }
        binding.btnCart.setOnClickListener {
            viewModel.checkAndAddToCart(userEmail)
        }
        binding.tvPrice
    }

    private fun setUpObserver() {
        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            binding.cbHeart.isChecked = isFavourite
        }
    }

    private fun setUpToastObserver() {
        viewModel.toastMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
