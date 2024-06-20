package com.zacker.bookmaster.ui.favouriteBooks

import androidx.recyclerview.widget.DiffUtil
import com.zacker.bookmaster.model.BooksModel

class BooksDiffCallback(
    private val oldList: List<BooksModel>,
    private val newList: List<BooksModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]._id == newList[newItemPosition]._id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
