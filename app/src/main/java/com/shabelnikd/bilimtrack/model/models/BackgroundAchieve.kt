package com.shabelnikd.bilimtrack.model.models

import com.shabelnikd.bilimtrack.R

enum class BackgroundAchieve(val apiRarityTypeId: Int, val resId: Int, val progressResId: Int) {
    REGULAR(1, R.drawable.bg_rarity_regular, R.drawable.progress_regular),
    RARE(2, R.drawable.bg_rarity_rare, R.drawable.progress_rare),
    EPIC(3, R.drawable.bg_rarity_epic, R.drawable.progress_epic),
    LEGEND(4, R.drawable.bg_rarity_legend, R.drawable.progress_legend),
    MYTHICAL(5, R.drawable.bg_rarity_mythical, R.drawable.progress_mythical),
    EXCLUSIVE(6, R.drawable.bg_rarity_exclusive, R.drawable.progress_exclusive)
}