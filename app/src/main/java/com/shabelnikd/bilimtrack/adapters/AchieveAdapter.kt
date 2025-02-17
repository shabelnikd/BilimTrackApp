package com.shabelnikd.bilimtrack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shabelnikd.bilimtrack.databinding.ItemAchieveBinding
import com.shabelnikd.bilimtrack.model.models.BackgroundAchieve
import com.shabelnikd.bilimtrack.model.models.MeResponse

class AchieveAdapter(
) : ListAdapter<MeResponse.Achievement, AchieveAdapter.ViewHolder>(GenericDiffUtil<MeResponse.Achievement>()) {

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
            binding.tvAchieveName.text = achieveSubject.name
            binding.tvTypeRarity.text = achieveSubject.rarity?.name

            getResIdByApiId(achieveSubject.rarity?.id)?.let {
                binding.tvTypeRarity.setBackgroundResource(it)
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