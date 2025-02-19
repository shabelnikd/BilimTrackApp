package com.shabelnikd.bilimtrack.ui.fragments.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.shabelnikd.bilimtrack.adapters.RatingGroupsAdapter
import com.shabelnikd.bilimtrack.adapters.RatingUsersAdapter
import com.shabelnikd.bilimtrack.databinding.FragmentRatingTabPageBinding
import com.shabelnikd.bilimtrack.ui.fragments.rating.RatingTabFragment.Companion.ARG_ON_USERS_TOP_POSITION
import kotlinx.coroutines.launch

class RatingTabPageFragment : Fragment() {

    private val viewModel: RatingTabPageViewModel by viewModels()

    private var _binding: FragmentRatingTabPageBinding? = null
    private val binding get() = _binding!!

    private val userRatingAdapter = RatingUsersAdapter()
    private val groupsRatingAdapter = RatingGroupsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingTabPageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        observeData()

        when (arguments?.getInt(ARG_ON_USERS_TOP_POSITION)) {
            0 -> {
                binding.rvTop.adapter = userRatingAdapter
                binding.rvTop.layoutManager = LinearLayoutManager(requireContext())
            }

            1 -> {
                binding.rvTop.adapter = groupsRatingAdapter
                binding.rvTop.layoutManager = LinearLayoutManager(requireContext())
            }
        }

    }

    private fun observeData() {
        viewModel.usersRatingResponse.observe(viewLifecycleOwner) { usersResponse ->
            var sortedList = usersResponse.filter { it.points!! > 0 }.sortedBy { it.rating }
            sortedList.take(3).mapIndexed { i, user -> user.isTopWinnerPosition = i + 1 }
            userRatingAdapter.submitList(sortedList)
        }

        viewModel.groupsRatingResponse.observe(viewLifecycleOwner) { groupsResponse ->
            var sortedList = groupsResponse.sortedBy { it.points }.reversed()
            sortedList.take(3).mapIndexed { i, group -> group.isTopWinnerPosition = i + 1 }
            groupsRatingAdapter.submitList(sortedList)
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUsersRating()
                viewModel.getGroupsRating()
            }
        }

    }

}