package com.zacker.bookmaster.ui.home


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zacker.bookmaster.ui.home.homeBookCase.HomeBookCaseFragment
import com.zacker.bookmaster.ui.home.homeDiscover.HomeDiscoverFragment
import com.zacker.bookmaster.ui.home.homeProfile.HomeProfileFragment


class ViewPagerHomeAdapter(fragment: HomeFragment):  FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            1 -> HomeBookCaseFragment()
            0 -> HomeDiscoverFragment()
            2 -> HomeProfileFragment()
            else -> throw  IllegalArgumentException("Unknown Fragment")
        }
    }
}