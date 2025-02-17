package com.shabelnikd.bilimtrack.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shabelnikd.bilimtrack.databinding.ItemRatingBinding
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val userSubject = getItem(position)
        with(holder) {
            binding.lnBlockNamesOnlyUsers.visibility = View.VISIBLE

            binding.tvUserUsername.text = userSubject.username
            binding.tvUserPoints.text = userSubject.points.toString()
            binding.tvRatingPosition.text = position.toString()

            if (userSubject.firstName != null && userSubject.lastName != null) {
                binding.tvUserFullName.text = userSubject.firstName + " " + userSubject.lastName
                if (userSubject.photo == null) {
                    binding.textedAvatarOnlyUsers.visibility = View.VISIBLE
                    binding.textedAvatarOnlyUsers.text = userSubject.firstName[0].toString()
                }
            } else {
                if (userSubject.photo == null) {
                    binding.textedAvatarOnlyUsers.visibility = View.VISIBLE
                    binding.textedAvatarOnlyUsers.text = userSubject.username?.get(0).toString()
                }
            }

            if (userSubject.photo != null) {
                binding.textedAvatarOnlyUsers.visibility = View.INVISIBLE
                binding.userAvatarOnlyUsers.visibility = View.VISIBLE
                Glide.with(binding.root).load(userSubject.photo).into(binding.userAvatarOnlyUsers)
            }
        }

    }

    class ViewHolder(val binding: ItemRatingBinding) : RecyclerView.ViewHolder(binding.root)

}
