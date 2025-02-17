package com.shabelnikd.bilimtrack.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.shabelnikd.bilimtrack.AppModule
import com.shabelnikd.bilimtrack.AppModule_ProvideBilimTrackRepositoryFactory
import com.shabelnikd.bilimtrack.BilimTrackApplication_GeneratedInjector
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.databinding.ActivityMainBinding
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository_Factory
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    val viewModel: MainActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.main)

        val sharedPreferences = PreferenceHelper()
        sharedPreferences.initialize(this)

        sharedPreferences.refreshToken?.let {
            viewModel.userRefresh(it)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? NavHostFragment?: return

        val inflater = navHostFragment.navController.navInflater
        val navGraph = inflater.inflate(R.navigation.nav_graph)

        if (!sharedPreferences.isFirstLaunch &&!sharedPreferences.isLoggedIn) {
            navGraph.setStartDestination(R.id.authFragment)
        } else if (!sharedPreferences.isFirstLaunch && sharedPreferences.isLoggedIn) {
            navGraph.setStartDestination(R.id.ratingTabFragment)
        }

        runBlocking {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.refreshResult.collect { result ->
                        when (result) {
                            is MainActivityViewModel.RefreshResult.Success -> {
                                sharedPreferences.accessToken = result.accessToken
                            }
                            is MainActivityViewModel.RefreshResult.Error -> {
                                sharedPreferences.accessToken = null
                                navGraph.setStartDestination(R.id.authFragment)
                            }
                        }
                    }
                }
            }
        }


        navHostFragment.navController.graph = navGraph

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val systemIme = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom + systemIme.bottom)
            insets
        }
    }
}