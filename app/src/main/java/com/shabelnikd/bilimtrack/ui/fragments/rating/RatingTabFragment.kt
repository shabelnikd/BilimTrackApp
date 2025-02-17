package com.shabelnikd.bilimtrack.ui.fragments.rating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.adapters.RatingViewPagerAdapter
import com.shabelnikd.bilimtrack.databinding.FragmentRatingTabBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatingTabFragment : Fragment() {

    private var _binding: FragmentRatingTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatingTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerRating.adapter = RatingViewPagerAdapter(this)

        TabLayoutMediator(binding.idTabLayoutRating, binding.viewPagerRating) { tab, position ->
            when (position) {
                0 -> tab.text = "ПО СТУДЕНТАМ"
                1 -> tab.text = "ПО ГРУППАМ"
            }
        }.attach()

    }


    companion object {
        const val ARG_ON_USERS_TOP_POSITION = "onUsersTop"
    }

}