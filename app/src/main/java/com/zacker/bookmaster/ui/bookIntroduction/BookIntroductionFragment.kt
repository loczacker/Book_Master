package com.zacker.bookmaster.ui.bookIntroduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.zacker.bookmaster.databinding.FragmentBookIntroductionBinding
import com.zacker.bookmaster.model.BooksModel

class BookIntroductionFragment : Fragment() {
    private lateinit var binding: FragmentBookIntroductionBinding
    private lateinit var viewModel: BookIntroductionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookIntroductionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BookIntroductionViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        val args = arguments
        if (args != null) {
            val selectedBook = args.getSerializable("selectedBook") as? BooksModel
            if (selectedBook != null) {
                viewModel.setNameBook("${selectedBook.bookTitle}")
                binding.tvTitle.text = selectedBook.bookTitle
                binding.tvAuthor.text = selectedBook.authorName
                binding.tvIntroduction.text = selectedBook.bookDescription
                binding.tvPrice.text = "$" + selectedBook.price
                Glide.with(binding.imgBook.context)
                    .load(selectedBook.imageURL)
                    .into(binding.imgBook)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameBook = viewModel.nameBook.value
        setUpObserver()
        setListener(nameBook)
        nameBook?.let { checkFavourite(it) }
    }

    private fun setListener(nameBook: String?) {
        binding.ibBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
//        binding.btnRead.setOnClickListener {
//            val bundleBook = Bundle()
//            bundleBook.putString("nameBook", nameBook)
//            NavHostFragment.findNavController(this)
//                .navigate(R.id.action_bookIntroductionFragment_to_chapFragment, bundleBook)
//        }
        binding.cbHeart.setOnClickListener {
            val nameBook = viewModel.nameBook.value.toString()
        }
    }

    private fun checkFavourite(nameBook: String) {
    }

    private fun setUpObserver() {
        viewModel.nameBook.observe(viewLifecycleOwner) {
        }
    }
}