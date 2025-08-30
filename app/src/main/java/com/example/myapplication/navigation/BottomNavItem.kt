package com.example.myapplication.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "首页", Icons.Default.Home)
    object Market : BottomNavItem("market", "行情", Icons.Default.ShowChart)
    object Trading : BottomNavItem("trading", "交易", Icons.Default.Money)
    object Wealth : BottomNavItem("wealth", "理财", Icons.Default.Wallet)
    object Profile : BottomNavItem("profile", "我的", Icons.Default.AccountCircle)
} 

