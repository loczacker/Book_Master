package com.zacker.bookmaster.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.ItemBookDiscoverBinding
import com.zacker.bookmaster.model.BooksModel

class BookAdapter(
    private val books: List<BooksModel>
): RecyclerView.Adapter<BookAdapter.ViewHolder>()  {
    class ViewHolder (
        private val binding: ItemBookDiscoverBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BooksModel) {
            binding.tvNameBook.text = book.bookTitle
            Glide.with(binding.imgBook.context)
                .load(book.imageURL)
                .placeholder(R.drawable.profile)
                .into(binding.imgBook)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }
}