package com.zacker.bookmaster.ui.cartBooks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookmaster.databinding.ItemLoveBinding
import com.zacker.bookmaster.model.BooksModel

class BookCartAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener,
    private val callBackCancel: DeleteItemFavourite
) : RecyclerView.Adapter<BookCartAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemLoveBinding):
        RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(book: BooksModel, deleteCancel: DeleteItemFavourite) {
                binding.tvNameBook.text = book.bookTitle
                binding.tvNameWriter.text = book.authorName
                binding.tvPrice.text = "$" +  book.price
                Glide.with(binding.imgBook.context)
                    .load(book.imageURL)
                    .into(binding.imgBook)
                binding.ivCancel.setOnClickListener {
                    deleteCancel.onClickCancel(book)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLoveBinding.inflate(
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