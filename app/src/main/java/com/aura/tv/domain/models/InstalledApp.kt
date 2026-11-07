package com.aura.tv.domain.models

import android.graphics.drawable.Drawable

data class InstalledApp(
    val packageName: String,
    val activityName: String,
    val label: String,
    val icon: Drawable? = null,
    val isLeanback: Boolean = true,
    val isFavorite: Boolean = false
)
