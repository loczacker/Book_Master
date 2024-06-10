package com.zacker.bookmaster.ui.home.homeBookCase.booksCart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookmaster.databinding.ItemViewedBinding
import com.zacker.bookmaster.model.BooksModel

class BookCartAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<BookCartAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemViewedBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel) {
                binding.tvNameBook.text = book.bookTitle
                binding.tvNameWriter.text = book.authorName
                Glide.with(binding.imgBook.context)
                    .load(book.imageURL)
                    .into(binding.imgBook)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewedBinding.inflate(
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