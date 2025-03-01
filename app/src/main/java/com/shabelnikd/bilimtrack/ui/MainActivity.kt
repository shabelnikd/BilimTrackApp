package com.shabelnikd.bilimtrack.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
        setTheme(R.style.Base_Theme_BilimTrack)
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
                    setNavigationVisibility(false)
                }

                R.id.profileFragment -> {
                    setTopBarText(" Ваш", " аккаунт")
                    setNavigationVisibility(true)
                }

                R.id.ratingTabFragment -> {
                    setTopBarText(" Рейтинг и ", "статистика")
                    setNavigationVisibility(true)
                }

                R.id.achievementsFragment -> {
                    setTopBarText(" Все", " достижения")
                    setNavigationVisibility(true)
                }

                else -> {
                    setNavigationVisibility(false)
                }
            }
        }

        navHostFragment.navController.graph = navGraph

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val systemIme = insets.getInsets(WindowInsetsCompat.Type.ime())

            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = true

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    fun setNavigationVisibility(isVisible: Boolean) {
        binding.navView.isVisible = isVisible
        binding.topBar.isVisible = isVisible
    }

    fun setTopBarText(text: String, textAccentColor: String) {
        binding.topBarGrayText.text = text
        binding.topBarAccentText.text = textAccentColor

    }

    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            binding.root
        ).show(WindowInsetsCompat.Type.systemBars())


    }
}