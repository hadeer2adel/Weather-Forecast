package com.example.weatherforecast.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecast.Helpers.TabLayoutAdapter
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.log

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var tabLayoutAdapter: TabLayoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabNumber = requireArguments().getInt("tabNumber", 0)

        var bundle = Bundle()
        if(requireArguments().containsKey("latitude")) {
            val latitude = requireArguments().getString("latitude", "0")
            val longitude = requireArguments().getString("longitude", "0")

            bundle.putString("latitude", latitude)
            bundle.putString("longitude", longitude)
        }

        tabLayoutAdapter = TabLayoutAdapter(requireActivity(), bundle)

        initViewPager()
        initTabLayout()

        binding.viewPager.doOnPreDraw {
            binding.viewPager.currentItem = tabNumber
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