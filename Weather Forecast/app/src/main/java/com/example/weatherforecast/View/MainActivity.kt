package com.example.weatherforecast.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecast.R
import com.example.weatherforecast.RecycleView.TabLayoutAdapter
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var tabLayoutAdapter: TabLayoutAdapter
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayoutAdapter = TabLayoutAdapter(this)

        initTopBar()
        initTabLayout()
        initViewPager()
    }

    private fun initTopBar(){
        binding.topBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> true
                R.id.settings -> true
                else -> false
            }
        }
    }
    private fun initTabLayout(){
        binding.apply {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.home))
            tabLayout.addTab(tabLayout.newTab().setText(R.string.alarm))
            tabLayout.addTab(tabLayout.newTab().setText(R.string.favourites))

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewPager.currentItem = tab.position
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
            })
        }
    }
    private fun initViewPager(){
        binding.apply {
            viewPager.adapter = tabLayoutAdapter
            viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.getTabAt(position)?.select()
                }
            })
        }
    }
}