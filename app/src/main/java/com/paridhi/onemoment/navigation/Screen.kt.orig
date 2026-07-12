package com.paridhi.onemoment.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Timeline : Screen("timeline", "Timeline", Icons.Default.Book)
    data object Reflection : Screen("reflection", "Reflection", Icons.Default.FavoriteBorder)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}
