package com.example.weatherforecast.View

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.ViewModel.SharedFlowViewModel
import com.example.weatherforecast.base.BaseActivity
import com.example.weatherforecast.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : BaseActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedFlowViewModel: SharedFlowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topBar)
        navController = findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(this, navController)

        sharedFlowViewModel = ViewModelProvider(this).get(SharedFlowViewModel::class.java)

        lifecycleScope.launch {
            sharedFlowViewModel.languageChangeFlow.collect { language ->
                recreate()
            }
        }
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