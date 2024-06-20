package com.zacker.bookmaster.ui.historyPayment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zacker.bookmaster.model.PaymentsModel
import com.zacker.bookmaster.network.BookClient
import kotlinx.coroutines.launch

class HistoryPaymentViewModel: ViewModel() {


    private val _payments = MutableLiveData<List<PaymentsModel>>()
    val payments: LiveData<List<PaymentsModel>>
        get() = _payments

    fun loadHistoryPayment(userEmail: String) {
        viewModelScope.launch {
            try {
                val temp = BookClient().getPaymentHistoryEmailWithCoroutine(userEmail)
                _payments.value = temp
            } catch (e: Exception) {
            }
        }
    }
}