package com.zacker.bookmaster.ui.home.homeBookCase.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookmaster.databinding.ItemFavouriteBinding
import com.zacker.bookmaster.model.BooksModel

class FavouriteAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener,
    private val callBackCancel: DeleteItemFavourite
) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemFavouriteBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel, deleteCancel: DeleteItemFavourite) {
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
            ItemFavouriteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        books[position].let {
            holder.bind(it, callBackCancel)
        }
        holder.itemView.setOnClickListener {
            callback.onClickBook(position, book)
        }
    }


    interface OnBookItemClickListener{
        fun onClickBook(position: Int, book: BooksModel)
    }

    interface DeleteItemFavourite{
        fun onClickCancel(book: BooksModel)
    }
}