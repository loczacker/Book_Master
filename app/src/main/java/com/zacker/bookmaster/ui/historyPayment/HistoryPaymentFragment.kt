package com.zacker.bookmaster.ui.historyPayment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import com.zacker.bookmaster.databinding.FragmentHistoryPaymentBinding
import com.zacker.bookmaster.model.PaymentsModel
import com.zacker.bookmaster.util.Const

class HistoryPaymentFragment : Fragment(), HistoryPaymentAdapter.OnBookItemClickListener {

    private lateinit var binding: FragmentHistoryPaymentBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: HistoryPaymentViewModel
    private lateinit var adapter: HistoryPaymentAdapter
    private val listHistoryPayment = arrayListOf<PaymentsModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryPaymentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HistoryPaymentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
        setupRecyclerView()
        setListener()
        viewModel.payments.observe(viewLifecycleOwner, Observer { paymentHistory ->
            updateHistoryList(paymentHistory)
        })
        viewModel.loadHistoryPayment(userEmail)
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString(Const.KEY_EMAIL_USER, "") ?: ""
        viewModel.loadHistoryPayment(userEmail)
    }

    private fun setListener() {
        binding.ibBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryPaymentAdapter(listHistoryPayment, this)
        binding.recyclerViewTabFavourite.adapter = adapter
    }

    private fun updateHistoryList(newList: List<PaymentsModel>) {
        val diffCallback = HistoryDiffCallback(listHistoryPayment, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listHistoryPayment.clear()
        listHistoryPayment.addAll(newList)
        diffResult.dispatchUpdatesTo(adapter)
    }

    override fun onClickBook(position: Int) {

    }
}