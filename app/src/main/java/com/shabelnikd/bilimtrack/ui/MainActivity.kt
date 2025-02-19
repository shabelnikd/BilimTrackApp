package com.shabelnikd.bilimtrack.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.databinding.ActivityMainBinding
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val preferenceHelper: PreferenceHelper by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.main)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as? NavHostFragment
                ?: return

        val inflater = navHostFragment.navController.navInflater
        val navGraph = inflater.inflate(R.navigation.nav_graph)

        if (!preferenceHelper.isFirstLaunch && !preferenceHelper.isLoggedIn) {
            navGraph.setStartDestination(R.id.authFragment)
        } else if (!preferenceHelper.isFirstLaunch && preferenceHelper.isLoggedIn) {
            navGraph.setStartDestination(R.id.profileFragment)
        }

        val bottomNavigationView = binding.navView
        bottomNavigationView.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.onBoardFragment, R.id.authFragment -> {
                    binding.navView.isVisible = false
                    binding.topBar.isVisible = false
                }

                else -> {
                    binding.navView.isVisible = true
                    binding.topBar.isVisible = true
                }
            }
        }

        navHostFragment.navController.graph = navGraph

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val systemIme = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom + systemIme.bottom
            )
            insets
        }
    }
}