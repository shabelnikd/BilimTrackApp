package com.shabelnikd.bilimtrack.model.models
import com.shabelnikd.bilimtrack.R

enum class BackgroundAchieve(val apiRarityTypeId: Int, val resId: Int) {
    REGULAR(1, R.drawable.bg_rarity_regular),
    RARE(2, R.drawable.bg_rarity_rare),
    EPIC(3, R.drawable.bg_rarity_epic),
    LEGEND(4, R.drawable.bg_rarity_legend),
    MYTHICAL(5, R.drawable.bg_rarity_mythical),
    EXCLUSIVE(6, R.drawable.bg_rarity_exclusive)
}