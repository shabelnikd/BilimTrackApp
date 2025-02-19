package com.shabelnikd.bilimtrack.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.databinding.ItemRatingBinding
import com.shabelnikd.bilimtrack.model.core.RetrofitClient.Companion.DOMAIN
import com.shabelnikd.bilimtrack.model.models.RatingUsersResponse

class RatingUsersAdapter() :
    ListAdapter<RatingUsersResponse, RatingUsersAdapter.ViewHolder>(GenericDiffUtil<RatingUsersResponse>()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val userSubject = getItem(position)
        val localPosition = position.plus(1).toString()

        with(holder) {
            binding.lnBlockNamesOnlyUsers.visibility = View.VISIBLE
            binding.tvUserUsername.text = userSubject.username
            binding.tvUserPoints.text = userSubject.points.toString()
            binding.tvRatingPosition.text = localPosition
            when {
                userSubject.firstName != null && userSubject.lastName != null ->
                    binding.tvUserFullName.text = String.format("%s %s", userSubject.firstName, userSubject.lastName)
                else -> binding.tvUserFullName.visibility = View.GONE
            }
        }

        bindAvatar(holder, userSubject)
        bindWinnerStatus(holder, userSubject)

    }


    private fun bindAvatar(holder: ViewHolder, userSubject: RatingUsersResponse) {
        with(holder) {
            when {
                userSubject.photo != null -> {
                    binding.textedAvatarOnlyUsers.visibility = View.INVISIBLE
                    binding.userAvatarOnlyUsers.visibility = View.VISIBLE
                    Glide.with(itemView.context).load(DOMAIN + userSubject.photo)
                        .into(binding.userAvatarOnlyUsers)
                }

                else -> {
                    binding.textedAvatarOnlyUsers.visibility = View.VISIBLE
                    binding.userAvatarOnlyUsers.visibility = View.INVISIBLE
                    binding.textedAvatarOnlyUsers.text = when {
                        userSubject.firstName != null && userSubject.lastName != null -> userSubject.firstName[0].toString()
                        else -> userSubject.username?.get(0).toString()
                    }
                }
            }
        }
    }

    private fun bindWinnerStatus(holder: ViewHolder, userSubject: RatingUsersResponse) {
        with(holder) {
            if (userSubject.isTopWinnerPosition in 1..3) {
                binding.tvRatingPosition.visibility = View.INVISIBLE
                val medalDrawable = when (userSubject.isTopWinnerPosition) {
                    1 -> R.drawable.ic_medal_1
                    2 -> R.drawable.ic_medal_2
                    3 -> R.drawable.ic_medal_3
                    else -> throw IllegalStateException("Invalid winner position")
                }
                binding.isWinnerImage.setImageResource(medalDrawable)
                val backgroundResource = when (userSubject.isTopWinnerPosition) {
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
