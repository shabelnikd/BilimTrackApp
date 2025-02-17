package com.shabelnikd.bilimtrack.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shabelnikd.bilimtrack.ui.fragments.onboard.OnBoardPageFragment
import kotlin.apply
import kotlin.to

class OnBoardAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int) = OnBoardPageFragment()
        .apply {
            arguments =
                bundleOf(OnBoardPageFragment.ARG_ON_BOARD_POSITION to position)
        }

}