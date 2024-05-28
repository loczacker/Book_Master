package com.zacker.bookmaster.ui.home.homeBookCase

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zacker.bookmaster.ui.home.homeBookCase.booksCart.CartBookFragment
import com.zacker.bookmaster.ui.home.homeBookCase.favourite.FavouriteFragment


class DataPagerBookCaseAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CartBookFragment()
            1 -> FavouriteFragment()
            else -> throw  IllegalArgumentException("Unknown Fragment")
        }
    }
}