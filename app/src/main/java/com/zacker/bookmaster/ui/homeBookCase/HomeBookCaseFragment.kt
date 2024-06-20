package com.zacker.bookmaster.ui.homeBookCase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.zacker.bookmaster.databinding.FragmentHomeBookCaseBinding

class HomeBookCaseFragment : Fragment() {

    private lateinit var binding: FragmentHomeBookCaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBookCaseBinding.inflate(inflater, container, false)
        setupViewPager()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.vpShare.adapter = DataPagerBookCaseAdapter(requireActivity() as AppCompatActivity)
        val tabTitles = arrayOf("Favourite", "Cart")
        TabLayoutMediator(binding.tabBookCase, binding.vpShare) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}