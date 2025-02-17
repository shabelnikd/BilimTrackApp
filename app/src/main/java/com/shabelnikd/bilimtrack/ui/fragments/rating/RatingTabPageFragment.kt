package com.shabelnikd.bilimtrack.ui.fragments.rating

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.adapters.RatingUsersAdapter
import com.shabelnikd.bilimtrack.databinding.FragmentRatingTabBinding
import com.shabelnikd.bilimtrack.databinding.FragmentRatingTabPageBinding
import com.shabelnikd.bilimtrack.ui.fragments.rating.RatingTabFragment.Companion.ARG_ON_USERS_TOP_POSITION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatingTabPageFragment : Fragment() {

    private val viewModel: RatingTabPageViewModel by viewModels()

    private var _binding: FragmentRatingTabPageBinding? = null
    private val binding get() = _binding!!

    private val userRatingAdapter = RatingUsersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingTabPageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (arguments?.getInt(ARG_ON_USERS_TOP_POSITION)) {
            0 -> {
                binding.rvTop.adapter = userRatingAdapter
            }

            1 -> {

            }
        }

    }

}