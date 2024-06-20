package com.zacker.bookmaster.ui.homeBookCase

import com.zacker.bookmaster.ui.cartBooks.CartBookFragment
import FavouriteFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class DataPagerBookCaseAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavouriteFragment()
            1 -> CartBookFragment()
            else -> throw  IllegalArgumentException("Unknown Fragment")
        }
    }
}