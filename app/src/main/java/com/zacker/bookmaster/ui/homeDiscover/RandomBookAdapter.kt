package com.zacker.bookmaster.ui.homeDiscover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.ItemBookDiscoverRandomBinding
import com.zacker.bookmaster.model.BooksModel

class RandomBookAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<RandomBookAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemBookDiscoverRandomBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel) {
                binding.tvNameBook.text = book.bookTitle
                binding.tvIntroduction.text = book.bookDescription
                Glide.with(binding.imgBook.context)
                    .load(book.imageURL)
                    .placeholder(R.drawable.profile)
                    .into(binding.imgBook)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookDiscoverRandomBinding.inflate(
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