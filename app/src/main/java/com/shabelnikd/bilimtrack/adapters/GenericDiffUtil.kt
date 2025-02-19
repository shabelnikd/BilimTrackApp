package com.shabelnikd.bilimtrack.adapters

import androidx.recyclerview.widget.DiffUtil
import com.shabelnikd.bilimtrack.model.models.MeResponse
import com.shabelnikd.bilimtrack.model.models.RatingGroupsResponse
import com.shabelnikd.bilimtrack.model.models.RatingUsersResponse
import com.shabelnikd.bilimtrack.model.models.SubjectsMeResponse

class GenericDiffUtil<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when {
            oldItem is SubjectsMeResponse && newItem is SubjectsMeResponse -> oldItem.id == newItem.id
            oldItem is MeResponse.Achievement && newItem is MeResponse.Achievement -> oldItem.id == newItem.id
            oldItem is RatingUsersResponse && newItem is RatingUsersResponse -> oldItem.points == newItem.points
            oldItem is RatingGroupsResponse && newItem is RatingGroupsResponse -> oldItem.id == newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when {
            oldItem is SubjectsMeResponse && newItem is SubjectsMeResponse -> oldItem == newItem
            oldItem is MeResponse.Achievement && newItem is MeResponse.Achievement -> oldItem == newItem
            oldItem is RatingUsersResponse && newItem is RatingUsersResponse -> oldItem == newItem
            oldItem is RatingGroupsResponse && newItem is RatingGroupsResponse -> oldItem == newItem
            else -> false
        }
    }
}