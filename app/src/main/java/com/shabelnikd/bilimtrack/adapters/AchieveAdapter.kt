package com.shabelnikd.bilimtrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shabelnikd.bilimtrack.databinding.ItemAchieveBinding
import com.shabelnikd.bilimtrack.model.models.AchievementsResponse
import com.shabelnikd.bilimtrack.model.models.BackgroundAchieve

class AchieveAdapter(
) : ListAdapter<AchievementsResponse, AchieveAdapter.ViewHolder>(GenericDiffUtil<AchievementsResponse>()) {

    private var onClick: ((achieve: AchievementsResponse) -> Unit)? = null

    fun setOnClickListener(listener: (achieve: AchievementsResponse) -> Unit) {
        this.onClick = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAchieveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val achieveSubject = getItem(position)
        with(holder) {

            itemView.setOnClickListener {
                onClick?.invoke(achieveSubject)
            }

            binding.tvAchieveName.text = achieveSubject.name
            getResIdByApiId(achieveSubject.rarity?.id)?.let {
                binding.forSetBackground.setBackgroundResource(it)
            }

            when (achieveSubject.isOpened) {
                true -> binding.isOpened.visibility = View.INVISIBLE
                else -> binding.isOpened.visibility = View.VISIBLE
            }

            achieveSubject.photo?.let { photoUrl ->
                Glide.with(binding.root).load(photoUrl).into(binding.achieveImage)
            }
        }

    }

    fun getResIdByApiId(apiRarityTypeId: Int?): Int? {
        return BackgroundAchieve.entries.find { it.apiRarityTypeId == apiRarityTypeId }?.resId
    }

    class ViewHolder(val binding: ItemAchieveBinding) : RecyclerView.ViewHolder(binding.root)

}