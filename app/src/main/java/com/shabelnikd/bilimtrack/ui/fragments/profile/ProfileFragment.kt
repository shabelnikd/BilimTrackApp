package com.shabelnikd.bilimtrack.ui.fragments.profile

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.shabelnikd.bilimtrack.adapters.CoursesAdapter
import com.shabelnikd.bilimtrack.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val args: ProfileFragmentArgs by navArgs()


    private val coursesAdapter = CoursesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData(args.username)
        initUI()
        updateUI()
    }

    private fun loadData(username: String) {
        when {
            username == "me" -> {
                viewModel.getUserMeData(username)
                viewModel.getSubjectsMeData()
            }

            else -> viewModel.getUserMeData(username)
        }
    }

    private fun initUI() {
        binding.rvCoursers.apply {
            adapter = coursesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        coursesAdapter.setOnClickListener { url ->
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToWebViewFragment(
                    url
                )
            )
        }

        when {
            args.username == "me" -> {
                binding.tvCoursesText.visibility = View.VISIBLE
                binding.rvCoursers.visibility = View.VISIBLE
                binding.rvCoursers.adapter = coursesAdapter
            }

            else -> {
                binding.tvCoursesText.visibility = View.GONE
                binding.rvCoursers.visibility = View.GONE
                binding.rvCoursers.adapter = null
            }
        }
    }

    private fun updateUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectProfile()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectSubjects()
            }
        }
    }

    private suspend fun collectProfile() {
        viewModel.profileResult.collect { result ->
            with(binding) {
                when (result) {
                    is ProfileViewModel.ProfileResult.Success -> {
                        result.me.photo?.let {
                            Glide.with(binding.root).load(it)
                                .into(binding.profileImage)
                        }

                        tvUsername.text = result.me.username

                        if (result.me.firstName != null && result.me.lastName != null) {
                            tvFullName.text =
                                String.format("%s %s", result.me.firstName, result.me.lastName)
                        }

                        tvGroupName.text = result.me.group
                        tvRatingValue.text = result.me.rating.toString()
                        tvAchieveValue.text = result.me.achievementsCount.toString()
                        tvScoreValue.text = result.me.points.toString()

                    }

                    is ProfileViewModel.ProfileResult.Error -> {
                        Snackbar.make(root, result.errorMessage, 2000).show()
                    }
                }
            }
        }
    }

    private suspend fun collectSubjects() {
        viewModel.subjectsResult.collect { result ->
            when (result) {
                is ProfileViewModel.SubjectsResult.Success -> {
                    coursesAdapter.submitList(result.me)
                }

                is ProfileViewModel.SubjectsResult.Error -> {
                    Snackbar.make(binding.root, result.errorMessage, 2000).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


