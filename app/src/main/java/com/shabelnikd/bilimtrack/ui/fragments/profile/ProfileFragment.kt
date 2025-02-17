package com.shabelnikd.bilimtrack.ui.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.adapters.AchieveAdapter
import com.shabelnikd.bilimtrack.adapters.CoursesAdapter
import com.shabelnikd.bilimtrack.databinding.FragmentProfileBinding
import com.shabelnikd.bilimtrack.ui.MainActivity
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferences = PreferenceHelper()

    private val coursesAdapter = CoursesAdapter()
    private val achieveAdapter = AchieveAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sharedPreferences.initialize(requireContext())
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserMeData(sharedPreferences)
        viewModel.getSubjectsMeData()

        binding.rvCoursers.adapter = coursesAdapter
        binding.rvCoursers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAchieve.adapter = achieveAdapter
        binding.rvAchieve.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileResult.collect { result ->
                    when (result) {
                        is ProfileViewModel.ProfileResult.Success -> {
                            binding.tvUsername.text = result.me.username
                            if (result.me.firstName != null && result.me.lastName != null) {
                                binding.tvFullName.text = result.me.firstName + " " + result.me.lastName
                            }

                            binding.tvGroupName.text = result.me.group
                            binding.tvRatingValue.text = result.me.rating.toString()
                            binding.tvAchieveValue.text = result.me.achievementsCount.toString()
                            binding.tvScoreValue.text = result.me.points.toString()

                            achieveAdapter.submitList(result.me.achievements)

                        }
                        is ProfileViewModel.ProfileResult.Error -> {
                            Snackbar.make(binding.root, result.errorMessage, 2000).show()
                            if (result.errorMessage == "401") {
                                activity?.finish()
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }

                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subjectsResult.collect { result ->
                    when(result) {
                        is ProfileViewModel.SubjectsResult.Success -> {
                            coursesAdapter.submitList(result.me)
                        }
                        is ProfileViewModel.SubjectsResult.Error -> {
                            Snackbar.make(binding.root, result.errorMessage, 2000).show()
                        }
                    }
                }
            }
        }

    }

}