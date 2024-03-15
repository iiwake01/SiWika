package com.example.siwika

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationBarModel (
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)