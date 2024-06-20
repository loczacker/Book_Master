package com.zacker.bookmaster.ui.historyPayment

import androidx.recyclerview.widget.DiffUtil
import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.model.PaymentsModel

class HistoryDiffCallback(
    private val oldList: List<PaymentsModel>,
    private val newList: List<PaymentsModel>
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
