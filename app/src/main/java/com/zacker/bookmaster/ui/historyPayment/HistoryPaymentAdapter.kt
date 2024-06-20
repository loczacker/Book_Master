package com.zacker.bookmaster.ui.historyPayment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zacker.bookmaster.databinding.ItemHistoryBinding
import com.zacker.bookmaster.model.PaymentsModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryPaymentAdapter(
    private val payment: List<PaymentsModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<HistoryPaymentAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemHistoryBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(payment: PaymentsModel) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val parsedDate: Date = dateFormat.parse(payment.date) ?: Date()
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = outputFormat.format(parsedDate)
                binding.tvTime.text = formattedDate
                binding.tvTotalItem.text = payment.bookId.size.toString()
                binding.tvAmount.text = "$" +   payment.amount.toString()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = payment.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        payment[position].let {
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