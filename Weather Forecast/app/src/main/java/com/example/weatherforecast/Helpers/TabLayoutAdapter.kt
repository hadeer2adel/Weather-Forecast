package com.example.weatherforecast.Helpers

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecast.View.FavouriteFragment
import com.example.weatherforecast.View.HomeFragment

class TabLayoutAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when{
            position == 0 -> return HomeFragment()
            position == 1 -> return HomeFragment()
            position == 2 -> return FavouriteFragment()
            else -> return HomeFragment()
        }
    }
}