package com.zacker.bookmaster.ui.homeDiscover

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zacker.bookmaster.model.BooksModel

class HomeDiscoverViewModel: ViewModel() {
    val resultBookSort = MutableLiveData<List<BooksModel>>()

}