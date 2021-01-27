package com.github.qxtno.coronastats.activities

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.github.qxtno.coronastats.R
import com.github.qxtno.coronastats.databinding.ActivityMainBinding
import com.github.qxtno.coronastats.helpers.PrefsManager

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val prefsManager = PrefsManager()
    lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstLaunch = prefsManager.getFirstLaunch(this, "firstLaunch", false)
        if (firstLaunch) {
            prefsManager.setCountryString(this, "country", "PL")
            prefsManager.setFirstLaunch(this, "firstLaunch", true)
        }

        actionBar = supportActionBar!!

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.findNavController()
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        val appBarConfiguration = AppBarConfiguration(binding.bottomNavigationView.menu)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun changeBottomNavVisibility(status: Boolean) {
        if (status) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationView.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}