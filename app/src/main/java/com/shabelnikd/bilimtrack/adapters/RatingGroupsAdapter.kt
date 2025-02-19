package com.shabelnikd.bilimtrack.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.databinding.ItemRatingBinding
import com.shabelnikd.bilimtrack.model.models.RatingGroupsResponse
import com.shabelnikd.bilimtrack.model.models.RatingUsersResponse
import kotlin.ranges.contains

class RatingGroupsAdapter() :
    ListAdapter<RatingGroupsResponse, RatingGroupsAdapter.ViewHolder>(GenericDiffUtil<RatingGroupsResponse>()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val groupSubject = getItem(position)
        with(holder) {
            binding.tvGroupNameOnlyGroups.visibility = View.VISIBLE
            binding.tvGroupNameOnlyGroups.text = groupSubject.name
            binding.tvUserPoints.text = groupSubject.points.toString()
            binding.tvRatingPosition.text = position.toString()
        }

        bindWinnerStatus(holder, groupSubject)

    }

    private fun bindWinnerStatus(holder: ViewHolder, groupSubject: RatingGroupsResponse) {
        with(holder) {
            if (groupSubject.isTopWinnerPosition in 1..3) {
                binding.tvRatingPosition.visibility = View.INVISIBLE
                val medalDrawable = when (groupSubject.isTopWinnerPosition) {
                    1 -> R.drawable.ic_medal_1
                    2 -> R.drawable.ic_medal_2
                    3 -> R.drawable.ic_medal_3
                    else -> throw IllegalStateException("Invalid winner position")
                }
                binding.isWinnerImage.setImageResource(medalDrawable)
                val backgroundResource = when (groupSubject.isTopWinnerPosition) {
                    1 -> R.color.winnerPos1
                    2 -> R.color.winnerPos2
                    3 -> R.color.winnerPos3
                    else -> throw IllegalStateException("Invalid winner position")
                }
                itemView.setBackgroundResource(backgroundResource)
            } else {
                binding.isWinnerImage.setImageResource(R.color.white)
                binding.tvRatingPosition.visibility = View.VISIBLE
                itemView.setBackgroundResource(R.color.white)
            }
        }
    }

    class ViewHolder(val binding: ItemRatingBinding) : RecyclerView.ViewHolder(binding.root)

}
