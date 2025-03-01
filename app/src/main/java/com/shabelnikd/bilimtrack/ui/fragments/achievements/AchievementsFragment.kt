package com.shabelnikd.bilimtrack.ui.fragments.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shabelnikd.bilimtrack.adapters.AchieveAdapter
import com.shabelnikd.bilimtrack.databinding.FragmentAchievementsBinding
import kotlinx.coroutines.launch

class AchievementsFragment : Fragment() {

    private val viewModel: AchievementsViewModel by viewModels()

    private var _binding: FragmentAchievementsBinding? = null
    private val binding get() = _binding!!

    private val achieveAdapter = AchieveAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        updateUI()
        setupListeners()
    }

    private fun loadData() {
        viewModel.getUserMeAchievementsData()
    }

    private fun initUI() {
        binding.rvAchieve.apply {
            adapter = achieveAdapter
            layoutManager =
//                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                GridLayoutManager(requireContext(), 3)
        }
    }

    private fun updateUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectAchievements()
            }
        }
    }

    private suspend fun collectAchievements() {
        viewModel.achieveResult.collect { result ->
            with(binding) {
                when (result) {
                    is AchievementsViewModel.AchieveResult.Success -> {
                        achieveAdapter.submitList(
                            result.achievements.sortedBy { it.rarity?.id }.reversed()
                        )
                    }

                    is AchievementsViewModel.AchieveResult.Error -> {
                        Snackbar.make(root, result.errorMessage, 2000).show()
                    }
                }
            }
        }
    }

    fun setupListeners() {
        achieveAdapter.setOnClickListener { achieveSubject ->
            findNavController().navigate(
                AchievementsFragmentDirections.actionAchievementsFragmentToAchieveDetailsFragment(
                    achieveSubject
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}