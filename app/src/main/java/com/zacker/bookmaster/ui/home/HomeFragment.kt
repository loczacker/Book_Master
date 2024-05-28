package com.zacker.bookmaster.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var isViewPagerScrollEnabled = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        setUpBottom()
    }

    private fun setUpBottom() {
        val viewPagerHomeAdapter = ViewPagerHomeAdapter(this)
        binding.vpShare.adapter = viewPagerHomeAdapter
        binding.vpShare.isUserInputEnabled = isViewPagerScrollEnabled  // Bật hoặc tắt sự kiện vuốt
        binding.vpShare.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    1 -> binding.bottomNavigationView.menu.findItem(R.id.nav_book_case).isChecked = true
                    0 -> binding.bottomNavigationView.menu.findItem(R.id.nav_discover).isChecked = true
                    2 -> binding.bottomNavigationView.menu.findItem(R.id.nav_profile).isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                isViewPagerScrollEnabled = state != ViewPager2.SCROLL_STATE_DRAGGING
                binding.vpShare.isUserInputEnabled = isViewPagerScrollEnabled
            }
        })
    }

    private fun setUpViewPager() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_book_case -> binding.vpShare.currentItem = 1
                R.id.nav_discover -> binding.vpShare.currentItem = 0
                R.id.nav_profile -> binding.vpShare.currentItem = 2
            }
            true
        })
    }
}