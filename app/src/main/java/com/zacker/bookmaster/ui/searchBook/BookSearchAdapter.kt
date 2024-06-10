package com.zacker.bookmaster.ui.searchBook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookmaster.databinding.ItemBookSearchBinding
import com.zacker.bookmaster.model.BooksModel

class BookSearchAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<BookSearchAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemBookSearchBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel) {
                binding.tvNameBook.text = book.bookTitle
                binding.tvNameWriter.text = book.authorName
                binding.tvCategory.text = book.category
                Glide.with(binding.imgBook.context)
                    .load(book.imageURL)
                    .into(binding.imgBook)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookSearchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        books[position].let {
            holder.bind(it)
        }
        holder.itemView.setOnClickListener {
            callback.onClickBook(position)
        }
    }

    interface OnBookItemClickListener{
        fun onClickBook(position: Int)
    }
}