package com.shabelnikd.bilimtrack.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shabelnikd.bilimtrack.ui.fragments.onboard.OnBoardPageFragment
import com.shabelnikd.bilimtrack.ui.fragments.rating.RatingTabFragment
import com.shabelnikd.bilimtrack.ui.fragments.rating.RatingTabPageFragment
import kotlin.apply
import kotlin.to

class RatingViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int) = RatingTabPageFragment()
        .apply {
            arguments =
                bundleOf(RatingTabFragment.ARG_ON_USERS_TOP_POSITION to position)
        }

}