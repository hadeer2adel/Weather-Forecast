package com.example.weatherforecast.RecycleView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecast.View.HomeFragment

class TabLayoutAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when{
            position == 0 -> return HomeFragment()
            position == 1 -> return HomeFragment()
            position == 2 -> return HomeFragment()
            else -> return HomeFragment()
        }
    }
}