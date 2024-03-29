package com.example.weatherforecast.Helpers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecast.View.AlertListFragment
import com.example.weatherforecast.View.LocationListFragment
import com.example.weatherforecast.View.HomeFragment

class TabLayoutAdapter(fragmentActivity: FragmentActivity, val bundle: Bundle) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when{
            position == 0 -> return HomeFragment()
            position == 1 -> {
                val fragment = AlertListFragment()
                if(bundle.containsKey("latitude")){
                    fragment.arguments = bundle
                }
                return fragment
            }
            position == 2 -> {
                val fragment = LocationListFragment()
                if(bundle.containsKey("latitude")){
                    fragment.arguments = bundle
                }
                return fragment
            }
            else -> return HomeFragment()
        }
    }
}