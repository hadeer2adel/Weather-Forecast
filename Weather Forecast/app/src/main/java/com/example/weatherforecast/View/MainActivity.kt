package com.example.weatherforecast.View

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import java.util.Locale

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocale(this)
        setLocale(this)
        setLocale(this)

        setSupportActionBar(binding.topBar)

        navController = findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(this, navController)
    }

    private fun setLocale(context: Context) {
        val languageCode = AppSettings.getInstance(context).language
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                navController.navigate(R.id.settingFragment)
                true
            }
            else -> {
                navController.navigate(R.id.mainFragment)
                true
            }
        }
    }

}